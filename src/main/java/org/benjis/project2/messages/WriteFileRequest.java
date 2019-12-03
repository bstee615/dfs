package org.benjis.project2.messages;

import java.io.Serializable;

// write(filename, offset, data)
public class WriteFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String filename;
    public byte[] data;
    public int offset;
    public int length;

    public WriteFileRequest(String path, byte[] data, int position, int length) {
        this.filename = path;
        this.data = data;
        this.offset = position;
        this.length = length;
    }
}