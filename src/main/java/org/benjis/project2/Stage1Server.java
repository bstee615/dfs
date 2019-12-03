package org.benjis.project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

    private ReadFileResponse handle(ReadFileRequest req) {
        throw new UnsupportedOperationException("not implemented");
    }

    private WriteFileResponse handle(WriteFileRequest req) {
        throw new UnsupportedOperationException("not implemented");
    }

    private LookupFileResponse handle(LookupFileRequest req) {
        throw new UnsupportedOperationException("not implemented");
    }

    private void handle(ClientMessage m) throws IOException {
        Serializable response;

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
        }
    }

    public void start(InetAddress addr, int port) throws IOException {
        serverSocket = new ServerSocket(port, 50, addr);
        clientSocket = serverSocket.accept();
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        while (true) {
            try {
                ClientMessage m = (ClientMessage) in.readObject();
                handle(m);
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
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
