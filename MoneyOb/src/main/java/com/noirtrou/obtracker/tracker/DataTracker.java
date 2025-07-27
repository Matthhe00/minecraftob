package com.noirtrou.obtracker.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    // === TRACKING MÉTIERS ===
    // Structure pour les métiers : nom du métier -> JobData
    public static class JobData {
        public String name;
        public int currentLevel;
        public long currentXP;
        public long maxXP; // XP nécessaire pour le niveau suivant
        public long xpRemaining; // XP restante pour le niveau suivant (directement depuis l'action bar)
        public double totalMoneyGain; // Gain d'argent total depuis le début de la session
        public long totalXPGain; // Gain d'XP total depuis le début de la session
        public long sessionStart; // Début de la session pour ce métier
        
        public JobData(String name, int level, long currentXP, long maxXP) {
            this.name = name;
            this.currentLevel = level;
            this.currentXP = currentXP;
            this.maxXP = maxXP;
            this.xpRemaining = maxXP - currentXP;
            this.totalMoneyGain = 0;
            this.totalXPGain = 0;
            this.sessionStart = System.currentTimeMillis();
        }
        
        public long getXPRemaining() {
            return xpRemaining;
        }
        
        public double getMoneyPerHour() {
            long sessionDuration = (System.currentTimeMillis() - sessionStart) / 1000;
            if (sessionDuration <= 0) return 0;
            double hours = sessionDuration / 3600.0;
            return hours > 0 ? totalMoneyGain / hours : 0;
        }
        
        public double getXPPerHour() {
            long sessionDuration = (System.currentTimeMillis() - sessionStart) / 1000;
            if (sessionDuration <= 0) return 0;
            double hours = sessionDuration / 3600.0;
            return hours > 0 ? totalXPGain / hours : 0;
        }
        
        public void updateXpRemaining(long newXpRemaining) {
            this.xpRemaining = newXpRemaining;
        }
    }
    
    private static final Map<String, JobData> jobs = new HashMap<>();
    private static long jobSessionStart = System.currentTimeMillis();
    private static long lastJobStatsCommand = 0;
    private static final long JOB_STATS_COMMAND_INTERVAL = 60000; // 60 secondes entre chaque commande /job stats

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

    // === MÉTHODES POUR LES MÉTIERS ===
    
    // Mettre à jour les informations d'un métier depuis /job stats
    public static void updateJobStats(String jobName, int level, long currentXP, long maxXP) {
        JobData job = jobs.get(jobName);
        if (job == null) {
            // Nouveau métier
            job = new JobData(jobName, level, currentXP, maxXP);
            jobs.put(jobName, job);
        } else {
            // Métier existant - mettre à jour les stats
            job.currentLevel = level;
            job.currentXP = currentXP;
            job.maxXP = maxXP;
        }
    }
    
    // Ajouter des gains d'XP et/ou d'argent pour un métier (depuis action bar)
    public static void addJobGain(String jobName, long xpGain, double moneyGain) {
        JobData job = jobs.get(jobName);
        if (job != null) {
            job.totalXPGain += xpGain;
            job.totalMoneyGain += moneyGain;
            job.currentXP += xpGain;
            
            // Vérifier si on a gagné un niveau
            while (job.currentXP >= job.maxXP && job.maxXP > 0) {
                job.currentLevel++;
                job.currentXP -= job.maxXP;
                // Note: maxXP devrait être mis à jour avec /job stats pour le nouveau niveau
            }
        }
    }
    
    // Nouvelle méthode pour ajouter des gains avec l'XP restante directement depuis l'action bar
    public static void addJobGainWithXpRemaining(String jobName, long xpGain, double moneyGain, long xpRemaining) {
        JobData job = jobs.get(jobName);
        if (job == null) {
            // Créer un nouveau métier si pas encore connu avec des valeurs par défaut
            job = new JobData(jobName, 1, 0, 1000);
            jobs.put(jobName, job);
        }
        
        // Accumuler les gains totaux
        job.totalXPGain += xpGain;
        job.totalMoneyGain += moneyGain;
        
        // Mettre à jour l'XP restante directement depuis l'action bar
        job.updateXpRemaining(xpRemaining);
        
        // Calculer le niveau actuel approximatif basé sur l'XP restante
        // Si nous avons des données d'XP restante, nous pouvons déduire l'XP actuelle
        if (xpRemaining > 0) {
            // Nous ne connaissons pas le maxXP exact du niveau actuel,
            // mais nous pouvons estimer en fonction des données disponibles
            job.xpRemaining = xpRemaining;
            
            // Debug pour voir les valeurs mises à jour
            System.out.println("[ObTracker] [JOB UPDATE] " + jobName + 
                             " - XP Remaining updated to: " + xpRemaining +
                             ", Total XP gained: " + job.totalXPGain +
                             ", Total Money gained: " + job.totalMoneyGain);
        }
    }
    
    // Obtenir la liste des métiers
    public static Map<String, JobData> getJobs() {
        return new HashMap<>(jobs);
    }
    
    // Obtenir la durée de la session métier
    public static long getJobSessionDuration() {
        if (jobs.isEmpty()) return 0;
        return (System.currentTimeMillis() - jobSessionStart) / 1000;
    }
    
    // Obtenir le total d'argent gagné par tous les métiers
    public static double getTotalJobMoney() {
        double total = 0.0;
        for (JobData job : jobs.values()) {
            total += job.totalMoneyGain;
        }
        return total;
    }
    
    // Obtenir le total d'XP gagné par tous les métiers
    public static long getTotalJobXP() {
        long total = 0;
        for (JobData job : jobs.values()) {
            total += job.totalXPGain;
        }
        return total;
    }
    
    // Vérifier s'il faut exécuter /job stats
    public static boolean shouldExecuteJobStatsCommand() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastJobStatsCommand >= JOB_STATS_COMMAND_INTERVAL) {
            lastJobStatsCommand = currentTime;
            return true;
        }
        return false;
    }
    
    // Réinitialiser la session métiers
    public static void clearJobSession() {
        jobs.clear();
        jobSessionStart = System.currentTimeMillis();
        lastJobStatsCommand = 0;
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
