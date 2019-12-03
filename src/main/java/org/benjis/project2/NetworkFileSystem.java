package org.benjis.project2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

public class NetworkFileSystem implements FileSystemAPI {
  private Hashtable<FileHandle, FileData> fhToUrl;

  private class FileData {
    public String ip;
    public String port;
    public String path;

    public int position;

    public FileData(String url) {
      int colonIndex = url.indexOf(":");
      int slashIndex = url.indexOf("/");
      String ip = url.substring(0, colonIndex);
      String port = url.substring(colonIndex + 1, slashIndex);
      String path = url.substring(slashIndex + 1);

      // TODO: Check format

      this.ip = ip;
      this.port = port;
      this.path = path;

      this.position = 0;
    }
  }

  // url format is "ip:port/path"
  public FileHandle open(String url) throws FileNotFoundException {
    FileHandle fh = new FileHandle();
    FileData nfsUrl = new FileData(url);

    fhToUrl.put(fh, nfsUrl);

    return fh;
  }

  private URLConnection getURLConnection(FileData fileData) throws IOException {
    URL netUrl = new URL(fileData.ip + ":" + fileData.port + "/" + fileData.path);

    URLConnection con = netUrl.openConnection();
    con.setDoOutput(true);
    return con;
  }

  /* write is not implemented. */
  public boolean write(FileHandle fh, byte[] data) throws IOException {
    FileData fileData = fhToUrl.get(fh);
    URLConnection con = getURLConnection(fileData);

    try {
      BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
      InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(data));
      int c;

      while ((c = reader.read()) != -1) {
        outWriter.write(c);
      }

      outWriter.close();
      reader.close();
    } catch (IOException ex) {
      return false;
    }

    return true;
  }

  /* read bytes from the current position. returns the number of bytes read. */
  public int read(FileHandle fh, byte[] data) throws IOException {
    FileData fileData = fhToUrl.get(fh);
    URLConnection con = getURLConnection(fileData);

    return con.getInputStream().read(data);
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
