package com.noirtrou.obtracker.listeners;

import com.noirtrou.obtracker.tracker.DataTracker;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ChatListener {
    
    // Pattern pour parser les informations de /job stats avec le nouveau format
    // Format: "Fermier - Niveau: 88 - [|||||||||||||||||||||||||||| 53%]"
    private static final Pattern JOB_STATS_PATTERN = Pattern.compile("([^-]+)\\s*-\\s*Niveau:\\s*(\\d+)\\s*-\\s*\\[.*?\\s*(\\d+)%\\]", Pattern.CASE_INSENSITIVE);
    
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String msg = message.getString();
            
            // Parser les informations de /job stats
            parseJobStats(msg);
            
            // Ce listener peut être utilisé pour d'autres types de messages
            // Les gains de minions, ventes et solde sont maintenant gérés dans ChatFilterMixin
            // avant le filtrage pour s'assurer que les données sont capturées même si les messages sont filtrés
            
            // Ici vous pouvez ajouter d'autres patterns de messages si nécessaire
        });
    }
    
    private static void parseJobStats(String message) {
        // Nettoyer le message des codes de couleur
        String cleanMessage = message.replaceAll("§[0-9a-fk-or]", "");
        
        Matcher matcher = JOB_STATS_PATTERN.matcher(cleanMessage);
        if (matcher.find()) {
            try {
                String jobName = matcher.group(1).trim();
                int level = Integer.parseInt(matcher.group(2));
                int percentage = Integer.parseInt(matcher.group(3));
                
                // Calculer l'XP approximative basée sur le pourcentage
                // Pour simplifier, on estime que chaque niveau nécessite 1000 XP de base
                long estimatedMaxXP = 1000L;
                long estimatedCurrentXP = (estimatedMaxXP * percentage) / 100;
                
                // Mettre à jour les stats du métier
                DataTracker.updateJobStats(jobName, level, estimatedCurrentXP, estimatedMaxXP);
                
            } catch (NumberFormatException e) {
                // Ignorer les erreurs de parsing
            }
        }
    }
}
