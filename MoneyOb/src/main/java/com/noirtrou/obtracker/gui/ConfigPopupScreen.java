package com.noirtrou.obtracker.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigPopupScreen extends Screen {
    private boolean minionVisible;
    private boolean globalVisible;
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
    private int moneyResetButtonX, moneyResetButtonY, moneyResetButtonWidth, moneyResetButtonHeight;
    private boolean moneyResetButtonHovered;
    // Global (nouvelle catégorie)
    private int globalButtonX, globalButtonY, globalButtonWidth, globalButtonHeight;
    private boolean globalButtonHovered;
    
    // Variables pour la colonne de droite (Filtres)
    private int filterGainButtonX, filterGainButtonY, filterGainButtonWidth, filterGainButtonHeight;
    private boolean filterGainButtonHovered;
    private int filterBalButtonX, filterBalButtonY, filterBalButtonWidth, filterBalButtonHeight;
    private boolean filterBalButtonHovered;
    
    private final Runnable onClose;
    private int crossButtonX, crossButtonY, crossButtonSize;
    
    // Variables pour le slider global
    private int globalSliderX, globalSliderY, globalSliderWidth, globalSliderHeight;
    private boolean globalSliderDragging = false;
    private boolean globalSliderHovered = false;
    
    // Configuration de l'interface - layout 2 colonnes
    private static final int POPUP_WIDTH = 380;
    private static final int POPUP_HEIGHT = 220; // Hauteur réduite pour popup plus compacte
    private static final int LINE_HEIGHT = 20;
    private static final int LINE_SPACING = 8;
    private static final int TOP_PADDING = 30;
    private static final int SIDE_PADDING = 20;
    private static final int TITLE_HEIGHT = 25;
    // Espace restant entre le bas du popup et la barre (plus bas = valeur plus petite)
    private static final int SLIDER_BOTTOM_PADDING = 40;

    public ConfigPopupScreen(boolean minionVisible, Runnable onClose) {
        super(Text.literal("ObTracker"));
        this.minionVisible = minionVisible;
    this.globalVisible = com.noirtrou.obtracker.gui.ObTrackerConfig.globalVisible;
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
    // Ligne 0 (Global) au-dessus du startY
    int line0Y = startY - LINE_HEIGHT - LINE_SPACING;
        
        // === COLONNE DE GAUCHE ===
        // Argent (ligne 1 gauche)
        moneyButtonWidth = 60;
        moneyButtonHeight = 16;
        moneyButtonX = leftColumnX + columnWidth - 60 - 25; // 25 pour le bouton R
        moneyButtonY = startY;

    // Global (ligne 0 gauche)
    globalButtonWidth = 60;
    globalButtonHeight = 16;
    globalButtonX = leftColumnX + columnWidth - 60 - 25;
    globalButtonY = line0Y;
        
        // Reset Argent 
        moneyResetButtonWidth = 20;
        moneyResetButtonHeight = 16;
        moneyResetButtonX = leftColumnX + columnWidth - 20;
        moneyResetButtonY = startY;
        
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
        
        // === SLIDER TAILLE GLOBALE ===
        // Position du slider au-dessus des crédits
    globalSliderWidth = 200;
    globalSliderHeight = 8;
    globalSliderX = popupX + (POPUP_WIDTH - globalSliderWidth) / 2; // Centré
    // Placer la barre plus bas et laisser une ligne d'espace entre les options et la barre
    globalSliderY = popupY + POPUP_HEIGHT - SLIDER_BOTTOM_PADDING - globalSliderHeight;
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
        moneyButtonHovered = mouseX >= moneyButtonX && mouseX <= moneyButtonX + moneyButtonWidth && mouseY >= moneyButtonY && mouseY <= moneyButtonY + moneyButtonHeight;
        renderButton(context, moneyButtonX, moneyButtonY, moneyButtonWidth, moneyButtonHeight, moneyVisible, moneyButtonHovered, "Affiché", "Masqué");
        // Bouton Reset Argent
        moneyResetButtonHovered = mouseX >= moneyResetButtonX && mouseX <= moneyResetButtonX + moneyResetButtonWidth && mouseY >= moneyResetButtonY && mouseY <= moneyResetButtonY + moneyResetButtonHeight;
        renderResetButton(context, moneyResetButtonX, moneyResetButtonY, moneyResetButtonWidth, moneyResetButtonHeight, moneyResetButtonHovered);
        
    // Sell Chest (ligne 2 gauche)
    int line2Y = startY + LINE_HEIGHT + LINE_SPACING;
    context.drawText(this.textRenderer, Text.literal("Sell Chest"), leftColumnX, line2Y, 0xFFFFFFFF, false);
        customButtonHovered = mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight;
        renderButton(context, customButtonX, customButtonY, customButtonWidth, customButtonHeight, minionVisible, customButtonHovered, "Affiché", "Masqué");
        // Bouton Reset Minion
        resetButtonHovered = mouseX >= resetButtonX && mouseX <= resetButtonX + resetButtonWidth && mouseY >= resetButtonY && mouseY <= resetButtonY + resetButtonHeight;
        renderResetButton(context, resetButtonX, resetButtonY, resetButtonWidth, resetButtonHeight, resetButtonHovered);
        
        // Niveau d'île (ligne 3 gauche)
        int line3Y = line2Y + LINE_HEIGHT + LINE_SPACING;
    // Global (au-dessus de tout, inséré en ligne 0 gauche)
    int line0Y = startY - LINE_HEIGHT - LINE_SPACING;
    context.drawText(this.textRenderer, Text.literal("Global"), leftColumnX, line0Y, 0xFFFFFFFF, false);
    globalButtonHovered = mouseX >= globalButtonX && mouseX <= globalButtonX + globalButtonWidth && mouseY >= globalButtonY && mouseY <= globalButtonY + globalButtonHeight;
    renderButton(context, globalButtonX, globalButtonY, globalButtonWidth, globalButtonHeight, globalVisible, globalButtonHovered, "Affiché", "Masqué");
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
        
        // === SLIDER TAILLE HUD ===
        // Titre du slider
    String sliderTitle = "Taille HUD: " + Math.round(com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale * 100) + "%";
    int sliderTitleWidth = this.textRenderer.getWidth(sliderTitle);
    int sliderTitleX = popupX + (POPUP_WIDTH - sliderTitleWidth) / 2;
    // Laisser une ligne d'espace entre les dernières options et le titre du slider
    int sliderTitleY = globalSliderY - (LINE_HEIGHT + LINE_SPACING);
        context.drawText(this.textRenderer, Text.literal(sliderTitle), sliderTitleX, sliderTitleY, 0xFFFFFF, false);
        
        // Vérifier si la souris survole le slider
        globalSliderHovered = mouseX >= globalSliderX && mouseX <= globalSliderX + globalSliderWidth && 
                            mouseY >= globalSliderY && mouseY <= globalSliderY + globalSliderHeight;
        
        // Rendu du slider
        renderSlider(context, globalSliderX, globalSliderY, globalSliderWidth, globalSliderHeight, globalSliderHovered, com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale);
        
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
    
    private void renderSlider(DrawContext context, int x, int y, int width, int height, boolean hovered, float scale) {
        // Fond du slider (rail)
        int railColor = hovered ? 0xFF666666 : 0xFF444444;
        context.fill(x, y, x + width, y + height, railColor);
        context.drawBorder(x, y, width, height, 0xFF888888);
        
        // Calculer la position du curseur basée sur la valeur (0.5f à 2.0f)
        float normalizedValue = (scale - 0.5f) / (2.0f - 0.5f);
        normalizedValue = Math.max(0.0f, Math.min(1.0f, normalizedValue)); // Clamper entre 0 et 1
        
        int knobWidth = 8;
        int knobHeight = height + 4; // Un peu plus haut que le rail
        int knobX = x + (int)((width - knobWidth) * normalizedValue);
        int knobY = y - 2; // Légèrement au-dessus
        
        // Curseur (knob)
        int knobColor = hovered ? 0xFF00FF00 : 0xFFCCCCCC;
        context.fill(knobX, knobY, knobX + knobWidth, knobY + knobHeight, knobColor);
        context.drawBorder(knobX, knobY, knobWidth, knobHeight, 0xFF888888);
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
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
                // Clic sur le bouton Sell Chest
            if (mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight) {
                minionVisible = !minionVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.minionVisible = minionVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
            // Clic sur le bouton Niveau d'île
            if (mouseX >= islandButtonX && mouseX <= islandButtonX + islandButtonWidth && mouseY >= islandButtonY && mouseY <= islandButtonY + islandButtonHeight) {
                islandLevelVisible = !islandLevelVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.islandLevelVisible = islandLevelVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
            // Clic sur le bouton Item en main
            if (mouseX >= itemButtonX && mouseX <= itemButtonX + itemButtonWidth && mouseY >= itemButtonY && mouseY <= itemButtonY + itemButtonHeight) {
                itemInHandVisible = !itemInHandVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.itemInHandVisible = itemInHandVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
            // Clic sur le bouton Filtre gains
            if (mouseX >= filterGainButtonX && mouseX <= filterGainButtonX + filterGainButtonWidth && mouseY >= filterGainButtonY && mouseY <= filterGainButtonY + filterGainButtonHeight) {
                filterMinionGainMessages = !filterMinionGainMessages;
                com.noirtrou.obtracker.gui.ObTrackerConfig.filterMinionGainMessages = filterMinionGainMessages;
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
            // Clic sur le bouton Filtre /bal
            if (mouseX >= filterBalButtonX && mouseX <= filterBalButtonX + filterBalButtonWidth && mouseY >= filterBalButtonY && mouseY <= filterBalButtonY + filterBalButtonHeight) {
                filterBalMessages = !filterBalMessages;
                com.noirtrou.obtracker.gui.ObTrackerConfig.filterBalMessages = filterBalMessages;
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
            // Clic sur le bouton X pour fermer
            if (mouseX >= crossButtonX && mouseX <= crossButtonX + crossButtonSize && mouseY >= crossButtonY && mouseY <= crossButtonY + crossButtonSize) {
                this.close();
                return true;
            }
            // Clic sur le bouton R pour reset de l'Argent (uniquement les ventes)
            if (mouseX >= moneyResetButtonX && mouseX <= moneyResetButtonX + moneyResetButtonWidth && mouseY >= moneyResetButtonY && mouseY <= moneyResetButtonY + moneyResetButtonHeight) {
                com.noirtrou.obtracker.tracker.DataTracker.clearMoneySession();
                this.init();
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
            
            // Clic sur le slider global
            if (mouseX >= globalSliderX && mouseX <= globalSliderX + globalSliderWidth && 
                mouseY >= globalSliderY && mouseY <= globalSliderY + globalSliderHeight) {
                globalSliderDragging = true;
                updateGlobalSliderValue((int)mouseX);
                return true;
            }
            // Clic sur le bouton Global
            if (mouseX >= globalButtonX && mouseX <= globalButtonX + globalButtonWidth && mouseY >= globalButtonY && mouseY <= globalButtonY + globalButtonHeight) {
                globalVisible = !globalVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.globalVisible = globalVisible;
                com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
                this.init();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (globalSliderDragging) {
                globalSliderDragging = false;
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0) {
            if (globalSliderDragging) {
                updateGlobalSliderValue((int)mouseX);
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    // Méthode de mise à jour du slider HUD - maintenant contrôle tous les groupes uniformément
    private void updateGlobalSliderValue(int mouseX) {
        float relativeX = (float)(mouseX - globalSliderX) / globalSliderWidth;
        relativeX = Math.max(0.0f, Math.min(1.0f, relativeX));
        float globalScale = 0.5f + relativeX * 1.5f; // 0.5f à 2.0f
        
        // Mise à jour seulement du globalScale - tous les groupes HUD utilisent cette valeur
        com.noirtrou.obtracker.gui.ObTrackerConfig.globalScale = globalScale;
        
        // Sauvegarder la configuration automatiquement
        com.noirtrou.obtracker.gui.ObTrackerConfig.saveConfig();
    }
}