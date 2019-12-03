package org.benjis.project2.messages;

import java.io.Serializable;

// read(filename, offset, n)
public class ReadFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String filename;
    public int offset;
    public int n;

    public ReadFileRequest(String path, int position, int n) {
        this.filename = path;
        this.offset = position;
        this.n = n;
    }
}