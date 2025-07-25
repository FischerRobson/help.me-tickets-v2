package com.helpme.tickets.utils;

public class FileSizeUtils {

    private static final double ONE_KB = 1024.0;
    private static final double ONE_MB = ONE_KB * 1024.0;

    public static double toKilobytes(long bytes) {
        return bytes / ONE_KB;
    }

    public static double toMegabytes(long bytes) {
        return bytes / ONE_MB;
    }

    public static String formatSize(long bytes) {
        if (bytes >= ONE_MB) {
            return String.format("%.2f MB", toMegabytes(bytes));
        } else if (bytes >= ONE_KB) {
            return String.format("%.2f KB", toKilobytes(bytes));
        } else {
            return bytes + " B";
        }
    }
}

