package com.noirtrou.obtracker.utils;

public class MathUtils {
    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    // Format abrégé : 1,2K, 3,4M, etc.
    public static String formatNumberShort(double value) {
        if (value >= 1_000_000_000) {
            return String.format("%.2fG", value / 1_000_000_000).replace('.', ',');
        } else if (value >= 1_000_000) {
            return String.format("%.2fM", value / 1_000_000).replace('.', ',');
        } else if (value >= 1_000) {
            return String.format("%.2fK", value / 1_000).replace('.', ',');
        } else {
            return String.format("%.0f", value);
        }
    }
}
