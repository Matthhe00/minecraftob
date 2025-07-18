package com.noirtrou.obtracker.listeners;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
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
    
    // Variables pour tracker les titres précédents et le timestamp
    private static Text lastTitle = null;
    private static Text lastSubtitle = null;
    private static Text lastActionBar = null;
    private static String lastTitleText = null;
    private static long lastTitleTimestamp = 0L; // en ms
    
    public static void register() {
        System.out.println("[ObTracker] TitleListener enregistré avec événements HUD");
        
        // Créer le fichier de log
        createLogFile();
        logToFile("=== SESSION DÉMARRÉE À " + LocalDateTime.now().format(TIME_FORMATTER) + " ===");
        
        // Enregistrer le callback de rendu HUD pour capturer les titres
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.inGameHud != null) {
                checkForTitleChanges(client.inGameHud);
            }
        });
    }
    
    // Vérifier les changements de titre en utilisant la réflexion
    private static void checkForTitleChanges(net.minecraft.client.gui.hud.InGameHud inGameHud) {
        try {
            Field[] fields = inGameHud.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(inGameHud);
                if (value instanceof Text) {
                    Text textValue = (Text) value;
                    String textContent = textValue.getString();
                    long now = System.currentTimeMillis();
                    // Log et capture si le titre est nouveau OU si le même texte est ré-envoyé après un délai minimal
                    if (field.getName().toLowerCase().contains("title")) {
                        boolean isNewTitle = lastTitleText == null || !lastTitleText.equals(textContent);
                        boolean isTimeout = (now - lastTitleTimestamp) > 500; // 500ms entre deux détections identiques
                        if (isNewTitle || isTimeout) {
                            System.out.println("[ObTracker][EVENT] Titre détecté: " + textContent + (isNewTitle ? " (nouveau)" : " (répétition)"));
                            onTitleReceived(textValue);
                            lastTitleText = textContent;
                            lastTitleTimestamp = now;
                        }
                    } else if (field.getName().toLowerCase().contains("subtitle")) {
                        if (lastSubtitle == null || !lastSubtitle.getString().equals(textContent)) {
                            onSubtitleReceived(textValue);
                        }
                        lastSubtitle = textValue;
                    } else if (field.getName().toLowerCase().contains("overlay") || field.getName().toLowerCase().contains("actionbar")) {
                        if (lastActionBar == null || !lastActionBar.getString().equals(textContent)) {
                            onActionBarReceived(textValue);
                        }
                        lastActionBar = textValue;
                    }
                }
            }
        } catch (Exception e) {
            // Ignorer les erreurs de réflexion pour éviter le spam
        }
    }
    
    // Méthode pour logger toutes les détections
    public static void logDetection(String methodName, Text content) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String contentStr = content != null ? content.getString() : "null";
        
        String logEntry = String.format("[%s] DÉTECTION MÉTHODE: %s | Contenu: %s", 
                                      timestamp, methodName, contentStr);
        
        System.out.println("[ObTracker] " + logEntry);
        logToFile(logEntry);
    }
    
    // Méthodes appelées par les mixins
    public static void onTitleReceived(Text title) {
        if (title != null) {
            String titleText = title.getString();
            long now = System.currentTimeMillis();
            boolean isNewTitle = lastTitleText == null || !lastTitleText.equals(titleText);
            boolean isTimeout = (now - lastTitleTimestamp) > 500;
            // Compte si nouveau titre OU si le même texte est ré-envoyé après délai
            if (isNewTitle || isTimeout) {
                capturedTitles.add(titleText);
                limitHistory(100);
                String logEntry = String.format("[%s] TITRE CAPTURÉ: %s", LocalDateTime.now().format(TIME_FORMATTER), titleText);
                logToFile(logEntry);
                // Incrémenter le niveau d'île à chaque titre détecté
                com.noirtrou.obtracker.tracker.DataTracker.addIslandLevel(1);
                // Analyser automatiquement le titre
                analyzeTitle(titleText);
                lastTitleText = titleText;
                lastTitleTimestamp = now;
            }
        }
    }
    
    public static void onSubtitleReceived(Text subtitle) {
        if (subtitle != null) {
            String subtitleText = subtitle.getString();
            capturedSubtitles.add(subtitleText);
            limitHistory(100);
            
            String logEntry = String.format("[%s] SOUS-TITRE CAPTURÉ: %s", 
                                          LocalDateTime.now().format(TIME_FORMATTER), subtitleText);
            System.out.println("[ObTracker] " + logEntry);
            logToFile(logEntry);
            
            analyzeSubtitle(subtitleText);
        }
    }
    
    public static void onActionBarReceived(Text actionBar) {
        if (actionBar != null) {
            String actionBarText = actionBar.getString();
            capturedActionBars.add(actionBarText);
            limitHistory(100);
            
            String logEntry = String.format("[%s] ACTION BAR CAPTURÉE: %s", 
                                          LocalDateTime.now().format(TIME_FORMATTER), actionBarText);
            System.out.println("[ObTracker] " + logEntry);
            logToFile(logEntry);
            
            // Analyser automatiquement l'action bar
            analyzeActionBar(actionBarText);
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
        logToFile("Titres capturés: " + capturedTitles.size());
        logToFile("Sous-titres capturés: " + capturedSubtitles.size());
        logToFile("Action bars capturées: " + capturedActionBars.size());
        logToFile("=======================================");
    }
    
    public static int getTitleCount() {
        return capturedTitles.size();
    }
}
