package org.benjis.project2.messages;

import java.io.Serializable;

// getattr(filename)
public class GetAttributeFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public String filename;

    public GetAttributeFileRequest(String filename) {
        this.filename = filename;
    }
}
