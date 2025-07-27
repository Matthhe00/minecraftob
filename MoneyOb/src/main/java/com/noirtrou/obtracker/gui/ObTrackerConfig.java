package com.noirtrou.obtracker.gui;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ObTrackerConfig {
    public static boolean minionVisible = true;
    public static boolean islandLevelVisible = true;
    public static boolean itemInHandVisible = true;
    public static boolean moneyVisible = true;
    public static boolean jobVisible = true; // Nouvelle catégorie métier
    public static boolean filterMinionGainMessages = true;
    public static boolean filterBalMessages = true;
    public static boolean filterJobStatsMessages = true; // Filtre pour /job stats
    public static boolean autoBalCommand = true; // Exécution automatique de /bal
    public static float hudScale = 1.0f; // Échelle globale de l'overlay HUD (0.5f à 2.0f)
    public static float globalScale = 1.0f; // Échelle globale contrôlant toutes les catégories
    
    // Échelles individuelles pour chaque catégorie
    public static float moneyScale = 1.0f; // Échelle de la section Argent
    public static float minionScale = 1.0f; // Échelle de la section Minion
    public static float islandScale = 1.0f; // Échelle de la section Niveau d'île
    public static float itemScale = 1.0f; // Échelle de la section Item en main
    public static float jobScale = 1.0f; // Échelle de la section Métier
    
    private static final String CONFIG_FILE_PATH = "config/obtracker-config.properties";
    
    // Sauvegarder la configuration dans un fichier
    public static void saveConfig() {
        try {
            Properties properties = new Properties();
            properties.setProperty("minionVisible", String.valueOf(minionVisible));
            properties.setProperty("islandLevelVisible", String.valueOf(islandLevelVisible));
            properties.setProperty("itemInHandVisible", String.valueOf(itemInHandVisible));
            properties.setProperty("moneyVisible", String.valueOf(moneyVisible));
            properties.setProperty("jobVisible", String.valueOf(jobVisible));
            properties.setProperty("filterMinionGainMessages", String.valueOf(filterMinionGainMessages));
            properties.setProperty("filterBalMessages", String.valueOf(filterBalMessages));
            properties.setProperty("filterJobStatsMessages", String.valueOf(filterJobStatsMessages));
            properties.setProperty("autoBalCommand", String.valueOf(autoBalCommand));
            properties.setProperty("hudScale", String.valueOf(hudScale));
            properties.setProperty("globalScale", String.valueOf(globalScale));
            properties.setProperty("moneyScale", String.valueOf(moneyScale));
            properties.setProperty("minionScale", String.valueOf(minionScale));
            properties.setProperty("islandScale", String.valueOf(islandScale));
            properties.setProperty("itemScale", String.valueOf(itemScale));
            properties.setProperty("jobScale", String.valueOf(jobScale));
            
            // Créer le dossier config s'il n'existe pas
            Path configPath = Paths.get(CONFIG_FILE_PATH);
            Files.createDirectories(configPath.getParent());
            
            // Sauvegarder le fichier
            try (FileOutputStream out = new FileOutputStream(CONFIG_FILE_PATH)) {
                properties.store(out, "ObTracker Configuration");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de la configuration ObTracker: " + e.getMessage());
        }
    }
    
    // Charger la configuration depuis un fichier
    public static void loadConfig() {
        try {
            Path configPath = Paths.get(CONFIG_FILE_PATH);
            if (!Files.exists(configPath)) {
                // Créer un fichier de configuration par défaut
                saveConfig();
                return;
            }
            
            Properties properties = new Properties();
            try (FileInputStream in = new FileInputStream(CONFIG_FILE_PATH)) {
                properties.load(in);
            }
            
            // Charger les valeurs (avec valeurs par défaut en cas d'erreur)
            minionVisible = Boolean.parseBoolean(properties.getProperty("minionVisible", "true"));
            islandLevelVisible = Boolean.parseBoolean(properties.getProperty("islandLevelVisible", "true"));
            itemInHandVisible = Boolean.parseBoolean(properties.getProperty("itemInHandVisible", "true"));
            moneyVisible = Boolean.parseBoolean(properties.getProperty("moneyVisible", "true"));
            jobVisible = Boolean.parseBoolean(properties.getProperty("jobVisible", "true"));
            filterMinionGainMessages = Boolean.parseBoolean(properties.getProperty("filterMinionGainMessages", "true"));
            filterBalMessages = Boolean.parseBoolean(properties.getProperty("filterBalMessages", "true"));
            filterJobStatsMessages = Boolean.parseBoolean(properties.getProperty("filterJobStatsMessages", "true"));
            autoBalCommand = Boolean.parseBoolean(properties.getProperty("autoBalCommand", "true"));
            
            try {
                hudScale = Float.parseFloat(properties.getProperty("hudScale", "1.0"));
                globalScale = Float.parseFloat(properties.getProperty("globalScale", "1.0"));
                moneyScale = Float.parseFloat(properties.getProperty("moneyScale", "1.0"));
                minionScale = Float.parseFloat(properties.getProperty("minionScale", "1.0"));
                islandScale = Float.parseFloat(properties.getProperty("islandScale", "1.0"));
                itemScale = Float.parseFloat(properties.getProperty("itemScale", "1.0"));
                jobScale = Float.parseFloat(properties.getProperty("jobScale", "1.0"));
                
                // Vérifier que les valeurs sont dans la plage correcte (0.5f à 2.0f)
                globalScale = Math.max(0.5f, Math.min(2.0f, globalScale));
                hudScale = Math.max(0.5f, Math.min(2.0f, hudScale));
                moneyScale = Math.max(0.5f, Math.min(2.0f, moneyScale));
                minionScale = Math.max(0.5f, Math.min(2.0f, minionScale));
                islandScale = Math.max(0.5f, Math.min(2.0f, islandScale));
                itemScale = Math.max(0.5f, Math.min(2.0f, itemScale));
                jobScale = Math.max(0.5f, Math.min(2.0f, jobScale));
            } catch (NumberFormatException e) {
                System.err.println("Erreur lors du parsing des valeurs de scale, utilisation des valeurs par défaut");
            }
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la configuration ObTracker: " + e.getMessage());
            // En cas d'erreur, créer un nouveau fichier avec les valeurs par défaut
            saveConfig();
        }
    }
}