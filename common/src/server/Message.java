package server;

public class Message {
    private final boolean isGlobal;
    private final String message;
    private final Integer orderId;

    public Message(boolean isGlobal, String message, Integer orderId) {
        this.isGlobal = isGlobal;
        this.message = message;
        this.orderId = orderId;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public String getMessage() {
        return message;
    }

    public Integer getOrderId() {
        return orderId;
    }
}
