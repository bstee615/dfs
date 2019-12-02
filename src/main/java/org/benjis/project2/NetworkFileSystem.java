package org.benjis.project2;

public class NetworkFileSystem implements FileSystemAPI {

  // url format is "ip:port/path"
  public FileHandle open(String url) throws java.io.FileNotFoundException {
    int colonIndex = url.indexOf(":");
    int slashIndex = url.indexOf("/");
    String ip = url.substring(0, colonIndex);
    String port = url.substring(colonIndex + 1, slashIndex);
    String path = url.substring(slashIndex + 1);
    // TODO: Check format
    return new NetworkFileHandle(ip, port, path);
  }

  /* write is not implemented. */
  public boolean write(FileHandle fh, byte[] data) throws java.io.IOException {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /* read bytes from the current position. returns the number of bytes read. */
  public int read(FileHandle fh, byte[] data) throws java.io.IOException {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /* close file. */
  public boolean close(FileHandle fh) throws java.io.IOException {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /* check if it is the end-of-file. */
  public boolean isEOF(FileHandle fh) throws java.io.IOException {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
