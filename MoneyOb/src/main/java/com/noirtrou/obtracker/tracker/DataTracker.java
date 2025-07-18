package com.noirtrou.obtracker.tracker;

import java.util.ArrayList;
import java.util.List;

public class DataTracker {
    // Tracking Niveau d'île
    private static final List<Integer> islandLevels = new ArrayList<>();
    private static int sessionIslandLevel = 0;

    public static void addIslandLevel(int level) {
        islandLevels.add(level);
        sessionIslandLevel += level;
        lastUpdate = System.currentTimeMillis();
    }
    public static int getTotalIslandLevel() {
        int sum = 0;
        for (int i = 0; i < islandLevels.size(); i++) {
            sum += islandLevels.get(i);
        }
        return sum;
    }
    public static double getAverageIslandLevel() {
        int size = islandLevels.size();
        if (size == 0) return 0.0;
        return getTotalIslandLevel() / (double)size;
    }
    public static double getIslandLevelPerHour() {
        double hours = getSessionDuration() / 3600.0;
        return hours > 0 ? getTotalIslandLevel() / hours : 0;
    }
    public static double getIslandLevelPerMinute() {
        double minutes = getSessionDuration() / 60.0;
        return minutes > 0 ? getTotalIslandLevel() / minutes : 0;
    }
    private static double lastBalance = 0;
    private static final List<Double> minionGains = new ArrayList<>();
    private static final List<Integer> minionObjects = new ArrayList<>();
    private static long sessionStart = System.currentTimeMillis();
    private static long lastUpdate = System.currentTimeMillis();
    private static double sessionGain = 0;
    private static int sessionObjects = 0;
    private static long globalSessionStart = System.currentTimeMillis();

    public static double getLastBalance() { return lastBalance; }
    public static double getSessionGain() { return sessionGain; }
    public static long getSessionDuration() { return (System.currentTimeMillis() - sessionStart) / 1000; }
    public static long getGlobalSessionDuration() {
        return (System.currentTimeMillis() - globalSessionStart) / 1000;
    }
    public static double getGainPerHour() {
        double hours = getSessionDuration() / 3600.0;
        return hours > 0 ? sessionGain / hours : 0;
    }
    public static double getAverageGain() {
        int size = minionGains.size();
        if (size == 0) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < size; i++) {
            sum += minionGains.get(i);
        }
        return sum / size;
    }
    public static double getMedianGain() {
        int size = minionGains.size();
        if (size == 0) return 0.0;
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) arr[i] = minionGains.get(i);
        java.util.Arrays.sort(arr);
        if (size % 2 == 0) {
            return (arr[size/2 - 1] + arr[size/2]) / 2.0;
        } else {
            return arr[size/2];
        }
    }
    public static long getTimeToNextUpdate() {
        long elapsed = (System.currentTimeMillis() - lastUpdate) / 1000;
        return Math.max(0, 120 - elapsed);
    }
    public static double getTotalMinionGains() {
        double sum = 0.0;
        for (int i = 0; i < minionGains.size(); i++) {
            sum += minionGains.get(i);
        }
        return sum;
    }
    public static int getTotalObjects() {
        int sum = 0;
        for (int i = 0; i < minionObjects.size(); i++) {
            sum += minionObjects.get(i);
        }
        return sum;
    }
    public static double getAverageObjectsPerItem() {
        int size = minionObjects.size();
        return size > 0 ? getTotalObjects() / (double)size : 0;
    }
    // Ajout des méthodes pour OverlayRenderer
    public static double getMinionGainPerHour() {
        double hours = getSessionDuration() / 3600.0;
        return hours > 0 ? getTotalMinionGains() / hours : 0;
    }
    public static double getObjectsPerHour() {
        double hours = getSessionDuration() / 3600.0;
        return hours > 0 ? getTotalObjects() / hours : 0;
    }
    public static double getMinionGainPerMinute() {
        double minutes = getSessionDuration() / 60.0;
        return minutes > 0 ? getTotalMinionGains() / minutes : 0;
    }
    public static double getObjectsPerMinute() {
        double minutes = getSessionDuration() / 60.0;
        return minutes > 0 ? getTotalObjects() / minutes : 0;
    }
    public static void addMinionGain(double gain) {
        minionGains.add(gain);
        sessionGain += gain;
        lastUpdate = System.currentTimeMillis();
    }
    public static void addMinionObject(int obj) {
        minionObjects.add(obj);
        sessionObjects += obj;
        lastUpdate = System.currentTimeMillis();
    }
    public static void clearHistory() {
        islandLevels.clear();
        sessionIslandLevel = 0;
        minionGains.clear();
        minionObjects.clear();
        sessionGain = 0;
        sessionObjects = 0;
        sessionStart = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }
}
