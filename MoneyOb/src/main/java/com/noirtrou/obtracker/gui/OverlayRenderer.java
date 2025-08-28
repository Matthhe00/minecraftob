package com.noirtrou.obtracker.gui;

import com.noirtrou.obtracker.tracker.DataTracker;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.util.Hand;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;

public class OverlayRenderer {
    // Cache des textes pour limiter les calculs à chaque frame
    private static long lastMinionUpdate = -1;
    private static long lastIslandUpdate = -1;
    private static long lastMoneyUpdate = -1;
    
    private static String cachedMinionSessionTime = "";
    private static String cachedIslandSessionTime = "";
    private static String cachedTotalGains = "";
    private static String cachedTotalObjects = "";
    private static String cachedAverageGain = "";
    private static String cachedAverageObjects = "";
    private static String cachedGainPerHour = "";
    private static String cachedObjectsPerHour = "";
    private static String cachedGainPerMinute = "";
    private static String cachedObjectsPerMinute = "";
    // Cache pour Niveau d'île
    private static String cachedTotalIsland = "";
    private static String cachedIslandPerHour = "";
    private static String cachedIslandPerMinute = "";
    // Cache pour Argent
    private static String cachedCurrentBalance = "";
    private static String cachedTotalGain = "";
    private static String cachedMinionTotal = "";
    private static String cachedSellTotal = "";
    private static String cachedMoneySessionTime = "";
    // Cache pour Global (totaux journaliers)
    private static String cachedDailyMinion = "";
    // Espacement additionnel appliqué uniquement après la catégorie Global
    private static final int EXTRA_GLOBAL_SPACING = 12;
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> render(drawContext));
    }

    private static void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getDebugHud().shouldShowDebugHud()) return;

        long currentTime = System.currentTimeMillis();

        int x = 10;
        int y = 10;
        int color = 0xFFFFFF;
        int yellow = 0xFFFF00;
        int orange = 0xFFAA00;

        // Titre avec background - GROUPE UNIFIÉ
        float titleScale = com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale;
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(titleScale, titleScale, 1.0f);
        
        int titleX = (int)(x / titleScale);
        int titleY = (int)(y / titleScale);
        
        // Calculer les dimensions du groupe titre + background
        int titleWidth = client.textRenderer.getWidth("ObTracker") + 8; // Texte + padding
        int titleHeight = 16;
        int bgPadding = 4;
        
        // Dessiner le background du titre
        int bgColor = 0xCC222222;
        drawContext.fill(titleX, titleY - bgPadding, titleX + titleWidth, titleY - bgPadding + titleHeight, bgColor);
        
        // Dessiner le texte du titre
        drawContext.drawText(client.textRenderer, Text.literal("§f§lObTracker"), titleX + 4, titleY, 0xFFFFFF, true); // +4 pour le padding
        
        drawContext.getMatrices().pop();
        
        // Calculer la hauteur totale du groupe titre scalé
        int titleGroupHeight = titleHeight + bgPadding; // Background + padding
        y += (int)(titleGroupHeight * titleScale) + (int)(4 * titleScale); // Espacement après titre aussi scalé
        
        // Mise à jour du cache Minion
        long minionSessionDuration = DataTracker.getMinionSessionDuration();
        if (minionSessionDuration != lastMinionUpdate) {
            cachedMinionSessionTime = formatTime(minionSessionDuration);
            cachedTotalGains = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalMinionGains());
            cachedTotalObjects = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalObjects());
            cachedAverageGain = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getAverageGain());
            cachedAverageObjects = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getAverageObjectsPerItem());
            cachedGainPerHour = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getMinionGainPerHour());
            cachedObjectsPerHour = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getObjectsPerHour());
            cachedGainPerMinute = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getMinionGainPerMinute());
            cachedObjectsPerMinute = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getObjectsPerMinute());
            lastMinionUpdate = minionSessionDuration;
        }
        
        // Mise à jour du cache Niveau d'île
        long islandSessionDuration = DataTracker.getIslandSessionDuration();
        if (islandSessionDuration != lastIslandUpdate) {
            cachedIslandSessionTime = formatTime(islandSessionDuration);
            cachedTotalIsland = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalIslandLevel());
            cachedIslandPerHour = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getIslandLevelPerHour());
            cachedIslandPerMinute = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getIslandLevelPerMinute());
            lastIslandUpdate = islandSessionDuration;
        }
        
        // Mise à jour du cache Argent - réutiliser currentTime
        if (currentTime - lastMoneyUpdate > 1000) { // Mise à jour chaque seconde
            cachedCurrentBalance = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getCurrentBalance());
            cachedTotalGain = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalGain());
            cachedMinionTotal = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalMinionGains());
            cachedSellTotal = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalSellGains());
            cachedMoneySessionTime = formatTime(DataTracker.getMoneySessionDuration());
            // Global daily totals
            cachedDailyMinion = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getDailyTotalMinionGains());
            // (combined option removed)
            lastMoneyUpdate = currentTime;
        }
        
        // Exécution automatique de la commande /bal à intervalles réguliers (si activée)
        if (com.noirtrou.obtracker.gui.ObTrackerConfig.autoBalCommand && 
            DataTracker.shouldExecuteBalCommand() && client.player != null) {
            if (client.getNetworkHandler() != null) {
                client.player.networkHandler.sendChatCommand("bal");
            }
        }
        
        // Affichage de l'item en main avec durabilité dans la zone HUD (si activé)
        // Optimisation : vérifier la visibilité avant le calcul coûteux
        if (com.noirtrou.obtracker.gui.ObTrackerConfig.itemInHandVisible && 
            client.player.getStackInHand(Hand.MAIN_HAND) != null && 
            !client.player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            renderItemInHand(drawContext, client, x, y);
            
            // Optimisation : ne render les armures que si nécessaire
            if (hasArmorWithDurability(client)) {
                renderArmorItems(drawContext, client);
            }
        }
        
        // SECTION ARGENT EN PREMIER (si activée) - GROUPE UNIFIÉ
        // SECTION GLOBAL (totaux journaliers) si activée
        if (com.noirtrou.obtracker.gui.ObTrackerConfig.globalVisible) {
            int groupHeightGlobal = 2 * 12 + 4; // 2 lignes
            float globalScale = com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale;
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(globalScale, globalScale, 1.0f);
            int gX = (int)(x / globalScale);
            int gY = (int)(y / globalScale);
                drawContext.drawText(client.textRenderer, Text.literal("\u00a7d[Global]"), gX, gY, 0xFF55FF, true);
                int tabG = 16;
                gY += 12;
                // Daily SC -> correspond aux gains des sell chests (minions)
                drawContext.drawText(client.textRenderer, Text.literal("SC: " + cachedDailyMinion + "\u00a7f\u5b9e"), gX + tabG, gY, 0xFFFFFF, true);
                gY += 12;
                // Daily Lvl -> total des niveaux d'île gagnés
                String cachedDailyIsland = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getDailyTotalIslandLevels());
                drawContext.drawText(client.textRenderer, Text.literal("Lvl: " + cachedDailyIsland), gX + tabG, gY, 0xFFFFFF, true);
                gY += 12;
            drawContext.getMatrices().pop();
            y += (int)(groupHeightGlobal * globalScale) + (int)(4 * globalScale) + (int)(EXTRA_GLOBAL_SPACING * globalScale);
        }

        if (com.noirtrou.obtracker.gui.ObTrackerConfig.moneyVisible) {
            // Calculer la hauteur totale du groupe AVANT transformation
            int groupHeight = 5 * 12 + 4; // 5 lignes * hauteur ligne + espacement interne
            
            float moneyScale = com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale; // Utiliser le globalScale du slider
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(moneyScale, moneyScale, 1.0f);
            
            int moneyX = (int) (x / moneyScale);
            int moneyY = (int) (y / moneyScale);
            
            // Rendu de tout le groupe en une seule transformation
            // Titre de la section Argent avec chrono en petit à côté (même taille que les éléments)
            drawContext.drawText(client.textRenderer, Text.literal("§e[Argent]"), moneyX, moneyY, yellow, true);
            int moneyTitleWidth = client.textRenderer.getWidth("[Argent]");
            drawContext.drawText(client.textRenderer, Text.literal("§7(" + cachedMoneySessionTime + ")"), moneyX + moneyTitleWidth + 4, moneyY, 0xAAAAAA, true);
            moneyY += 12;
            
            int tabMoney = 16;
            // Total du solde actuel
            drawContext.drawText(client.textRenderer, Text.literal("Total: " + cachedCurrentBalance + "§f实"), moneyX + tabMoney, moneyY, color, true);
            moneyY += 12;
            // Gain total (minion + sell) depuis le début de la session
            drawContext.drawText(client.textRenderer, Text.literal("Gain total: " + cachedTotalGain + "§f实"), moneyX + tabMoney, moneyY, yellow, true);
            moneyY += 12;
            // Gains des sell chests (SC)
            drawContext.drawText(client.textRenderer, Text.literal("SC: " + cachedMinionTotal + "§f实"), moneyX + tabMoney, moneyY, color, true);
            moneyY += 12;
            // Gains des ventes
            drawContext.drawText(client.textRenderer, Text.literal("Sell: " + cachedSellTotal + "§f实"), moneyX + tabMoney, moneyY, color, true);
            
            drawContext.getMatrices().pop();
            
            // Mise à jour de Y avec la hauteur du groupe transformé + espacement
            y += (int)(groupHeight * moneyScale) + (int)(4 * moneyScale); // Espacement après Argent aussi scalé
        }
        
        // SECTION MINIONS EN DEUXIÈME (si activée) - GROUPE UNIFIÉ
        if (com.noirtrou.obtracker.gui.ObTrackerConfig.minionVisible) {
            // Calculer la hauteur totale du groupe AVANT transformation
            int groupHeight = 9 * 12 + 4; // 9 lignes * hauteur ligne + espacement interne
            
            float minionScale = com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale; // Utiliser le globalScale du slider
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(minionScale, minionScale, 1.0f);
            
            int minionX = (int) (x / minionScale);
            int minionY = (int) (y / minionScale);
            
            // Rendu de tout le groupe en une seule transformation
            // Titre avec chrono en petit à côté (même taille que les éléments)
            drawContext.drawText(client.textRenderer, Text.literal("§6[SC]"), minionX, minionY, orange, true);
            int minionTitleWidth = client.textRenderer.getWidth("[SC]");
            drawContext.drawText(client.textRenderer, Text.literal("§7(" + cachedMinionSessionTime + ")"), minionX + minionTitleWidth + 4, minionY, 0xAAAAAA, true);
            minionY += 12;
            
            int tab = 16;
            // Mettre en premier les statistiques avec le caractère 实
            drawContext.drawText(client.textRenderer, Text.literal("Total (SC): " + cachedTotalGains + "§f实"), minionX + tab, minionY, color, true);
            minionY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Moyenne: " + cachedAverageGain + "§f实"), minionX + tab, minionY, color, true);
            minionY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Gains/h: " + cachedGainPerHour + "§f实"), minionX + tab, minionY, orange, true);
            minionY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Gains/min: " + cachedGainPerMinute + "§f实"), minionX + tab, minionY, orange, true);
            minionY += 12;
            // Ensuite les statistiques sans le caractère 实
            drawContext.drawText(client.textRenderer, Text.literal("Objets: " + cachedTotalObjects), minionX + tab, minionY, color, true);
            minionY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Moy/Objects: " + cachedAverageObjects), minionX + tab, minionY, color, true);
            minionY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Objets/h: " + cachedObjectsPerHour), minionX + tab, minionY, color, true);
            minionY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Objets/min: " + cachedObjectsPerMinute), minionX + tab, minionY, color, true);
            
            drawContext.getMatrices().pop();
            
            // Mise à jour de Y avec la hauteur du groupe transformé + espacement
            y += (int)(groupHeight * minionScale) + (int)(4 * minionScale); // Espacement après Minions aussi scalé
        }
        
        // SECTION NIVEAU D'ÎLE EN TROISIÈME (si activée) - GROUPE UNIFIÉ
        if (com.noirtrou.obtracker.gui.ObTrackerConfig.islandLevelVisible) {
            // Calculer la hauteur totale du groupe AVANT transformation
            int groupHeight = 4 * 12 + 4; // 4 lignes * hauteur ligne + espacement interne
            
            float islandScale = com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale; // Utiliser le globalScale du slider
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(islandScale, islandScale, 1.0f);
            
            int islandX = (int) (x / islandScale);
            int islandY = (int) (y / islandScale);
            
            // Rendu de tout le groupe en une seule transformation
            // Titre avec chrono en petit à côté (même taille que les éléments)
            drawContext.drawText(client.textRenderer, Text.literal("§b[Niveau d'île]"), islandX, islandY, 0x00FFFF, true);
            int islandTitleWidth = client.textRenderer.getWidth("[Niveau d'île]");
            drawContext.drawText(client.textRenderer, Text.literal("§7(" + cachedIslandSessionTime + ")"), islandX + islandTitleWidth + 4, islandY, 0xAAAAAA, true);
            islandY += 12;
            
            int tabIsland = 16;
            drawContext.drawText(client.textRenderer, Text.literal("Total: " + cachedTotalIsland), islandX + tabIsland, islandY, color, true);
            islandY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Gains/h: " + cachedIslandPerHour), islandX + tabIsland, islandY, 0x00FFFF, true);
            islandY += 12;
            drawContext.drawText(client.textRenderer, Text.literal("Gains/min: " + cachedIslandPerMinute), islandX + tabIsland, islandY, color, true);
            
            drawContext.getMatrices().pop();
            
            // Mise à jour de Y avec la hauteur du groupe transformé + espacement
            y += (int)(groupHeight * islandScale) + (int)(4 * islandScale); // Espacement après Niveau d'île aussi scalé
        }
    }

    private static String formatTime(long seconds) {
        if (seconds < 60) {
            return String.format("%ds", seconds);
        } else if (seconds < 3600) {
            long m = seconds / 60;
            long s = seconds % 60;
            return String.format("%dm %ds", m, s);
        } else {
            long h = seconds / 3600;
            long m = (seconds % 3600) / 60;
            long s = seconds % 60;
            return String.format("%dh %dm %ds", h, m, s);
        }
    }
    
    private static void renderItemInHand(DrawContext drawContext, MinecraftClient client, int x, int y) {
        if (client.player == null) return;
        
        ItemStack mainHandItem = client.player.getStackInHand(Hand.MAIN_HAND);
        
        if (!mainHandItem.isEmpty()) {
            // Calculer la position à droite de la barre d'inventaire (hotbar)
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            
            // Position : à droite de la hotbar mais décalée vers la gauche pour éviter la barre de faim
            int hudX = screenWidth / 2 + 91 - 35; // Centre + moitié hotbar - décalage plus important vers la gauche
            int hudY = screenHeight - 39 - 15; // Au-dessus de la barre XP/vie avec plus d'espacement
            
            // Échelle réduite pour l'item (plus petit)
            float itemScale = 0.7f;
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(itemScale, itemScale, 1.0f);
            
            int scaledX = (int) (hudX / itemScale);
            int scaledY = (int) (hudY / itemScale);
            
            // Récupérer la durabilité depuis la description de l'item
            int durabilityFromTooltip = extractDurabilityFromTooltip(mainHandItem, client);
            
            boolean hasDurability = (durabilityFromTooltip != -1);
            
            // Ne pas afficher si l'item n'a pas de durabilité
            if (!hasDurability) {
                drawContext.getMatrices().pop();
                return;
            }
            
            String durabilityText;
            int durabilityColor;
            
            // Afficher la valeur de durabilité brute sans conversion
            durabilityText = String.valueOf(durabilityFromTooltip);
            // Couleur blanche par défaut, rouge si 15% ou moins de durabilité
            durabilityColor = 0xFFFFFF; // Blanc par défaut
            
            // Essayer de calculer le pourcentage pour les items vanilla
            if (mainHandItem.isDamageable()) {
                int maxDamage = mainHandItem.getMaxDamage();
                if (maxDamage > 0) {
                    int damage = mainHandItem.getDamage();
                    int remaining = maxDamage - damage;
                    double percentage = (double) remaining / maxDamage;
                    if (percentage <= 0.15) { // 15% ou moins
                        durabilityColor = 0xFF5555; // Rouge
                    }
                }
            } else {
                // Pour les items modifiés, essayer d'estimer avec des valeurs courantes
                // Si durabilité < 200, considérer comme critique (estimation approximative)
                if (durabilityFromTooltip < 200) {
                    durabilityColor = 0xFF5555; // Rouge
                }
            }
            
            // Calculer la taille du fond gris pour couvrir l'item et le texte de durabilité
            int textWidth = client.textRenderer.getWidth(durabilityText);
            int textX = scaledX + (int)(20 / itemScale); // Position du texte à droite de l'item
            
            // Background qui couvre l'item + le texte de durabilité
            int backgroundPadding = 1; // Réduction du padding pour moins de hauteur
            int backgroundX = scaledX - backgroundPadding;
            int backgroundY = scaledY - backgroundPadding;
            int backgroundWidth = (textX - scaledX) + textWidth + backgroundPadding * 2; // De l'item jusqu'à la fin du texte
            int backgroundHeight = (int)(14 / itemScale) + backgroundPadding * 2; // Hauteur réduite
            
            // Dessiner le fond gris foncé et transparent qui couvre tout
            int backgroundColor = 0x80333333; // Gris foncé avec plus de transparence
            drawContext.fill(backgroundX, backgroundY, 
                           backgroundX + backgroundWidth, 
                           backgroundY + backgroundHeight, 
                           backgroundColor);
            
            // Dessiner l'icône de l'item en petit
            drawContext.drawItem(mainHandItem, scaledX, scaledY);
            
            // Afficher la durabilité ou le debug
            int textY = scaledY + (int)(3 / itemScale); // Position verticale du texte ajustée pour moins de hauteur
            
            drawContext.drawText(client.textRenderer, Text.literal(durabilityText), textX, textY, durabilityColor, true);
            
            drawContext.getMatrices().pop();
        }
    }
    
    private static void renderArmorItems(DrawContext drawContext, MinecraftClient client) {
        if (client.player == null) return;
        
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        
        // Position à droite de la hotbar
        int baseX = screenWidth / 2 + 91 + 6; // Juste à droite de la hotbar
        int baseY = screenHeight - 22; // Position de base, sera ajustée après calcul des armures valides
        
        float armorScale = 0.6f; // Plus petit que l'item en main
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(armorScale, armorScale, 1.0f);
        
        // Récupérer les pièces d'armure (ordre: casque, plastron, jambières, bottes)
        ItemStack[] armorItems = {
            client.player.getInventory().getArmorStack(3), // Casque
            client.player.getInventory().getArmorStack(2), // Plastron
            client.player.getInventory().getArmorStack(1), // Jambières
            client.player.getInventory().getArmorStack(0)  // Bottes
        };
        
        // Calculer d'abord quelles armures ont une durabilité pour dimensionner le background global
        java.util.List<Integer> validArmor = new java.util.ArrayList<>();
        int maxTextWidth = 0;
        
        for (int i = 0; i < armorItems.length; i++) {
            ItemStack armorItem = armorItems[i];
            if (!armorItem.isEmpty()) {
                int durabilityFromTooltip = extractDurabilityFromTooltip(armorItem, client);
                if (durabilityFromTooltip != -1) {
                    validArmor.add(i);
                    String durabilityText = String.valueOf(durabilityFromTooltip);
                    int textWidth = client.textRenderer.getWidth(durabilityText);
                    maxTextWidth = Math.max(maxTextWidth, textWidth);
                }
            }
        }
        
        // Si aucune armure avec durabilité, ne rien afficher
        if (validArmor.isEmpty()) {
            drawContext.getMatrices().pop();
            return;
        }
        
        // Ajuster la position Y pour que les pieds soient alignés avec le bas de la hotbar
        baseY = baseY - (int)((validArmor.size() - 1) * (16 * 0.5f * armorScale));
        
        int scaledBaseX = (int) (baseX / armorScale);
        int scaledBaseY = (int) (baseY / armorScale);
        
        // Calculer les dimensions du background global
        int backgroundPadding = 1; // Padding réduit pour moins de hauteur
        int itemSize = (int)(16 / armorScale);
        int itemOverlap = itemSize / 2; // Chevauchement de 50% pour ultra-compacité
        int textOffset = (int)(20 / armorScale);
        int backgroundWidth = textOffset + maxTextWidth + backgroundPadding * 2;
        int backgroundHeight = itemSize + (validArmor.size() - 1) * itemOverlap + backgroundPadding * 2; // Chevauchement des items
        
        // Dessiner le background global
        int backgroundColor = 0x80333333;
        drawContext.fill(scaledBaseX - backgroundPadding, 
                       scaledBaseY - backgroundPadding,
                       scaledBaseX + backgroundWidth, 
                       scaledBaseY + backgroundHeight - backgroundPadding, 
                       backgroundColor);
        
        // Afficher les armures en colonne
        int currentY = scaledBaseY;
        for (int armorIndex : validArmor) {
            ItemStack armorItem = armorItems[armorIndex];
            int durabilityFromTooltip = extractDurabilityFromTooltip(armorItem, client);
            
            String durabilityText = String.valueOf(durabilityFromTooltip);
            int durabilityColor = 0xFFFFFF; // Blanc par défaut
            
            // Essayer de calculer le pourcentage pour les items vanilla
            if (armorItem.isDamageable()) {
                int maxDamage = armorItem.getMaxDamage();
                if (maxDamage > 0) {
                    int damage = armorItem.getDamage();
                    int remaining = maxDamage - damage;
                    double percentage = (double) remaining / maxDamage;
                    if (percentage <= 0.15) { // 15% ou moins
                        durabilityColor = 0xFF5555; // Rouge
                    }
                }
            } else {
                // Pour les items modifiés, essayer d'estimer avec des valeurs courantes
                // Si durabilité < 200, considérer comme critique (estimation approximative)
                if (durabilityFromTooltip < 200) {
                    durabilityColor = 0xFF5555; // Rouge
                }
            }
            
            // Dessiner l'item d'armure
            drawContext.drawItem(armorItem, scaledBaseX, currentY);
            
            // Dessiner la durabilité
            int textX = scaledBaseX + textOffset;
            int textY = currentY + (int)(3 / armorScale);
            drawContext.drawText(client.textRenderer, Text.literal(durabilityText), textX, textY, durabilityColor, true);
            
            // Passer à la ligne suivante
            currentY += itemOverlap; // Chevauchement de 50% pour ultra-compacité
        }
        
        drawContext.getMatrices().pop();
    }
    
    // Cache pour éviter le spam de logs
    private static long lastTooltipCheck = 0;
    private static String lastItemCached = "";
    private static int lastDurabilityCached = -1;

    // Méthode pour extraire la durabilité depuis le tooltip de l'item
    private static int extractDurabilityFromTooltip(ItemStack itemStack, MinecraftClient client) {
        try {
            String displayName = itemStack.getName().getString();
            String cleanDisplayName = displayName.replaceAll("§[0-9a-fk-or]", "");
            
            // Cache pour éviter le spam - vérifier seulement toutes les 500ms ou si l'item change
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTooltipCheck < 500 && cleanDisplayName.equals(lastItemCached)) {
                return lastDurabilityCached;
            }
            
            lastTooltipCheck = currentTime;
            lastItemCached = cleanDisplayName;
            
            // 1. Patterns simples sur le nom de l'item
            Pattern exactDurabilityPattern = Pattern.compile("(\\d+)\\s+durabilités?\\s+restantes?", Pattern.CASE_INSENSITIVE);
            Pattern looseDurabilityPattern = Pattern.compile("(\\d+)\\s*durabilités?\\s*restantes?", Pattern.CASE_INSENSITIVE);
            
            // Chercher dans le nom de l'item
            Matcher exactMatcher = exactDurabilityPattern.matcher(cleanDisplayName);
            if (exactMatcher.find()) {
                try {
                    int durability = Integer.parseInt(exactMatcher.group(1));
                    lastDurabilityCached = durability;
                    return durability;
                } catch (NumberFormatException e) {
                    // Continuer
                }
            }
            
            Matcher looseMatcher = looseDurabilityPattern.matcher(cleanDisplayName);
            if (looseMatcher.find()) {
                try {
                    int durability = Integer.parseInt(looseMatcher.group(1));
                    lastDurabilityCached = durability;
                    return durability;
                } catch (NumberFormatException e) {
                    // Continuer
                }
            }
            
            // 2. Chercher dans la description/tooltip de l'item
            try {
                // Obtenir le tooltip complet de l'item
                List<Text> tooltip = itemStack.getTooltip(
                    net.minecraft.item.Item.TooltipContext.DEFAULT,
                    client.player,
                    TooltipType.BASIC
                );
                
                // Patterns pour chercher la durabilité dans la description
                Pattern durabilityInTooltipPattern1 = Pattern.compile("Durabilité\\s*:\\s*(\\d+)\\s*/\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
                Pattern durabilityInTooltipPattern2 = Pattern.compile("(\\d+)\\s*durabilités?\\s*restantes?", Pattern.CASE_INSENSITIVE);
                Pattern durabilityInTooltipPattern3 = Pattern.compile("(\\d+)\\s*/\\s*(\\d+)\\s*durabilité", Pattern.CASE_INSENSITIVE);
                Pattern durabilityInTooltipPattern4 = Pattern.compile("\\[(\\d+)\\s*/\\s*(\\d+)\\]", Pattern.CASE_INSENSITIVE);
                
                for (Text tooltipLine : tooltip) {
                    String line = tooltipLine.getString();
                    String cleanLine = line.replaceAll("§[0-9a-fk-or]", "");
                    
                    // Essayer différents patterns
                    Matcher matcher1 = durabilityInTooltipPattern1.matcher(cleanLine);
                    if (matcher1.find()) {
                        try {
                            int current = Integer.parseInt(matcher1.group(1));
                            lastDurabilityCached = current;
                            return current;
                        } catch (NumberFormatException e) {
                            // Continuer
                        }
                    }
                    
                    Matcher matcher2 = durabilityInTooltipPattern2.matcher(cleanLine);
                    if (matcher2.find()) {
                        try {
                            int durability = Integer.parseInt(matcher2.group(1));
                            lastDurabilityCached = durability;
                            return durability;
                        } catch (NumberFormatException e) {
                            // Continuer
                        }
                    }
                    
                    Matcher matcher3 = durabilityInTooltipPattern3.matcher(cleanLine);
                    if (matcher3.find()) {
                        try {
                            int current = Integer.parseInt(matcher3.group(1));
                            lastDurabilityCached = current;
                            return current;
                        } catch (NumberFormatException e) {
                            // Continuer
                        }
                    }
                    
                    Matcher matcher4 = durabilityInTooltipPattern4.matcher(cleanLine);
                    if (matcher4.find()) {
                        try {
                            int current = Integer.parseInt(matcher4.group(1));
                            lastDurabilityCached = current;
                            return current;
                        } catch (NumberFormatException e) {
                            // Continuer
                        }
                    }
                }
            } catch (Exception e) {
                // Continuer vers fallback vanilla
            }
            
            // 3. Fallback final: Vérifier durabilité vanilla
            if (itemStack.isDamageable()) {
                int maxDamage = itemStack.getMaxDamage();
                int damage = itemStack.getDamage();
                int remaining = maxDamage - damage;
                
                if (maxDamage > 0) {
                    lastDurabilityCached = remaining;
                    return remaining;
                }
            }
            
            lastDurabilityCached = -1;
            
        } catch (Exception e) {
            lastDurabilityCached = -1;
        }
        
        return lastDurabilityCached;
    }
    
    // Méthode optimisée pour vérifier s'il y a des armures avec durabilité
    private static boolean hasArmorWithDurability(MinecraftClient client) {
        if (client.player == null) return false;
        
        // Vérification rapide sans calcul de durabilité complet
        ItemStack[] armorItems = {
            client.player.getInventory().getArmorStack(3), // Casque
            client.player.getInventory().getArmorStack(2), // Plastron
            client.player.getInventory().getArmorStack(1), // Jambières
            client.player.getInventory().getArmorStack(0)  // Bottes
        };
        
        for (ItemStack armorItem : armorItems) {
            if (!armorItem.isEmpty() && 
                (armorItem.isDamageable() || armorItem.getName().getString().contains("durabilité"))) {
                return true;
            }
        }
        return false;
    }
}
