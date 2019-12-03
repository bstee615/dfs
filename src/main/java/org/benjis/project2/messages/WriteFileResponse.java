package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

// res = (success, ts)
public class WriteFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean success;
    public Date ts;

    public WriteFileResponse(boolean success) {
        this.success = success;
    }
}