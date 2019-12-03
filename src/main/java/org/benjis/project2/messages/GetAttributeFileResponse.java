package org.benjis.project2.messages;

import java.io.Serializable;

// res = (ts)
public class GetAttributeFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    long ts;

    public GetAttributeFileResponse(long ts) {
        this.ts = ts;
    }
}
