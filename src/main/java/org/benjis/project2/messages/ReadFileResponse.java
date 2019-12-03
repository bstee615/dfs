package org.benjis.project2.messages;

import java.io.Serializable;

// res = (bytes, ts)
public class ReadFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public int numBytesRead;
    public byte[] bytesRead;
    public long ts;

    public ReadFileResponse(int bytesRead, byte[] data, long ts) {
        this.numBytesRead = bytesRead;
        this.bytesRead = data;
        this.ts = ts;
    }
}