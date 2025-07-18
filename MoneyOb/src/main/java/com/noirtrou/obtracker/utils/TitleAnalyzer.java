package com.noirtrou.obtracker.utils;

import com.noirtrou.obtracker.listeners.TitleListener;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TitleAnalyzer {
    
    // Exemples de patterns pour analyser différents types de titres
    private static final Pattern LEVEL_UP_PATTERN = Pattern.compile("Niveau (\\d+)");
    private static final Pattern MONEY_PATTERN = Pattern.compile("([\\d.]+).*coins?");
    private static final Pattern ACHIEVEMENT_PATTERN = Pattern.compile("(?i)(achievement|succès|réussite)");
    
    /**
     * Analyse le dernier titre reçu pour extraire des informations utiles
     */
    public static void analyzeLastTitle() {
        String lastTitle = TitleListener.getLastTitle();
        if (lastTitle != null) {
            System.out.println("[TitleAnalyzer] Analyse du titre: " + lastTitle);
            
            // Vérifier s'il s'agit d'un level up
            Matcher levelMatcher = LEVEL_UP_PATTERN.matcher(lastTitle);
            if (levelMatcher.find()) {
                int level = Integer.parseInt(levelMatcher.group(1));
                System.out.println("[TitleAnalyzer] Level up détecté: Niveau " + level);
                // Ici vous pouvez ajouter la logique pour traquer les niveaux
            }
            
            // Vérifier s'il y a des informations sur l'argent
            Matcher moneyMatcher = MONEY_PATTERN.matcher(lastTitle);
            if (moneyMatcher.find()) {
                String amount = moneyMatcher.group(1);
                System.out.println("[TitleAnalyzer] Montant détecté: " + amount);
            }
            
            // Vérifier s'il s'agit d'un achievement
            Matcher achievementMatcher = ACHIEVEMENT_PATTERN.matcher(lastTitle);
            if (achievementMatcher.find()) {
                System.out.println("[TitleAnalyzer] Achievement détecté: " + lastTitle);
            }
        }
    }
    
    /**
     * Analyse tous les titres capturés pour des statistiques
     */
    public static void analyzeAllTitles() {
        List<String> titles = TitleListener.getCapturedTitles();
        System.out.println("[TitleAnalyzer] Analyse de " + titles.size() + " titres capturés");
        
        int levelUps = 0;
        int achievements = 0;
        
        for (String title : titles) {
            if (LEVEL_UP_PATTERN.matcher(title).find()) {
                levelUps++;
            }
            if (ACHIEVEMENT_PATTERN.matcher(title).find()) {
                achievements++;
            }
        }
        
        System.out.println("[TitleAnalyzer] Statistiques:");
        System.out.println("  - Level ups: " + levelUps);
        System.out.println("  - Achievements: " + achievements);
    }
    
    /**
     * Recherche des titres contenant un texte spécifique
     */
    public static List<String> searchTitles(String searchText) {
        return TitleListener.getCapturedTitles()
            .stream()
            .filter(title -> title.toLowerCase().contains(searchText.toLowerCase()))
            .toList();
    }
    
    /**
     * Affiche un résumé des derniers titres
     */
    public static void printRecentTitles(int count) {
        List<String> titles = TitleListener.getCapturedTitles();
        int start = Math.max(0, titles.size() - count);
        
        System.out.println("[TitleAnalyzer] Derniers " + count + " titres:");
        for (int i = start; i < titles.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + titles.get(i));
        }
    }
    
    /**
     * Analyse spécifique des action bars capturées
     */
    public static void analyzeActionBars() {
        List<String> actionBars = TitleListener.getCapturedActionBars();
        System.out.println("[TitleAnalyzer] Analyse de " + actionBars.size() + " action bars capturées");
        
        int moneyBars = 0;
        int coordinateBars = 0;
        int progressBars = 0;
        
        for (String actionBar : actionBars) {
            if (actionBar.toLowerCase().contains("$") || 
                actionBar.toLowerCase().contains("coin") ||
                actionBar.toLowerCase().contains("money")) {
                moneyBars++;
            }
            
            if (actionBar.contains("X:") || actionBar.contains("Y:") || actionBar.contains("Z:")) {
                coordinateBars++;
            }
            
            if (actionBar.contains("❘") || actionBar.contains("|") || actionBar.contains("█")) {
                progressBars++;
            }
        }
        
        System.out.println("[TitleAnalyzer] Statistiques Action Bar:");
        System.out.println("  - Action bars avec argent: " + moneyBars);
        System.out.println("  - Action bars avec coordonnées: " + coordinateBars);
        System.out.println("  - Action bars avec barres de progression: " + progressBars);
    }
    
    /**
     * Recherche dans les action bars capturées
     */
    public static List<String> searchActionBars(String searchText) {
        return TitleListener.getCapturedActionBars()
            .stream()
            .filter(actionBar -> actionBar.toLowerCase().contains(searchText.toLowerCase()))
            .toList();
    }
    
    /**
     * Affiche les dernières action bars
     */
    public static void printRecentActionBars(int count) {
        List<String> actionBars = TitleListener.getCapturedActionBars();
        int start = Math.max(0, actionBars.size() - count);
        
        System.out.println("[TitleAnalyzer] Dernières " + count + " action bars:");
        for (int i = start; i < actionBars.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + actionBars.get(i));
        }
    }
    
    /**
     * Analyse complète de tous les éléments capturés
     */
    public static void analyzeAllCapturedContent() {
        System.out.println("[TitleAnalyzer] === ANALYSE COMPLÈTE ===");
        analyzeAllTitles();
        analyzeActionBars();
        
        System.out.println("[TitleAnalyzer] === RÉSUMÉ ===");
        System.out.println("  - Titres capturés: " + TitleListener.getCapturedTitles().size());
        System.out.println("  - Sous-titres capturés: " + TitleListener.getCapturedSubtitles().size());
        System.out.println("  - Action bars capturées: " + TitleListener.getCapturedActionBars().size());
    }
}
