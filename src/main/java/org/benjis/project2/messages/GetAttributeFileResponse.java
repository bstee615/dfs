package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

// res = (ts)
public class GetAttributeFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    Date ts;

    public GetAttributeFileResponse() {

    }
}
