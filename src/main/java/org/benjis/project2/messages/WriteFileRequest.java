package org.benjis.project2.messages;

import java.io.Serializable;

public class WriteFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;
    public byte[] data;

    public WriteFileRequest(String path, byte[] data) {
        this.path = path;
        this.data = data;
    }
}