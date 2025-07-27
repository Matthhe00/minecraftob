package com.noirtrou.obtracker.listeners;

import com.noirtrou.obtracker.tracker.DataTracker;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ActionBarListener {
    
    // Pattern pour capturer les gains dans l'action bar
    // Format réel détecté : "+210.33实 ▪ +131.24夺 (308098/2939812 XP restantes)"
    // Support des nombres décimaux avec points et virgules
    private static final Pattern ACTION_BAR_GAIN_PATTERN = Pattern.compile("\\+([0-9,.]+)实\\s*▪\\s*\\+([0-9,.]+)夺\\s*\\(([0-9,./]+)\\s*XP\\s*restantes?\\)", Pattern.CASE_INSENSITIVE);
    
    public static void onActionBarUpdate(Text actionBarText) {
        if (actionBarText == null) return;
        
        String text = actionBarText.getString();
        if (text == null || text.trim().isEmpty()) return;
        
        // Nettoyer le texte des codes de couleur
        String cleanText = text.replaceAll("§[0-9a-fk-or]", "");
        
        // Debug : afficher TOUS les messages d'action bar pour vérifier la capture
        System.out.println("[ObTracker] [ACTION BAR DETECTED] " + cleanText);
        
        // Debug spécifique pour les messages de gains
        if (cleanText.contains("实") && cleanText.contains("夺")) {
            System.out.println("[ObTracker] [GAIN MESSAGE DETECTED] Testing pattern match...");
            System.out.println("[ObTracker] [DEBUG] Full message: '" + cleanText + "'");
            System.out.println("[ObTracker] [DEBUG] Pattern: \\+([0-9,.]+)实\\s*▪\\s*\\+([0-9,.]+)夺\\s*\\(([0-9,./]+)\\s*XP\\s*restantes?\\)");
        }
        
        // Chercher les gains dans l'action bar
        Matcher gainMatcher = ACTION_BAR_GAIN_PATTERN.matcher(cleanText);
        if (gainMatcher.find()) {
            System.out.println("[ObTracker] [PATTERN MATCHED] Successfully matched gain pattern!");
            try {
                // Extraire les valeurs
                String moneyStr = gainMatcher.group(1).replace(",", "");
                String xpStr = gainMatcher.group(2).replace(",", "");
                String xpRemainingStr = gainMatcher.group(3);
                
                System.out.println("[ObTracker] [PARSING] Money string: '" + moneyStr + "', XP string: '" + xpStr + "', XP remaining string: '" + xpRemainingStr + "'");
                
                double moneyGain = Double.parseDouble(moneyStr);
                double xpGainDouble = Double.parseDouble(xpStr);
                long xpGain = Math.round(xpGainDouble); // Convertir en long en arrondissant
                
                // Parser l'XP restante (format: "14689/2939812" ou juste "14689")
                long xpRemaining = parseXpRemaining(xpRemainingStr);
                
                // Identifier le métier en fonction de l'item en main
                String jobName = identifyJobFromItem();
                
                System.out.println("[ObTracker] [JOB GAIN] Job: " + jobName + 
                                 ", Money: " + moneyGain + 
                                 ", XP: " + xpGain + 
                                 ", XP Remaining: " + xpRemaining);
                
                if (jobName != null) {
                    // Ajouter les gains au tracker avec l'XP restante
                    DataTracker.addJobGainWithXpRemaining(jobName, xpGain, moneyGain, xpRemaining);
                    System.out.println("[ObTracker] [JOB TRACKED] " + jobName + " gains added to tracker");
                }
                
            } catch (NumberFormatException e) {
                System.err.println("[ObTracker] [ERROR] Failed to parse action bar numbers: " + e.getMessage());
            }
        }
    }
    
    private static long parseXpRemaining(String xpRemainingStr) {
        try {
            // Format détecté : "307033/2939812" où 307033 = XP actuelle, 2939812 = XP totale pour le niveau suivant
            if (xpRemainingStr.contains("/")) {
                String[] parts = xpRemainingStr.split("/");
                if (parts.length == 2) {
                    long current = Long.parseLong(parts[0].replace(",", ""));
                    long max = Long.parseLong(parts[1].replace(",", ""));
                    long remaining = max - current; // XP restante = max - actuelle
                    
                    System.out.println("[ObTracker] [XP PARSING] Current: " + current + 
                                     ", Max: " + max + 
                                     ", Remaining: " + remaining);
                    
                    return remaining;
                }
            } else {
                // Si c'est juste un nombre, c'est l'XP restante directement
                long remaining = Long.parseLong(xpRemainingStr.replace(",", ""));
                System.out.println("[ObTracker] [XP PARSING] Direct remaining: " + remaining);
                return remaining;
            }
        } catch (NumberFormatException e) {
            System.err.println("[ObTracker] [ERROR] Failed to parse XP remaining: " + e.getMessage());
        }
        return 0;
    }
    
    private static String identifyJobFromItem() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return null;
        
        ItemStack mainHandItem = client.player.getStackInHand(Hand.MAIN_HAND);
        if (mainHandItem.isEmpty()) return null;
        
        String itemName = mainHandItem.getItem().toString().toLowerCase();
        String displayName = mainHandItem.getName().getString().toLowerCase();
        
        // Debug : afficher l'item en main
        System.out.println("[ObTracker] [ITEM] " + itemName + " | Display: " + displayName);
        
        // Identifier le métier basé sur l'item selon les spécifications
        
        // Fermier : fourche / graine
        if (itemName.contains("hoe") || displayName.contains("fourche") || 
            itemName.contains("seeds") || displayName.contains("graine") ||
            displayName.contains("semence")) {
            return "Fermier";
        }
        
        // Bûcheron : hache / houe en netherite
        if (itemName.contains("axe") || displayName.contains("hache") ||
            (itemName.contains("netherite") && itemName.contains("hoe")) ||
            displayName.contains("hache")) {
            return "Bûcheron";
        }
        
        // Mineur : pioche / multitools
        if (itemName.contains("pickaxe") || displayName.contains("pioche") ||
            displayName.contains("multitools") || displayName.contains("multitool") ||
            displayName.contains("multi-tool")) {
            return "Mineur";
        }
        
        // Pêcheur : canne
        if (itemName.contains("fishing_rod") || displayName.contains("canne") ||
            displayName.contains("canne à pêche")) {
            return "Pêcheur";
        }
        
        // Chasseur : épée / machette
        if (itemName.contains("sword") || displayName.contains("épée") ||
            displayName.contains("machette") || displayName.contains("épee")) {
            return "Chasseur";
        }
        
        return null; // Métier non identifié
    }
}
