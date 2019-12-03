package org.benjis.project2;

import java.io.IOException;

public class Stage1Client {
    public static void main(String[] args) {
        try {
            FileSystemAPI api = new NetworkFileSystem();
            api.open("localhost:6666/hello.txt");
        } catch (IOException ex) {
            System.out.println("some error: " + ex.getMessage());
        }
    }
}
