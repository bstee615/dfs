package org.benjis.project2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.benjis.project2.messages.ClientMessage;
import org.benjis.project2.messages.GetAttributeFileRequest;
import org.benjis.project2.messages.GetAttributeFileResponse;
import org.benjis.project2.messages.LookupFileRequest;
import org.benjis.project2.messages.LookupFileResponse;
import org.benjis.project2.messages.ReadFileRequest;
import org.benjis.project2.messages.ReadFileResponse;
import org.benjis.project2.messages.WriteFileRequest;
import org.benjis.project2.messages.WriteFileResponse;

// A server to implement the protocol for network file access.
// https://www.baeldung.com/a-guide-to-java-sockets
class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Handle a read() request.
    private ReadFileResponse handle(ReadFileRequest req) throws IOException {
        File file = new File(req.filename);
        // if (!file.exists()) {
        // Handle it
        // }

        FileInputStream in = new FileInputStream(file);
        // It's fine if we set the position past EOF,
        // since a consequent read will just return empty.
        in.getChannel().position(req.offset);
        byte[] data = new byte[req.n];
        int bytesRead = in.read(data);
        if (bytesRead == -1)
            bytesRead = 0;
        in.close();

        return new ReadFileResponse(bytesRead, data, getTS(req.filename));
    }

    // Helper method for handling a write()
    private boolean writeHelper(WriteFileRequest req) throws IOException {
        File file = new File(req.filename);
        if (!file.exists()) {
            return false;
        }

        try {
            // Save current file contents
            FileInputStream in = null;
            byte[] fileContents;
            try {
                in = new FileInputStream(file);
                fileContents = new byte[(int) file.length()];
                int bytesSaved = in.read(fileContents);
                if (bytesSaved < req.offset - 1) {
                    return false;
                }
            } finally {
                in.close();
            }

            FileOutputStream out = new FileOutputStream(file);
            out.write(fileContents, 0, req.offset - 1);
            out.write(req.data);
            if (req.offset + req.length < fileContents.length) {
                out.write(fileContents, req.offset + req.length, fileContents.length - (req.offset + req.length));
            }
            out.close();

            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    // Handle a write() request.
    private WriteFileResponse handle(WriteFileRequest req) throws IOException {
        System.out.println("Writing \"" + new String(req.data) + "\" to " + req.filename);
        boolean success = writeHelper(req);
        if (success) {
            System.out.println("success");
        } else {
            System.out.println("failure");
        }
        return new WriteFileResponse(success, getTS(req.filename));
    }

    // Handle lookup()
    private LookupFileResponse handle(LookupFileRequest req) throws IOException {
        System.out.println("Looking up file " + req.filename);
        File file = new File(req.filename);
        boolean exists = file.exists();
        int length = 0;
        if (exists) {
            length = (int) file.length();
        }
        return new LookupFileResponse(exists, length, getTS(req.filename));
    }

    // Handle getattr()
    private GetAttributeFileResponse handle(GetAttributeFileRequest req) throws IOException {
        return new GetAttributeFileResponse(getTS(req.filename));
    }

    private long getTS(String filename) throws IOException {
        return new File(filename).lastModified();
    }

    // Switch to handle requests from the client.
    private void handle(ClientMessage m) throws IOException {
        Serializable response;

        System.out.println("Received verb \"" + m.verb + "\"");

        switch (m.verb) {
        case "read":
            response = handle((ReadFileRequest) m.obj);
            break;
        case "write":
            response = handle((WriteFileRequest) m.obj);
            break;
        case "lookup":
            response = handle((LookupFileRequest) m.obj);
            break;
        case "getattr":
            response = handle((GetAttributeFileRequest) m.obj);
            break;
        default:
            System.out.println("Unknown verb " + m.verb);
            response = null;
            break;
        }

        if (response != null) {
            out.writeObject(response);
        }
    }

    // Start the server and serve requests until the cows come home.
    public void start(InetAddress addr, int port) throws IOException {
        serverSocket = new ServerSocket(port, 50, addr);
        Path currentRelativePath = Paths.get("");
        System.out.println(String.format("Serving at %s:%d at path %s. Press CTRL+C to quit.", addr.toString(), port,
                currentRelativePath.toAbsolutePath().toString()));

        // Loop until Ctrl+C is pressed.
        while (true) {
            clientSocket = serverSocket.accept();
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Object o = null;
            try {
                if ((o = in.readObject()) != null) {
                    ClientMessage m = (ClientMessage) o;
                    handle(m);
                }
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Client disconnected. Accepting connections...");

            in.close();
            out.close();
            clientSocket.close();
        }
    }

    // Stop the server and shutdown all resources.
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}

// A thin wrapper to run the server.
public class Stage1Server {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: Stage1Server <host> <port>");
            return;
        }

        Server server = new Server();
        try {
            server.start(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
        } catch (IOException ex) {
            System.out.println("Some error: " + ex.getMessage());
        }
    }
}
