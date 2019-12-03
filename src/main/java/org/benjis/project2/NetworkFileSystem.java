package org.benjis.project2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Hashtable;

public class NetworkFileSystem implements FileSystemAPI {
  private Hashtable<FileHandle, FileData> fhToUrl;

  private class FileData {
    public InetSocketAddress ip;
    public String path;

    public int position;

    public FileData(String url) {
      int colonIndex = url.indexOf(":");
      int slashIndex = url.indexOf("/");

      String addr = url.substring(0, colonIndex);
      int port = Integer.parseInt(url.substring(colonIndex + 1, slashIndex));
      this.ip = new InetSocketAddress(addr, port);

      String path = url.substring(slashIndex + 1);
      // TODO: Check format
      this.path = path;

      this.position = 0;
    }

    public Socket getSocket() throws IOException {
      return new Socket(ip.getAddress(), ip.getPort());
    }
  }

  // url format is "ip:port/path"
  public FileHandle open(String url) throws FileNotFoundException {
    FileHandle fh = new FileHandle();
    FileData nfsUrl = new FileData(url);

    fhToUrl.put(fh, nfsUrl);

    return fh;
  }

  private class WriteFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;
    public byte[] data;

    public WriteFileRequest(String path, byte[] data) {
      this.path = path;
      this.data = data;
    }
  }

  private class WriteFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean success;
  }

  private class ReadFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;

    public ReadFileRequest(String path) {
      this.path = path;
    }
  }

  private class ReadFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public int bytesRead;
    public byte[] data;
  }

  private void writeToSock(Socket sock, Object o) throws IOException {
    ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
    out.writeObject(o);
    // out.close();
  }

  private Object readFromSock(Socket sock) throws IOException {
    ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
    try {
      return in.readObject();
    } catch (ClassNotFoundException ex) {
      System.out.println(ex.getMessage());
      return null;
    }
    // finally {
    // in.close();
    // }
  }

  /* write is not implemented. */
  public boolean write(FileHandle fh, byte[] data) throws IOException {
    FileData fileData = fhToUrl.get(fh);
    Socket sock = fileData.getSocket();

    try {
      WriteFileRequest outData = new WriteFileRequest(fileData.path, data);
      writeToSock(sock, outData);

      WriteFileResponse inData = (WriteFileResponse) readFromSock(sock);

      return inData.success;
    } catch (IOException ex) {
      System.out.println("write(): network error");
      return false;
    } finally {
      sock.close();
    }
  }

  /* read bytes from the current position. returns the number of bytes read. */
  public int read(FileHandle fh, byte[] data) throws IOException {
    FileData fileData = fhToUrl.get(fh);
    Socket sock = fileData.getSocket();

    ReadFileRequest outData = new ReadFileRequest(fileData.path);
    writeToSock(sock, outData);

    ReadFileResponse inData = (ReadFileResponse) readFromSock(sock);

    sock.close();
    System.arraycopy(inData.data, 0, data, 0, inData.bytesRead);
    return inData.bytesRead;
  }

  /* close file. */
  public boolean close(FileHandle fh) throws IOException {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /* check if it is the end-of-file. */
  public boolean isEOF(FileHandle fh) throws IOException {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
