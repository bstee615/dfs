package org.benjis.project2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Hashtable;

import org.benjis.project2.messages.ClientMessage;
import org.benjis.project2.messages.LookupFileRequest;
import org.benjis.project2.messages.LookupFileResponse;
import org.benjis.project2.messages.ReadFileRequest;
import org.benjis.project2.messages.ReadFileResponse;
import org.benjis.project2.messages.WriteFileRequest;
import org.benjis.project2.messages.WriteFileResponse;

// An implementation of FileSystemAPI for access over the Internet.
public class NetworkFileSystem implements FileSystemAPI {
  private Hashtable<FileHandle, FileData> fileHandlesToData;

  public NetworkFileSystem() {
    this.fileHandlesToData = new Hashtable<>();
  }

  // A bag of data about each open file handle.
  private class FileData {
    public InetSocketAddress ip;
    public String path;

    public int position;
    public Integer length;

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
      this.length = 0;
    }
  }

  // A utility method to create a socket for an open file.
  private Socket getSocket(FileData fd) throws IOException {
    return new Socket(fd.ip.getAddress(), fd.ip.getPort());
  }

  // A utility method to write a request to the server.
  private void writeToSock(Socket sock, ClientMessage m) throws IOException {
    ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
    out.writeObject(m);
  }

  // A utility method to read a response from the server.
  @SuppressWarnings("unchecked")
  private <T extends Serializable> T readFromSock(Socket sock) throws IOException {
    ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
    try {
      return (T) in.readObject();
    } catch (ClassNotFoundException ex) {
      System.out.println(ex.getMessage());
      return null;
    }
  }

  // Get data about an open file.
  FileData getFileData(FileHandle fh) {
    if (!fh.isAlive()) {
      System.out.println("Invalid handle");
    }
    FileData fd = fileHandlesToData.get(fh);
    if (fd.length == null) {
      System.out.println("Uninitialized FileData");
    }
    return fd;
  }

  // Get info from the server and store the handle for a file.
  // url format is "ip:port/path"
  public FileHandle open(String url) throws FileNotFoundException {
    FileData fileData = new FileData(url);
    Socket sock = null;
    try {
      sock = getSocket(fileData);
      LookupFileRequest outData = new LookupFileRequest(fileData.path);
      writeToSock(sock, new ClientMessage(outData));

      LookupFileResponse inData = readFromSock(sock);

      if (inData.fileExists) {
        fileData.length = inData.size;
        FileHandle fh = new FileHandle();
        fileHandlesToData.put(fh, fileData);
        return fh;
      }
    } catch (IOException ex) {
      System.out.println("open: " + ex.getMessage());
    } finally {
      if (sock != null) {
        try {
          sock.close();
        } catch (IOException ex) {
          System.out.println("ouch! things are messed up");
        }
      }
    }

    return null;
  }

  // Write data to a file, starting from the current position.
  public boolean write(FileHandle fh, byte[] data) throws IOException {
    FileData fileData = getFileData(fh);
    Socket sock = getSocket(fileData);
    boolean success;

    try {
      WriteFileRequest outData = new WriteFileRequest(fileData.path, data, fileData.position, data.length);
      writeToSock(sock, new ClientMessage(outData));

      WriteFileResponse inData = readFromSock(sock);

      success = inData.success;
    } catch (IOException ex) {
      System.out.println("write(): network error");
      success = false;
    } finally {
      if (sock != null)
        sock.close();
    }

    if (success) {
      fileData.position += data.length;
    }

    return success;
  }

  // Read data from a file, starting from the current position.
  public int read(FileHandle fh, byte[] data) throws IOException {
    FileData fileData = getFileData(fh);
    Socket sock = getSocket(fileData);

    ReadFileRequest outData = new ReadFileRequest(fileData.path, fileData.position, data.length);
    writeToSock(sock, new ClientMessage(outData));

    ReadFileResponse inData = readFromSock(sock);

    sock.close();
    System.arraycopy(inData.bytesRead, 0, data, 0, inData.numBytesRead);
    fileData.position += inData.numBytesRead;
    return inData.numBytesRead;
  }

  // Close an open file.
  public boolean close(FileHandle fh) throws IOException {
    if (fh.isAlive()) {
      fh.discard();
      fileHandlesToData.remove(fh);
      return true;
    } else {
      return false;
    }
  }

  // Check the server for if a file is EOF.
  public boolean isEOF(FileHandle fh) throws IOException {
    FileData fileData = getFileData(fh);
    return fileData.position >= fileData.length;
  }
}
