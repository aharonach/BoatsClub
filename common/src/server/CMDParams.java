package server;

public class CMDParams {
    static String host = "localhost";
    static int port = 1989;

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        CMDParams.port = port;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        CMDParams.host = host;
    }
}
