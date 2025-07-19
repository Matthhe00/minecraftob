package com.noirtrou.obtracker.listeners;

import net.minecraft.text.Text;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TitleListener {
    private static final List<String> capturedTitles = new ArrayList<>();
    private static final List<String> capturedSubtitles = new ArrayList<>();
    private static final List<String> capturedActionBars = new ArrayList<>();
    
    // Système de logging
    private static final String LOG_FILE_PATH = "run/title_capture.log";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // Variables pour l'écoute instantanée des événements MIXIN uniquement
    private static long titleCounter = 0L;
    private static long subtitleCounter = 0L;
    private static long actionBarCounter = 0L;
    
    public static void register() {
        // Initialiser le DataTracker pour s'assurer que tout part à 0
        com.noirtrou.obtracker.tracker.DataTracker.initialize();
        
        // Créer le fichier de log
        createLogFile();
        logToFile("=== SESSION DÉMARRÉE À " + LocalDateTime.now().format(TIME_FORMATTER) + " ===");
        logToFile("MODE: Capture par mixins (interception directe des paquets/méthodes HUD)");
        
        // Plus besoin d'enregistrer un callback HUD - les mixins s'en occupent
        // Les événements arrivent directement via onTitleReceived(), onSubtitleReceived(), onActionBarReceived()
    }
    
    // Méthode pour logger toutes les détections
    public static void logDetection(String methodName, Text content) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String contentStr = content != null ? content.getString() : "null";
        
        String logEntry = String.format("[%s] DÉTECTION MÉTHODE: %s | Contenu: %s", 
                                      timestamp, methodName, contentStr);
        
        logToFile(logEntry);
    }
    
    // Suppression de la déduplication - traiter TOUS les événements
    private static boolean shouldProcessTitle(String titleText) {
        // Toujours traiter chaque événement, même si identique
        return true;
    }
    
    private static boolean shouldProcessSubtitle(String subtitleText) {
        // Toujours traiter chaque événement, même si identique
        return true;
    }
    
    private static boolean shouldProcessActionBar(String actionBarText) {
        // Toujours traiter chaque événement, même si identique
        return true;
    }
    
    // Méthodes pour mixins (utilisées par TitleCaptureMixin)
    public static void onTitleReceived(Text title) {
        if (title != null) {
            String titleText = title.getString();
            
            // Traiter chaque événement sans déduplication
            if (shouldProcessTitle(titleText)) {
                titleCounter++;
                
                capturedTitles.add(titleText);
                limitHistory(100);
                
                String logEntry = String.format("[%s] TITRE MIXIN #%d: %s", 
                    LocalDateTime.now().format(TIME_FORMATTER), titleCounter, titleText);
                logToFile(logEntry);
                
                // Optimisation: ne compter que les titres contenant le caractère 隔
                if (titleText.contains("隔")) {
                    // Ajouter au compteur d'île seulement pour les titres avec 隔
                    com.noirtrou.obtracker.tracker.DataTracker.addIslandLevel(1);
                    logToFile("  -> TITRE AVEC 隔 DÉTECTÉ - Ajouté au compteur d'île");
                } else {
                    logToFile("  -> TITRE SANS 隔 - Non compté dans les statistiques d'île");
                }
                
                analyzeTitle(titleText);
            }
        }
    }

    public static void onSubtitleReceived(Text subtitle) {
        if (subtitle != null) {
            String subtitleText = subtitle.getString();
            
            if (shouldProcessSubtitle(subtitleText)) {
                subtitleCounter++;
                
                capturedSubtitles.add(subtitleText);
                limitHistory(100);
                
                String logEntry = String.format("[%s] SOUS-TITRE MIXIN #%d: %s", 
                                              LocalDateTime.now().format(TIME_FORMATTER), subtitleCounter, subtitleText);
                logToFile(logEntry);
                analyzeSubtitle(subtitleText);
            }
        }
    }

    public static void onActionBarReceived(Text actionBar) {
        if (actionBar != null) {
            String actionBarText = actionBar.getString();
            
            if (shouldProcessActionBar(actionBarText)) {
                actionBarCounter++;
                
                capturedActionBars.add(actionBarText);
                limitHistory(100);
                
                String logEntry = String.format("[%s] ACTION BAR MIXIN #%d: %s", 
                                              LocalDateTime.now().format(TIME_FORMATTER), actionBarCounter, actionBarText);
                logToFile(logEntry);
                analyzeActionBar(actionBarText);
            }
        }
    }
    
    // Créer le fichier de log
    private static void createLogFile() {
        try {
            Path logPath = Paths.get(LOG_FILE_PATH);
            Files.createDirectories(logPath.getParent());
            
            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
            }
        } catch (IOException e) {
            System.err.println("[ObTracker] Erreur lors de la création du fichier de log: " + e.getMessage());
        }
    }
    
    // Écrire dans le fichier de log
    private static void logToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(message + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            System.err.println("[ObTracker] Erreur lors de l'écriture dans le log: " + e.getMessage());
        }
    }
    
    // Méthode pour analyser automatiquement un titre
    private static void analyzeTitle(String titleText) {
        List<String> types = new ArrayList<>();
        
        // Détection prioritaire du caractère 隔 pour l'optimisation
        if (titleText.contains("隔")) {
            types.add("NIVEAU_ÎLE_隔");
        }
        
        if (titleText.toLowerCase().contains("niveau")) {
            types.add("NIVEAU");
        }
        
        if (titleText.toLowerCase().contains("gagné") || titleText.toLowerCase().contains("earn")) {
            types.add("GAIN");
        }
        
        if (titleText.toLowerCase().contains("succès") || titleText.toLowerCase().contains("achievement")) {
            types.add("SUCCÈS");
        }
        
        if (titleText.toLowerCase().contains("$") || titleText.toLowerCase().contains("coin")) {
            types.add("ARGENT");
        }
        
        if (titleText.toLowerCase().contains("event") || titleText.toLowerCase().contains("événement")) {
            types.add("ÉVÉNEMENT");
        }
        
        if (!types.isEmpty()) {
            String typeLog = "  -> TYPES DÉTECTÉS: " + String.join(", ", types);
            logToFile(typeLog);
        }
    }
    
    // Méthode pour analyser automatiquement un sous-titre
    private static void analyzeSubtitle(String subtitleText) {
        List<String> types = new ArrayList<>();
        
        if (subtitleText.toLowerCase().contains("félicitation") || subtitleText.toLowerCase().contains("bravo")) {
            types.add("FÉLICITATION");
        }
        
        if (subtitleText.toLowerCase().contains("niveau")) {
            types.add("NIVEAU");
        }
        
        if (!types.isEmpty()) {
            String typeLog = "  -> TYPES DÉTECTÉS: " + String.join(", ", types);
            logToFile(typeLog);
        }
    }
    
    // Méthode pour analyser automatiquement une action bar
    private static void analyzeActionBar(String actionBarText) {
        List<String> types = new ArrayList<>();
        
        if (actionBarText.toLowerCase().contains("$") || 
            actionBarText.toLowerCase().contains("coin") ||
            actionBarText.toLowerCase().contains("money") ||
            actionBarText.toLowerCase().contains("argent")) {
            types.add("FINANCIER");
        }
        
        if (actionBarText.contains("X:") || actionBarText.contains("Y:") || actionBarText.contains("Z:")) {
            types.add("COORDONNÉES");
        }
        
        if (actionBarText.contains("❘") || actionBarText.contains("|") || actionBarText.contains("█")) {
            types.add("BARRE_PROGRESSION");
        }
        
        if (actionBarText.contains("%")) {
            types.add("POURCENTAGE");
        }
        
        if (actionBarText.toLowerCase().contains("hp") || actionBarText.toLowerCase().contains("vie")) {
            types.add("VIE");
        }
        
        if (actionBarText.toLowerCase().contains("mana") || actionBarText.toLowerCase().contains("mp")) {
            types.add("MANA");
        }
        
        if (!types.isEmpty()) {
            String typeLog = "  -> TYPES DÉTECTÉS: " + String.join(", ", types);
            logToFile(typeLog);
        }
    }
    
    // Méthodes pour récupérer les titres capturés
    public static List<String> getCapturedTitles() {
        return new ArrayList<>(capturedTitles);
    }
    
    public static List<String> getCapturedSubtitles() {
        return new ArrayList<>(capturedSubtitles);
    }
    
    public static List<String> getCapturedActionBars() {
        return new ArrayList<>(capturedActionBars);
    }
    
    public static String getLastTitle() {
        return capturedTitles.isEmpty() ? null : capturedTitles.get(capturedTitles.size() - 1);
    }
    
    public static String getLastSubtitle() {
        return capturedSubtitles.isEmpty() ? null : capturedSubtitles.get(capturedSubtitles.size() - 1);
    }
    
    public static String getLastActionBar() {
        return capturedActionBars.isEmpty() ? null : capturedActionBars.get(capturedActionBars.size() - 1);
    }
    
    // Méthode pour nettoyer l'historique
    public static void clearHistory() {
        capturedTitles.clear();
        capturedSubtitles.clear();
        capturedActionBars.clear();
        logToFile("HISTORIQUE NETTOYÉ");
    }
    
    // Méthode pour limiter la taille de l'historique
    private static void limitHistory(int maxSize) {
        while (capturedTitles.size() > maxSize) {
            capturedTitles.remove(0);
        }
        while (capturedSubtitles.size() > maxSize) {
            capturedSubtitles.remove(0);
        }
        while (capturedActionBars.size() > maxSize) {
            capturedActionBars.remove(0);
        }
    }
     // Méthode pour obtenir des statistiques détaillées
    public static void logStatistics() {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        logToFile("=== STATISTIQUES [" + timestamp + "] ===");
        logToFile("Titres capturés: " + capturedTitles.size() + " (Compteur total: " + titleCounter + ")");
        logToFile("Sous-titres capturés: " + capturedSubtitles.size() + " (Compteur total: " + subtitleCounter + ")");
        logToFile("Action bars capturées: " + capturedActionBars.size() + " (Compteur total: " + actionBarCounter + ")");
        logToFile("=======================================");
    }

    public static int getTitleCount() {
        return capturedTitles.size();
    }

    // Nouvelles méthodes pour accéder aux compteurs d'écoute instantanée
    public static long getTotalTitleCounter() {
        return titleCounter;
    }

    public static long getTotalSubtitleCounter() {
        return subtitleCounter;
    }

    public static long getTotalActionBarCounter() {
        return actionBarCounter;
    }

    // Méthode pour remettre à zéro les compteurs
    public static void resetCounters() {
        titleCounter = 0;
        subtitleCounter = 0;
        actionBarCounter = 0;
        logToFile("COMPTEURS REMIS À ZÉRO");
    }
}
