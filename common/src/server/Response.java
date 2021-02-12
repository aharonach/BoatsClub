package server;

import java.io.Serializable;

public class Response implements Serializable {
    boolean isUserActive = false;
    Object object = null;
    Throwable exception = null;

    public Response() {}

    public Response(/*boolean isUserActive,*/ Object object, Throwable exception){
//        this.isUserActive = isUserActive;
        this.object = object;
        if (exception != null) {
            this.exception = exception;
        }
    }

    public Object getObject(){
        return object;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean isUserActive() {
        return isUserActive;
    }
}
