package org.benjis.project2;

import java.io.IOException;

public class Stage1Client {
    public static void main(String[] args) {
        try {
            FileSystemAPI api = new NetworkFileSystem();
            FileHandle fh = api.open("localhost:6666/testfile.txt");
            System.out.println(fh.toString());

            byte[] inData = new byte[45]; // Read the first line only
            int bytesRead = api.read(fh, inData);
            System.out.println("Read " + bytesRead + " bytes of data: \"" + new String(inData) + "\"");

            byte[] outData = "Benji was here.".getBytes();
            api.write(fh, outData);
        } catch (IOException ex) {
            System.out.println("some error: " + ex.getMessage());
        }
    }
}
