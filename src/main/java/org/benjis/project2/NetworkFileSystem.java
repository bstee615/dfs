package org.benjis.project2;

import java.util.Hashtable;

public class NetworkFileSystem implements FileSystemAPI {
  private Hashtable<FileHandle, NFSUrl> fhToUrl;

  private class NFSUrl {
    public String ip;
    public String port;
    public String path;

    public NFSUrl(String url) {
      int colonIndex = url.indexOf(":");
      int slashIndex = url.indexOf("/");
      String ip = url.substring(0, colonIndex);
      String port = url.substring(colonIndex + 1, slashIndex);
      String path = url.substring(slashIndex + 1);

      // TODO: Check format

      this.ip = ip;
      this.port = port;
      this.path = path;
    }
  }

  // url format is "ip:port/path"
  public FileHandle open(String url) throws java.io.FileNotFoundException {
    FileHandle fh = new FileHandle();
    NFSUrl nfsUrl = new NFSUrl(url);

    fhToUrl.put(fh, nfsUrl);

    return fh;
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
