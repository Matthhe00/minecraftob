package com.noirtrou.obtracker.tracker;

import java.util.ArrayList;
import java.util.List;

public class DataTracker {
    // Tracking Niveau d'île avec session propre
    private static final List<Integer> islandLevels = new ArrayList<>();
    private static int sessionIslandLevel = 0;
    private static long islandSessionStart = System.currentTimeMillis();

    public static void addIslandLevel(int level) {
        islandLevels.add(level);
        sessionIslandLevel += level;
        // Démarrer la session île au premier événement si pas encore démarrée
        if (islandLevels.size() == 1) {
            islandSessionStart = System.currentTimeMillis();
        }
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
        double hours = getIslandSessionDuration() / 3600.0;
        return hours > 0 ? getTotalIslandLevel() / hours : 0;
    }
    public static double getIslandLevelPerMinute() {
        double minutes = getIslandSessionDuration() / 60.0;
        return minutes > 0 ? getTotalIslandLevel() / minutes : 0;
    }
    public static long getIslandSessionDuration() {
        if (islandLevels.isEmpty()) return 0;
        return (System.currentTimeMillis() - islandSessionStart) / 1000;
    }
    
    // Tracking Minions avec session propre
    private static double lastBalance = 0;
    private static final List<Double> minionGains = new ArrayList<>();
    private static final List<Integer> minionObjects = new ArrayList<>();
    private static long minionSessionStart = System.currentTimeMillis();
    private static long lastUpdate = System.currentTimeMillis();
    private static double sessionGain = 0;
    private static int sessionObjects = 0;

    public static double getLastBalance() { return lastBalance; }
    public static double getSessionGain() { return sessionGain; }
    public static long getMinionSessionDuration() {
        if (minionGains.isEmpty()) return 0;
        return (System.currentTimeMillis() - minionSessionStart) / 1000;
    }
    public static double getGainPerHour() {
        double hours = getMinionSessionDuration() / 3600.0;
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
        double hours = getMinionSessionDuration() / 3600.0;
        return hours > 0 ? getTotalMinionGains() / hours : 0;
    }
    public static double getObjectsPerHour() {
        double hours = getMinionSessionDuration() / 3600.0;
        return hours > 0 ? getTotalObjects() / hours : 0;
    }
    public static double getMinionGainPerMinute() {
        double minutes = getMinionSessionDuration() / 60.0;
        return minutes > 0 ? getTotalMinionGains() / minutes : 0;
    }
    public static double getObjectsPerMinute() {
        double minutes = getMinionSessionDuration() / 60.0;
        return minutes > 0 ? getTotalObjects() / minutes : 0;
    }
    public static void addMinionGain(double gain) {
        minionGains.add(gain);
        sessionGain += gain;
        // Démarrer la session minion au premier événement si pas encore démarrée
        if (minionGains.size() == 1) {
            minionSessionStart = System.currentTimeMillis();
        }
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
        islandSessionStart = System.currentTimeMillis();
        minionGains.clear();
        minionObjects.clear();
        sessionGain = 0;
        sessionObjects = 0;
        minionSessionStart = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }
    
    public static void clearIslandHistory() {
        islandLevels.clear();
        sessionIslandLevel = 0;
        islandSessionStart = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }
    
    public static void clearMinionHistory() {
        minionGains.clear();
        minionObjects.clear();
        sessionGain = 0;
        sessionObjects = 0;
        minionSessionStart = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }
    
    // Méthode pour s'assurer que tout démarre à 0
    public static void initialize() {
        // Les variables sont déjà initialisées à 0 par défaut, mais on peut forcer si nécessaire
        if (!islandLevels.isEmpty()) {
            islandLevels.clear();
        }
        if (sessionIslandLevel != 0) {
            sessionIslandLevel = 0;
        }
        if (!minionGains.isEmpty()) {
            minionGains.clear();
        }
        if (!minionObjects.isEmpty()) {
            minionObjects.clear();
        }
        if (sessionGain != 0) {
            sessionGain = 0;
        }
        if (sessionObjects != 0) {
            sessionObjects = 0;
        }
    }
}
