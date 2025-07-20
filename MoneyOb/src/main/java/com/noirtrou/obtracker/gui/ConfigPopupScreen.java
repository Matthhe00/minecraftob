package com.noirtrou.obtracker.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigPopupScreen extends Screen {
    private boolean minionVisible;
    private boolean islandLevelVisible;
    private boolean itemInHandVisible;
    private boolean moneyVisible;
    private boolean filterMinionGainMessages;
    private boolean filterBalMessages;
    
    // Variables pour la colonne de gauche (Affichage/Reset)
    private int moneyButtonX, moneyButtonY, moneyButtonWidth, moneyButtonHeight;
    private boolean moneyButtonHovered;
    private int customButtonX, customButtonY, customButtonWidth, customButtonHeight;
    private boolean customButtonHovered;
    private int islandButtonX, islandButtonY, islandButtonWidth, islandButtonHeight;
    private boolean islandButtonHovered;
    private int itemButtonX, itemButtonY, itemButtonWidth, itemButtonHeight;
    private boolean itemButtonHovered;
    private int resetButtonX, resetButtonY, resetButtonWidth, resetButtonHeight;
    private boolean resetButtonHovered;
    private int islandResetButtonX, islandResetButtonY, islandResetButtonWidth, islandResetButtonHeight;
    private boolean islandResetButtonHovered;
    
    // Variables pour la colonne de droite (Filtres)
    private int filterGainButtonX, filterGainButtonY, filterGainButtonWidth, filterGainButtonHeight;
    private boolean filterGainButtonHovered;
    private int filterBalButtonX, filterBalButtonY, filterBalButtonWidth, filterBalButtonHeight;
    private boolean filterBalButtonHovered;
    
    private final Runnable onClose;
    private int crossButtonX, crossButtonY, crossButtonSize;
    
    // Configuration de l'interface - layout 2 colonnes
    private static final int POPUP_WIDTH = 380;
    private static final int POPUP_HEIGHT = 220;
    private static final int LINE_HEIGHT = 20;
    private static final int LINE_SPACING = 8;
    private static final int TOP_PADDING = 30;
    private static final int SIDE_PADDING = 20;
    private static final int TITLE_HEIGHT = 25;

    public ConfigPopupScreen(boolean minionVisible, Runnable onClose) {
        super(Text.literal("ObTracker"));
        this.minionVisible = minionVisible;
        this.islandLevelVisible = com.noirtrou.obtracker.gui.ObTrackerConfig.islandLevelVisible;
        this.itemInHandVisible = com.noirtrou.obtracker.gui.ObTrackerConfig.itemInHandVisible;
        this.moneyVisible = com.noirtrou.obtracker.gui.ObTrackerConfig.moneyVisible;
        this.filterMinionGainMessages = com.noirtrou.obtracker.gui.ObTrackerConfig.filterMinionGainMessages;
        this.filterBalMessages = com.noirtrou.obtracker.gui.ObTrackerConfig.filterBalMessages;
        this.onClose = onClose;
    }

    @Override
    protected void init() {
        int popupX = (this.width - POPUP_WIDTH) / 2;
        int popupY = (this.height - POPUP_HEIGHT) / 2;
        
        // Colonnes - Division en 2 parties égales
        int columnWidth = (POPUP_WIDTH - 3 * SIDE_PADDING) / 2; // 3 paddings : gauche, milieu, droite
        int leftColumnX = popupX + SIDE_PADDING;
        int rightColumnX = popupX + SIDE_PADDING + columnWidth + SIDE_PADDING;
        int startY = popupY + TOP_PADDING + TITLE_HEIGHT;
        
        // === COLONNE DE GAUCHE ===
        // Argent (ligne 1 gauche)
        moneyButtonWidth = 60;
        moneyButtonHeight = 16;
        moneyButtonX = leftColumnX + columnWidth - 60 - 25; // 25 pour le bouton R
        moneyButtonY = startY;
        
        // Reset Argent (pas implémenté mais position)
        // resetButtonWidth = 20;
        // resetButtonHeight = 16;
        // resetButtonX = leftColumnX + columnWidth - 20;
        // resetButtonY = startY;
        
        // Minion (ligne 2 gauche)
        int line2Y = startY + LINE_HEIGHT + LINE_SPACING;
        customButtonWidth = 60;
        customButtonHeight = 16;
        customButtonX = leftColumnX + columnWidth - 60 - 25;
        customButtonY = line2Y;
        
        // Reset Minion
        resetButtonWidth = 20;
        resetButtonHeight = 16;
        resetButtonX = leftColumnX + columnWidth - 20;
        resetButtonY = line2Y;
        
        // Niveau d'île (ligne 3 gauche)
        int line3Y = line2Y + LINE_HEIGHT + LINE_SPACING;
        islandButtonWidth = 60;
        islandButtonHeight = 16;
        islandButtonX = leftColumnX + columnWidth - 60 - 25;
        islandButtonY = line3Y;
        
        // Reset Niveau d'île
        islandResetButtonWidth = 20;
        islandResetButtonHeight = 16;
        islandResetButtonX = leftColumnX + columnWidth - 20;
        islandResetButtonY = line3Y;
        
        // === COLONNE DE DROITE ===
        // Item (ligne 1 droite)
        itemButtonWidth = 60;
        itemButtonHeight = 16;
        itemButtonX = rightColumnX + columnWidth - 60;
        itemButtonY = startY;
        
        // Filtre gains (ligne 2 droite)
        filterGainButtonWidth = 60;
        filterGainButtonHeight = 16;
        filterGainButtonX = rightColumnX + columnWidth - 60;
        filterGainButtonY = line2Y;
        
        // Filtre /bal (ligne 3 droite)
        filterBalButtonWidth = 60;
        filterBalButtonHeight = 16;
        filterBalButtonX = rightColumnX + columnWidth - 60;
        filterBalButtonY = line3Y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int popupX = (this.width - POPUP_WIDTH) / 2;
        int popupY = (this.height - POPUP_HEIGHT) / 2;
        
        // Fond du popup
        context.fill(popupX, popupY, popupX + POPUP_WIDTH, popupY + POPUP_HEIGHT, 0xCC222222);
        
        // Titre centré
        int titleY = popupY + 12;
        int titleCenterX = popupX + POPUP_WIDTH / 2;
        drawCenteredText(context, Text.literal("§lObTracker"), titleCenterX, titleY, 0xFFFFFF);
        
        // Calcul des colonnes
        int columnWidth = (POPUP_WIDTH - 3 * SIDE_PADDING) / 2;
        int leftColumnX = popupX + SIDE_PADDING;
        int rightColumnX = popupX + SIDE_PADDING + columnWidth + SIDE_PADDING;
        int startY = popupY + TOP_PADDING + TITLE_HEIGHT;
        
        // === COLONNE DE GAUCHE ===
        // Argent (ligne 1 gauche)
        context.drawText(this.textRenderer, Text.literal("Argent"), leftColumnX, startY, 0xFFFFFFFF, false);
        // context.drawText(this.textRenderer, Text.literal("R"), leftColumnX + 50, startY, 0xFFFF9900, false); // Pas de reset pour argent pour l'instant
        moneyButtonHovered = mouseX >= moneyButtonX && mouseX <= moneyButtonX + moneyButtonWidth && mouseY >= moneyButtonY && mouseY <= moneyButtonY + moneyButtonHeight;
        renderButton(context, moneyButtonX, moneyButtonY, moneyButtonWidth, moneyButtonHeight, moneyVisible, moneyButtonHovered, "Affiché", "Masqué");
        
        // Minion (ligne 2 gauche)
        int line2Y = startY + LINE_HEIGHT + LINE_SPACING;
        context.drawText(this.textRenderer, Text.literal("Minion"), leftColumnX, line2Y, 0xFFFFFFFF, false);
        customButtonHovered = mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight;
        renderButton(context, customButtonX, customButtonY, customButtonWidth, customButtonHeight, minionVisible, customButtonHovered, "Affiché", "Masqué");
        // Bouton Reset Minion
        resetButtonHovered = mouseX >= resetButtonX && mouseX <= resetButtonX + resetButtonWidth && mouseY >= resetButtonY && mouseY <= resetButtonY + resetButtonHeight;
        renderResetButton(context, resetButtonX, resetButtonY, resetButtonWidth, resetButtonHeight, resetButtonHovered);
        
        // Niveau d'île (ligne 3 gauche)
        int line3Y = line2Y + LINE_HEIGHT + LINE_SPACING;
        context.drawText(this.textRenderer, Text.literal("Niveau d'île"), leftColumnX, line3Y, 0xFFFFFFFF, false);
        islandButtonHovered = mouseX >= islandButtonX && mouseX <= islandButtonX + islandButtonWidth && mouseY >= islandButtonY && mouseY <= islandButtonY + islandButtonHeight;
        renderButton(context, islandButtonX, islandButtonY, islandButtonWidth, islandButtonHeight, islandLevelVisible, islandButtonHovered, "Affiché", "Masqué");
        // Bouton Reset Niveau d'île
        islandResetButtonHovered = mouseX >= islandResetButtonX && mouseX <= islandResetButtonX + islandResetButtonWidth && mouseY >= islandResetButtonY && mouseY <= islandResetButtonY + islandResetButtonHeight;
        renderResetButton(context, islandResetButtonX, islandResetButtonY, islandResetButtonWidth, islandResetButtonHeight, islandResetButtonHovered);
        
        // === COLONNE DE DROITE ===
        // Item (ligne 1 droite)
        context.drawText(this.textRenderer, Text.literal("Item"), rightColumnX, startY, 0xFFFFFFFF, false);
        itemButtonHovered = mouseX >= itemButtonX && mouseX <= itemButtonX + itemButtonWidth && mouseY >= itemButtonY && mouseY <= itemButtonY + itemButtonHeight;
        renderButton(context, itemButtonX, itemButtonY, itemButtonWidth, itemButtonHeight, itemInHandVisible, itemButtonHovered, "Affiché", "Masqué");
        
        // Gains minions (ligne 2 droite)
        context.drawText(this.textRenderer, Text.literal("Gains coffre"), rightColumnX, line2Y, 0xFFFFFFFF, false);
        filterGainButtonHovered = mouseX >= filterGainButtonX && mouseX <= filterGainButtonX + filterGainButtonWidth && mouseY >= filterGainButtonY && mouseY <= filterGainButtonY + filterGainButtonHeight;
        // Inverser la logique : si filtre activé = masqué, si filtre désactivé = affiché
        renderButton(context, filterGainButtonX, filterGainButtonY, filterGainButtonWidth, filterGainButtonHeight, !filterMinionGainMessages, filterGainButtonHovered, "Affiché", "Masqué");
        
        // Bal (ligne 3 droite)
        context.drawText(this.textRenderer, Text.literal("Bal"), rightColumnX, line3Y, 0xFFFFFFFF, false);
        filterBalButtonHovered = mouseX >= filterBalButtonX && mouseX <= filterBalButtonX + filterBalButtonWidth && mouseY >= filterBalButtonY && mouseY <= filterBalButtonY + filterBalButtonHeight;
        // Inverser la logique : si filtre activé = masqué, si filtre désactivé = affiché
        renderButton(context, filterBalButtonX, filterBalButtonY, filterBalButtonWidth, filterBalButtonHeight, !filterBalMessages, filterBalButtonHovered, "Affiché", "Masqué");
        
        // Bouton X pour fermer
        int crossSize = 18;
        int crossX = popupX + POPUP_WIDTH - crossSize - 8;
        int crossY = titleY - 2;
        renderCloseButton(context, crossX, crossY, crossSize);
        this.crossButtonX = crossX;
        this.crossButtonY = crossY;
        this.crossButtonSize = crossSize;
        
        // Texte "by : 实 NoirTrou 实" en bas du popup
        int byTextY = popupY + POPUP_HEIGHT - 15;
        String byPrefix = "by : 实 ";
        String authorName = "NoirTrou";
        
        // Calculer la largeur totale pour centrer
        int prefixWidth = this.textRenderer.getWidth(byPrefix);
        int authorWidth = this.textRenderer.getWidth(authorName);
        int totalWidth = prefixWidth + authorWidth;
        
        // Position centrée
        int startX = popupX + (POPUP_WIDTH - totalWidth) / 2;
        
        // Dessiner les parties séparément pour le formatage
        context.drawText(this.textRenderer, Text.literal(byPrefix), startX, byTextY, 0xAAAAAA, false);
        context.drawText(this.textRenderer, Text.literal("§l" + authorName), startX + prefixWidth, byTextY, 0xFFFFFF, false);
    }
    
    private void renderButton(DrawContext context, int x, int y, int width, int height, boolean state, boolean hovered, String activeText, String inactiveText) {
        int borderColor = state ? 0xFF00FF00 : 0xFFFF0000;
        if (hovered) borderColor |= 0xAA000000;
        int fillColor = 0x00FFFFFF;
        context.fill(x, y, x + width, y + height, fillColor);
        context.drawBorder(x, y, width, height, borderColor);
        String btnText = state ? activeText : inactiveText;
        int btnTextWidth = this.textRenderer.getWidth(btnText);
        int btnTextX = x + (width - btnTextWidth) / 2;
        int btnTextY = y + (height - 8) / 2;
        context.drawText(this.textRenderer, btnText, btnTextX, btnTextY, 0xFFFFFFFF, false);
    }
    
    private void renderResetButton(DrawContext context, int x, int y, int width, int height, boolean hovered) {
        int borderColor = 0xFFFF9900;
        int fillColor = hovered ? 0x33FF9900 : 0x00FFFFFF;
        context.fill(x, y, x + width, y + height, fillColor);
        context.drawBorder(x, y, width, height, borderColor);
        String resetText = "R";
        int resetTextWidth = this.textRenderer.getWidth(resetText);
        int resetTextX = x + (width - resetTextWidth) / 2;
        int resetTextY = y + (height - 8) / 2;
        context.drawText(this.textRenderer, resetText, resetTextX, resetTextY, 0xFFFF9900, false);
    }
    
    private void renderCloseButton(DrawContext context, int x, int y, int size) {
        int crossBorderColor = 0xFFDD2222;
        int crossFillColor = 0x00FFFFFF;
        context.fill(x, y, x + size, y + size, crossFillColor);
        context.drawBorder(x, y, size, size, crossBorderColor);
        String crossText = "X";
        int crossTextWidth = this.textRenderer.getWidth(crossText);
        int crossTextX = x + (size - crossTextWidth) / 2;
        int crossTextY = y + (size - 8) / 2;
        context.drawText(this.textRenderer, crossText, crossTextX, crossTextY, 0xFFFFFFFF, false);
    }

    private void drawCenteredText(DrawContext context, Text text, int centerX, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        context.drawText(this.textRenderer, text, centerX - textWidth / 2, y, color, false);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_SLASH) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        if (onClose != null) onClose.run();
        super.close();
    }

    public boolean isMinionVisible() {
        return minionVisible;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            // Clic sur le bouton Argent
            if (mouseX >= moneyButtonX && mouseX <= moneyButtonX + moneyButtonWidth && mouseY >= moneyButtonY && mouseY <= moneyButtonY + moneyButtonHeight) {
                moneyVisible = !moneyVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.moneyVisible = moneyVisible;
                this.init();
                return true;
            }
            // Clic sur le bouton Minion
            if (mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight) {
                minionVisible = !minionVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.minionVisible = minionVisible;
                this.init();
                return true;
            }
            // Clic sur le bouton Niveau d'île
            if (mouseX >= islandButtonX && mouseX <= islandButtonX + islandButtonWidth && mouseY >= islandButtonY && mouseY <= islandButtonY + islandButtonHeight) {
                islandLevelVisible = !islandLevelVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.islandLevelVisible = islandLevelVisible;
                this.init();
                return true;
            }
            // Clic sur le bouton Item en main
            if (mouseX >= itemButtonX && mouseX <= itemButtonX + itemButtonWidth && mouseY >= itemButtonY && mouseY <= itemButtonY + itemButtonHeight) {
                itemInHandVisible = !itemInHandVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.itemInHandVisible = itemInHandVisible;
                this.init();
                return true;
            }
            // Clic sur le bouton Filtre gains
            if (mouseX >= filterGainButtonX && mouseX <= filterGainButtonX + filterGainButtonWidth && mouseY >= filterGainButtonY && mouseY <= filterGainButtonY + filterGainButtonHeight) {
                filterMinionGainMessages = !filterMinionGainMessages;
                com.noirtrou.obtracker.gui.ObTrackerConfig.filterMinionGainMessages = filterMinionGainMessages;
                this.init();
                return true;
            }
            // Clic sur le bouton Filtre /bal
            if (mouseX >= filterBalButtonX && mouseX <= filterBalButtonX + filterBalButtonWidth && mouseY >= filterBalButtonY && mouseY <= filterBalButtonY + filterBalButtonHeight) {
                filterBalMessages = !filterBalMessages;
                com.noirtrou.obtracker.gui.ObTrackerConfig.filterBalMessages = filterBalMessages;
                this.init();
                return true;
            }
            // Clic sur le bouton X pour fermer
            if (mouseX >= crossButtonX && mouseX <= crossButtonX + crossButtonSize && mouseY >= crossButtonY && mouseY <= crossButtonY + crossButtonSize) {
                this.close();
                return true;
            }
            // Clic sur le bouton R pour reset des Minions
            if (mouseX >= resetButtonX && mouseX <= resetButtonX + resetButtonWidth && mouseY >= resetButtonY && mouseY <= resetButtonY + resetButtonHeight) {
                com.noirtrou.obtracker.tracker.DataTracker.clearMinionHistory();
                this.init();
                return true;
            }
            // Clic sur le bouton R pour reset du Niveau d'île
            if (mouseX >= islandResetButtonX && mouseX <= islandResetButtonX + islandResetButtonWidth && mouseY >= islandResetButtonY && mouseY <= islandResetButtonY + islandResetButtonHeight) {
                com.noirtrou.obtracker.tracker.DataTracker.clearIslandHistory();
                this.init();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}