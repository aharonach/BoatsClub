package server;

import java.io.Serializable;

public class Request implements Serializable {
    private Integer userId;
    private final String entityType;
    private final String methodName;
    private final Class<?>[] types;
    private final Object[] args;

    public Request(Integer userId, String entityType, String methodName, Object[] args, Class<?>[] types) {
        this.userId = userId;
        this.entityType = entityType;
        this.methodName = methodName;
        this.args = args;
        this.types = types;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public Integer getUserId() {
        return userId;
    }
}
