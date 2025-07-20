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
    
    // Tracking des gains de vente (sell)
    private static final List<Double> sellGains = new ArrayList<>();
    private static double sessionSellGain = 0;
    private static long sellSessionStart = System.currentTimeMillis();
    
    // Tracking session Argent (pour affichage avec chrono et reset)
    private static long moneySessionStart = System.currentTimeMillis();
    
    // Solde total actuel
    private static double currentBalance = 0;
    
    // Protection contre les doublons
    private static String lastProcessedMessage = "";
    private static long lastMessageTime = 0;
    
    // Gestion automatique de la commande /bal
    private static long lastBalCommand = 0;
    private static final long BAL_COMMAND_INTERVAL = 30000; // 30 secondes entre chaque commande /bal

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
    
    // Méthodes pour les gains de vente (sell)
    public static void addSellGain(double gain) {
        sellGains.add(gain);
        sessionSellGain += gain;
        // Démarrer la session sell au premier événement si pas encore démarrée
        if (sellGains.size() == 1) {
            sellSessionStart = System.currentTimeMillis();
            // Démarrer aussi la session Argent au premier gain de vente
            moneySessionStart = System.currentTimeMillis();
        }
        lastUpdate = System.currentTimeMillis();
    }
    
    public static double getTotalSellGains() {
        double sum = 0.0;
        for (int i = 0; i < sellGains.size(); i++) {
            sum += sellGains.get(i);
        }
        return sum;
    }
    
    public static double getSellGainPerHour() {
        double hours = getSellSessionDuration() / 3600.0;
        return hours > 0 ? getTotalSellGains() / hours : 0;
    }
    
    public static double getSellGainPerMinute() {
        double minutes = getSellSessionDuration() / 60.0;
        return minutes > 0 ? getTotalSellGains() / minutes : 0;
    }
    
    public static long getSellSessionDuration() {
        if (sellGains.isEmpty()) return 0;
        return (System.currentTimeMillis() - sellSessionStart) / 1000;
    }
    
    // Méthodes pour la session Argent (affichage global avec chrono)
    public static long getMoneySessionDuration() {
        return (System.currentTimeMillis() - moneySessionStart) / 1000;
    }
    
    // Méthodes pour le solde total
    public static void setCurrentBalance(double balance) {
        currentBalance = balance;
    }
    
    public static double getCurrentBalance() {
        return currentBalance;
    }
    
    // Méthode pour obtenir le gain total (minion + sell)
    public static double getTotalGain() {
        return getTotalMinionGains() + getTotalSellGains();
    }
    
    // Protection contre les doublons
    public static boolean isMessageAlreadyProcessed(String message) {
        long currentTime = System.currentTimeMillis();
        
        // Si le même message a été traité dans les 500ms, on ignore
        if (message.equals(lastProcessedMessage) && (currentTime - lastMessageTime) < 500) {
            return true;
        }
        
        // Marquer le message comme traité
        lastProcessedMessage = message;
        lastMessageTime = currentTime;
        return false;
    }
    
    // Gestion automatique de la commande /bal
    public static boolean shouldExecuteBalCommand() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBalCommand >= BAL_COMMAND_INTERVAL) {
            lastBalCommand = currentTime;
            return true;
        }
        return false;
    }
    public static void addMinionGain(double gain) {
        minionGains.add(gain);
        sessionGain += gain;
        // Démarrer la session minion au premier événement si pas encore démarrée
        if (minionGains.size() == 1) {
            minionSessionStart = System.currentTimeMillis();
            // Démarrer aussi la session Argent au premier gain de minion
            moneySessionStart = System.currentTimeMillis();
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
        sellGains.clear();
        sessionSellGain = 0;
        sellSessionStart = System.currentTimeMillis();
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
    
    public static void clearSellHistory() {
        sellGains.clear();
        sessionSellGain = 0;
        sellSessionStart = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }
    
    // Reset spécifique pour la catégorie Argent : remet à zéro les ventes et redémarre le chrono
    public static void clearMoneySession() {
        // Reset uniquement les gains de vente (sell), pas les minions ni le solde total
        sellGains.clear();
        sessionSellGain = 0;
        sellSessionStart = System.currentTimeMillis();
        // Redémarrer le chrono de la session Argent
        moneySessionStart = System.currentTimeMillis();
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
