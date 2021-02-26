package server;

import java.io.Serializable;

public class Response implements Serializable {
    String error = null;
    Object value = null;
    final Boolean status;

    public Response(Boolean status, Object value){
        this.status = status;
        if (this.status == null || this.status) {
            this.value = value;
        } else {
            this.error = (String) value;
        }
    }

    public Object getValue(){
        return value;
    }
}
