package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

public class WriteFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public Date lastModifiedTime;
    public boolean success;

    public WriteFileResponse(boolean success) {
        this.success = success;
    }
}