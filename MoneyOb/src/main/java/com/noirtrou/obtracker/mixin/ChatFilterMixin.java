package com.noirtrou.obtracker.mixin;

import com.noirtrou.obtracker.gui.ObTrackerConfig;
import com.noirtrou.obtracker.tracker.DataTracker;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPlayNetworkHandler.class)
public class ChatFilterMixin {
    
    private static final Pattern GAIN_PATTERN = Pattern.compile("Vous avez gagné ([\\d,.]+[KMB]?).*?\\((\\d+)x objets\\)");
    private static final Pattern SELL_PATTERN = Pattern.compile("Vous venez de vendre le contenu de votre inventaire pour ([\\d,.]+[KMB]?)实\\.");
    private static final Pattern BALANCE_PATTERN = Pattern.compile("Votre solde est de ([\\d,.]+[KMB]?)实\\.");
    
    // Pattern pour les métiers avec le nouveau format
    private static final Pattern JOB_STATS_PATTERN = Pattern.compile("([^-]+)\\s*-\\s*Niveau:\\s*(\\d+)\\s*-\\s*\\[.*?\\s*(\\d+)%\\]", Pattern.CASE_INSENSITIVE);
    
    // Pattern pour détecter les messages de job stats (pour filtrage)
    private static final Pattern JOB_STATS_HEADER_PATTERN = Pattern.compile("✎\\s*Recapitulatif\\s+des\\s+métiers:", Pattern.CASE_INSENSITIVE);
    private static final Pattern JOB_STATS_LINE_PATTERN = Pattern.compile("([^-]+)\\s*-\\s*Niveau:\\s*(\\d+)\\s*-\\s*\\[", Pattern.CASE_INSENSITIVE);
    
    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text message = packet.content();
        String messageText = message.getString();
        
        // Vérifier si le message a déjà été traité pour éviter les doublons
        if (DataTracker.isMessageAlreadyProcessed(messageText)) {
            return;
        }
        
        // CAPTURE DES DONNÉES AVANT FILTRAGE
        
        // Traitement des gains de minions
        Matcher minionMatcher = GAIN_PATTERN.matcher(messageText);
        if (minionMatcher.find()) {
            double amount = parseNumberWithSuffix(minionMatcher.group(1));
            int objects = Integer.parseInt(minionMatcher.group(2));
            DataTracker.addMinionGain(amount);
            DataTracker.addMinionObject(objects);
        }
        
        // Traitement des gains de vente - récupérer simplement l'information du texte
        Matcher sellMatcher = SELL_PATTERN.matcher(messageText);
        if (sellMatcher.find()) {
            double amount = parseNumberWithSuffix(sellMatcher.group(1));
            DataTracker.addSellGain(amount);
        }
        
        // Traitement du solde actuel
        Matcher balanceMatcher = BALANCE_PATTERN.matcher(messageText);
        if (balanceMatcher.find()) {
            double amount = parseNumberWithSuffix(balanceMatcher.group(1));
            DataTracker.setCurrentBalance(amount);
        }
        
        // Traitement des stats de métier (/job stats)
        Matcher jobStatsMatcher = JOB_STATS_PATTERN.matcher(messageText);
        if (jobStatsMatcher.find()) {
            try {
                String jobName = jobStatsMatcher.group(1).trim();
                int level = Integer.parseInt(jobStatsMatcher.group(2));
                int percentage = Integer.parseInt(jobStatsMatcher.group(3));
                
                // Calculer l'XP approximative basée sur le pourcentage
                // Pour simplifier, on estime que chaque niveau nécessite 1000 XP de base
                long estimatedMaxXP = 1000L;
                long estimatedCurrentXP = (estimatedMaxXP * percentage) / 100;
                
                DataTracker.updateJobStats(jobName, level, estimatedCurrentXP, estimatedMaxXP);
            } catch (NumberFormatException e) {
                // Ignorer les erreurs de parsing
            }
        }
        
        // FILTRAGE DES MESSAGES SI ACTIVÉ (basé sur le code de référence)
        
        // Filtre gains de minions
        if (ObTrackerConfig.filterMinionGainMessages && 
            messageText.matches(".*Vous avez gagné [\\d,.]+[KMB]?.*\\([\\d]+x objets\\).*")) {
            System.out.println("[ObTracker] [FILTRE GAIN] " + messageText);
            ci.cancel();
            return;
        }
        
        // Filtre /bal
        if (ObTrackerConfig.filterBalMessages && 
            messageText.contains("Votre solde est de")) {
            System.out.println("[ObTracker] [FILTRE BAL] " + messageText);
            ci.cancel();
            return;
        }
        
        // Filtre /job stats
        if (ObTrackerConfig.filterJobStatsMessages && 
            (JOB_STATS_HEADER_PATTERN.matcher(messageText).find() || 
             JOB_STATS_LINE_PATTERN.matcher(messageText).find())) {
            System.out.println("[ObTracker] [FILTRE JOB STATS] " + messageText);
            ci.cancel();
            return;
        }
    }
    
    private double parseNumberWithSuffix(String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            return 0.0;
        }
        
        // Remplacer les virgules par des points pour le parsing
        numberStr = numberStr.replace(",", ".");
        
        // Gérer les suffixes K, M, B
        char lastChar = numberStr.charAt(numberStr.length() - 1);
        double multiplier = 1.0;
        
        if (Character.isLetter(lastChar)) {
            switch (lastChar) {
                case 'K':
                case 'k':
                    multiplier = 1000.0;
                    break;
                case 'M':
                case 'm':
                    multiplier = 1000000.0;
                    break;
                case 'B':
                case 'b':
                    multiplier = 1000000000.0;
                    break;
            }
            numberStr = numberStr.substring(0, numberStr.length() - 1);
        }
        
        try {
            return Double.parseDouble(numberStr) * multiplier;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
