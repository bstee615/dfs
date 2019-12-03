package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

public class ReadFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public int bytesRead;
    public Date lastModifiedTime;
    public byte[] data;

    public ReadFileResponse(int bytesRead, byte[] data) {
        this.bytesRead = bytesRead;
        this.data = data;
    }
}