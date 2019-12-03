package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

// res = (bytes, ts)
public class ReadFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public int numBytesRead;
    public byte[] bytesRead;
    public Date ts;

    public ReadFileResponse(int bytesRead, byte[] data) {
        this.numBytesRead = bytesRead;
        this.bytesRead = data;
    }
}