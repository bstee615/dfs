package org.benjis.project2.messages;

import java.io.Serializable;

public class WriteFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;
    public byte[] data;
    public int position;
    public int length;

    public WriteFileRequest(String path, byte[] data, int position, int length) {
        this.path = path;
        this.data = data;
        this.position = position;
        this.length = length;
    }
}