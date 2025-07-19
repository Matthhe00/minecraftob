package com.noirtrou.obtracker.listeners;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TitleCaptureAlternative {
    private static final List<String> capturedMessages = new ArrayList<>();
    
    // Système de logging
    private static final String LOG_FILE_PATH = "run/title_capture_alternative.log";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    public static void register() {
        
        // Créer le fichier de log
        createLogFile();
        logToFile("=== SESSION ALTERNATIVE DÉMARRÉE À " + LocalDateTime.now().format(TIME_FORMATTER) + " ===");
        
        // Capturer tous les messages qui passent par le chat/overlay
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (overlay) {
                // C'est probablement un message overlay (action bar, titre, etc.)
                String messageText = message.getString();
                capturedMessages.add(messageText);
                
                String logEntry = String.format("[%s] MESSAGE OVERLAY CAPTURÉ: %s", 
                                              LocalDateTime.now().format(TIME_FORMATTER), messageText);
                logToFile(logEntry);
                
                // Analyser le message
                analyzeMessage(messageText);
            }
        });
        
        // Capturer aussi les messages normaux qui pourraient contenir des informations
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!overlay) {
                String messageText = message.getString();
                
                // Seulement logger si c'est intéressant
                if (isInterestingMessage(messageText)) {
                    String logEntry = String.format("[%s] MESSAGE INTÉRESSANT CAPTURÉ: %s", 
                                                  LocalDateTime.now().format(TIME_FORMATTER), messageText);
                    logToFile(logEntry);
                    
                    analyzeMessage(messageText);
                }
            }
        });
    }
    
    private static boolean isInterestingMessage(String message) {
        String lower = message.toLowerCase();
        return lower.contains("gagné") || 
               lower.contains("niveau") ||
               lower.contains("$") ||
               lower.contains("coin") ||
               lower.contains("argent") ||
               lower.contains("succès") ||
               lower.contains("achievement") ||
               lower.contains("title") ||
               lower.contains("showing new");
    }
    
    private static void analyzeMessage(String messageText) {
        List<String> types = new ArrayList<>();
        
        String lower = messageText.toLowerCase();
        
        if (lower.contains("showing new title")) {
            types.add("TITRE_SYSTÈME");
        }
        
        if (lower.contains("showing new subtitle")) {
            types.add("SOUS-TITRE_SYSTÈME");
        }
        
        if (lower.contains("showing new actionbar")) {
            types.add("ACTION_BAR_SYSTÈME");
        }
        
        if (lower.contains("niveau")) {
            types.add("NIVEAU");
        }
        
        if (lower.contains("gagné") || lower.contains("earn")) {
            types.add("GAIN");
        }
        
        if (lower.contains("$") || lower.contains("coin")) {
            types.add("ARGENT");
        }
        
        if (lower.contains("succès") || lower.contains("achievement")) {
            types.add("SUCCÈS");
        }
        
        if (!types.isEmpty()) {
            String typeLog = "  -> TYPES DÉTECTÉS: " + String.join(", ", types);
            logToFile(typeLog);
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
    
    // Méthodes pour récupérer les messages capturés
    public static List<String> getCapturedMessages() {
        return new ArrayList<>(capturedMessages);
    }
    
    public static void logStatistics() {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        logToFile("=== STATISTIQUES ALTERNATIVE [" + timestamp + "] ===");
        logToFile("Messages capturés: " + capturedMessages.size());
        logToFile("=======================================");
    }
    
    public static void clearHistory() {
        capturedMessages.clear();
        logToFile("HISTORIQUE ALTERNATIF NETTOYÉ");
    }
}
