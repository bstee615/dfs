package org.benjis.project2.messages;

import java.io.Serializable;

// res = (success, ts)
public class WriteFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean success;
    public long ts;

    public WriteFileResponse(boolean success, long ts) {
        this.success = success;
        this.ts = ts;
    }
}