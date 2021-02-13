package server;

import java.io.Serializable;

public class Response implements Serializable {
    String error = null;
    Object value;
    boolean status;

    public Response(boolean status, Object value){
        this.status = status;
        if (this.status) {
            this.value = value;
        } else {
            this.error = (String) value;
            this.value = null;
        }
    }

    public Object getValue(){
        return value;
    }
}
