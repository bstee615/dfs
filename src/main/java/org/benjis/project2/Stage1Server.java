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
import org.benjis.project2.messages.LookupFileRequest;
import org.benjis.project2.messages.LookupFileResponse;
import org.benjis.project2.messages.ReadFileRequest;
import org.benjis.project2.messages.ReadFileResponse;
import org.benjis.project2.messages.WriteFileRequest;
import org.benjis.project2.messages.WriteFileResponse;

// https://www.baeldung.com/a-guide-to-java-sockets
class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ReadFileResponse handle(ReadFileRequest req) throws IOException {
        File file = new File(req.path);
        // if (!file.exists()) {
        // Handle it
        // }

        FileInputStream in = new FileInputStream(file);
        // It's fine if we set the position past EOF,
        // since a consequent read will just return empty.
        in.getChannel().position(req.position);
        byte[] data = new byte[req.n];
        int bytesRead = in.read(data);
        if (bytesRead == -1)
            bytesRead = 0;
        in.close();

        return new ReadFileResponse(bytesRead, data);
    }

    private boolean writeHelper(WriteFileRequest req) throws IOException {
        File file = new File(req.path);
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
                if (bytesSaved < req.position - 1) {
                    return false;
                }
            } finally {
                in.close();
            }

            FileOutputStream out = new FileOutputStream(file);
            out.write(fileContents, 0, req.position - 1);
            out.write(req.data);
            if (req.position + req.length < fileContents.length) {
                out.write(fileContents, req.position + req.length, fileContents.length - (req.position + req.length));
            }
            out.close();

            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    private WriteFileResponse handle(WriteFileRequest req) throws IOException {
        System.out.println("Writing \"" + new String(req.data) + "\" to " + req.path);
        boolean success = writeHelper(req);
        if (success) {
            System.out.println("success");
        } else {
            System.out.println("failure");
        }
        return new WriteFileResponse(success);
    }

    private LookupFileResponse handle(LookupFileRequest req) {
        System.out.println("Looking up file " + req.path);
        File file = new File(req.path);
        boolean exists = file.exists();
        int length = 0;
        if (exists) {
            length = (int) file.length();
        }
        return new LookupFileResponse(exists, length);
    }

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
        default:
            System.out.println("Unknown verb " + m.verb);
            response = null;
            break;
        }

        if (response != null) {
            out.writeObject(response);
            // System.out.println("wrote response");
        }
    }

    public void start(InetAddress addr, int port) throws IOException {
        serverSocket = new ServerSocket(port, 50, addr);
        Path currentRelativePath = Paths.get("");
        System.out.println("Serving at " + addr.toString() + ":" + port + " at path "
                + currentRelativePath.toAbsolutePath().toString());

        while (true) {
            clientSocket = serverSocket.accept();
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // try {
            Object o = null;
            // Read objects until they stop sending them.
            try {
                // while
                if (/* clientSocket.isConnected() && */(o = in.readObject()) != null) {
                    ClientMessage m = (ClientMessage) o;
                    handle(m);
                }
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Client disconnected. Accepting connections...");
            // }
            // catch (SocketException ex) {
            // if (ex.getMessage().equals("Connection reset")) {
            // // Silently reset for a new client
            // } else {
            // throw ex;
            // }
            // }

            in.close();
            out.close();
            clientSocket.close();
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}

public class Stage1Server {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(InetAddress.getByName("127.0.0.1"), 6666);
        } catch (IOException ex) {
            System.out.println("Some error: " + ex.getMessage());
        }
    }
}
