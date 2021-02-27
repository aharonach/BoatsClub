package utils;

public class Utils {
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getOS() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return "windows";
        } else if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
            return "linux";
        }
        return "none";
    }
}
