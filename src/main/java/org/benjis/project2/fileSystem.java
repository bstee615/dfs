package org.benjis.project2;

/* This is a simple example implementation of fileSystemAPI, 
   using local file system calls. 
*/

/* standard java classes. */
import java.io.*;
import java.util.*;

public class FileSystem implements FileSystemAPI {
  /* It needs a table relating filehandles and real files. */
  Hashtable tbl = new Hashtable();

  /* url SHOULD HAVE form IP:port/path, but here simply a file name. */

  public FileHandle open(String url) throws java.io.FileNotFoundException {
    FileInputStream in = new FileInputStream(new File(url));
    FileHandle fh = new FileHandle();
    tbl.put(fh, in);
    return fh;
  }

  /* write is not implemented. */
  public boolean write(FileHandle fh, byte[] data) throws java.io.IOException {
    return true;
  }

  /* read bytes from the current position. returns the number of bytes read. */
  public int read(FileHandle fh, byte[] data) throws java.io.IOException {
    FileInputStream in = (FileInputStream) tbl.get(fh);
    int res = in.read(data);
    return res;
  }

  /* close file. */
  public boolean close(FileHandle fh) throws java.io.IOException {
    ((FileInputStream) tbl.get(fh)).close();
    tbl.remove(fh);
    fh.discard();
    return true;
  }

  /* check if it is the end-of-file. */
  public boolean isEOF(FileHandle fh) throws java.io.IOException {
    byte[] dummy = { 0 };
    return (((FileInputStream) tbl.get(fh)).available() == 0);
  }
}
