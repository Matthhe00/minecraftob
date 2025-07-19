package com.noirtrou.obtracker.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigPopupScreen extends Screen {
    private boolean minionVisible;
    private boolean islandLevelVisible;
    private boolean itemInHandVisible;
    private int customButtonX, customButtonY, customButtonWidth, customButtonHeight;
    private boolean customButtonHovered;
    private int islandButtonX, islandButtonY, islandButtonWidth, islandButtonHeight;
    private boolean islandButtonHovered;
    private int itemButtonX, itemButtonY, itemButtonWidth, itemButtonHeight;
    private boolean itemButtonHovered;
    private final Runnable onClose;
    private int crossButtonX, crossButtonY, crossButtonSize;
    private int resetButtonX, resetButtonY, resetButtonWidth, resetButtonHeight;
    private boolean resetButtonHovered;
    private int islandResetButtonX, islandResetButtonY, islandResetButtonWidth, islandResetButtonHeight;
    private boolean islandResetButtonHovered;
    
    // Configuration des éléments de l'interface
    private static final int POPUP_WIDTH = 220;
    private static final int LINE_HEIGHT = 20;
    private static final int LINE_SPACING = 10;
    private static final int TOP_PADDING = 20;
    private static final int BOTTOM_PADDING = 20;
    private static final int TITLE_HEIGHT = LINE_HEIGHT + 8;
    
    // Liste des éléments configurables (facilite l'ajout de nouveaux éléments)
    private static final String[] CONFIG_ELEMENTS = {
        "Minion",
        "Niveau d'île", 
        "Item"
    };
    
    private static int getNumElements() {
        return CONFIG_ELEMENTS.length;
    }
    
    private static int calculatePopupHeight() {
        return TOP_PADDING + TITLE_HEIGHT + (getNumElements() * (LINE_HEIGHT + LINE_SPACING)) + BOTTOM_PADDING;
    }

    public ConfigPopupScreen(boolean minionVisible, Runnable onClose) {
        super(Text.literal("ObTracker"));
        this.minionVisible = minionVisible;
        this.islandLevelVisible = com.noirtrou.obtracker.gui.ObTrackerConfig.islandLevelVisible;
        this.itemInHandVisible = com.noirtrou.obtracker.gui.ObTrackerConfig.itemInHandVisible;
        this.onClose = onClose;
    }

    @Override
    protected void init() {
        int popupHeight = calculatePopupHeight();
        int popupX = (this.width - POPUP_WIDTH) / 2;
        int popupY = (this.height - popupHeight) / 2;
        
        // Bouton Minion (première ligne)
        int catY = popupY + TOP_PADDING + TITLE_HEIGHT;
        customButtonWidth = 80;
        customButtonHeight = 16;
        customButtonX = popupX + POPUP_WIDTH - customButtonWidth - 18;
        customButtonY = catY - 2;
        customButtonHovered = false;
        
        // Bouton Niveau d'île (deuxième ligne)
        int islandY = catY + LINE_HEIGHT + LINE_SPACING;
        islandButtonWidth = 80;
        islandButtonHeight = 16;
        islandButtonX = popupX + POPUP_WIDTH - islandButtonWidth - 18;
        islandButtonY = islandY - 2;
        islandButtonHovered = false;
        
        // Bouton Item en main (troisième ligne)
        int itemY = islandY + LINE_HEIGHT + LINE_SPACING;
        itemButtonWidth = 80;
        itemButtonHeight = 16;
        itemButtonX = popupX + POPUP_WIDTH - itemButtonWidth - 18;
        itemButtonY = itemY - 2;
        itemButtonHovered = false;
        
        // Positionnement du bouton R pour Minion
        resetButtonWidth = 18;
        resetButtonHeight = 18;
        // Entre le texte "Minion" et le bouton Afficher
        int leftX = (this.width - POPUP_WIDTH) / 2 + 18;
        resetButtonX = leftX + 70; // ajuster selon l'espace
        resetButtonY = catY - 2;
        resetButtonHovered = false;
        
        // Positionnement du bouton R pour Niveau d'île
        islandResetButtonWidth = 18;
        islandResetButtonHeight = 18;
        islandResetButtonX = leftX + 70; // même position X que le bouton Minion
        islandResetButtonY = islandY - 2;
        islandResetButtonHovered = false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int popupHeight = calculatePopupHeight();
        int popupX = (this.width - POPUP_WIDTH) / 2;
        int popupY = (this.height - popupHeight) / 2;
        context.fill(popupX, popupY, popupX + POPUP_WIDTH, popupY + popupHeight, 0xCC222222);
        int titleY = popupY + 12;
        int titleCenterX = popupX + POPUP_WIDTH / 2;
        drawCenteredText(context, Text.literal("§lObTracker"), titleCenterX, titleY, 0xFFFFFF);
        
        int leftX = popupX + 18;
        int catY = popupY + TOP_PADDING + TITLE_HEIGHT;
        
        // Première ligne : Minion
        context.drawText(this.textRenderer, Text.literal("Minion"), leftX, catY, 0xFFFFFFFF, false);
        customButtonHovered = mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight;
        int borderColor = minionVisible ? 0xFF00FF00 : 0xFFFF0000;
        if (customButtonHovered) borderColor |= 0xAA000000;
        int fillColor = 0x00FFFFFF;
        context.fill(customButtonX, customButtonY, customButtonX + customButtonWidth, customButtonY + customButtonHeight, fillColor);
        context.drawBorder(customButtonX, customButtonY, customButtonWidth, customButtonHeight, borderColor);
        String btnText = minionVisible ? "Affiché" : "Masqué";
        int btnTextWidth = this.textRenderer.getWidth(btnText);
        int btnTextX = customButtonX + (customButtonWidth - btnTextWidth) / 2;
        int btnTextY = customButtonY + (customButtonHeight - LINE_HEIGHT) / 2 + 6;
        context.drawText(this.textRenderer, btnText, btnTextX, btnTextY, 0xFFFFFFFF, false);
        
        // Deuxième ligne : Niveau d'île
        int islandY = catY + LINE_HEIGHT + LINE_SPACING;
        context.drawText(this.textRenderer, Text.literal("Niveau d'île"), leftX, islandY, 0xFFFFFFFF, false);
        islandButtonHovered = mouseX >= islandButtonX && mouseX <= islandButtonX + islandButtonWidth && mouseY >= islandButtonY && mouseY <= islandButtonY + islandButtonHeight;
        int islandBorderColor = islandLevelVisible ? 0xFF00FF00 : 0xFFFF0000;
        if (islandButtonHovered) islandBorderColor |= 0xAA000000;
        context.fill(islandButtonX, islandButtonY, islandButtonX + islandButtonWidth, islandButtonY + islandButtonHeight, fillColor);
        context.drawBorder(islandButtonX, islandButtonY, islandButtonWidth, islandButtonHeight, islandBorderColor);
        String islandBtnText = islandLevelVisible ? "Affiché" : "Masqué";
        int islandBtnTextWidth = this.textRenderer.getWidth(islandBtnText);
        int islandBtnTextX = islandButtonX + (islandButtonWidth - islandBtnTextWidth) / 2;
        int islandBtnTextY = islandButtonY + (islandButtonHeight - LINE_HEIGHT) / 2 + 6;
        context.drawText(this.textRenderer, islandBtnText, islandBtnTextX, islandBtnTextY, 0xFFFFFFFF, false);
        
        // Troisième ligne : Item
        int itemY = islandY + LINE_HEIGHT + LINE_SPACING;
        context.drawText(this.textRenderer, Text.literal("Item"), leftX, itemY, 0xFFFFFFFF, false);
        itemButtonHovered = mouseX >= itemButtonX && mouseX <= itemButtonX + itemButtonWidth && mouseY >= itemButtonY && mouseY <= itemButtonY + itemButtonHeight;
        int itemBorderColor = itemInHandVisible ? 0xFF00FF00 : 0xFFFF0000;
        if (itemButtonHovered) itemBorderColor |= 0xAA000000;
        context.fill(itemButtonX, itemButtonY, itemButtonX + itemButtonWidth, itemButtonY + itemButtonHeight, fillColor);
        context.drawBorder(itemButtonX, itemButtonY, itemButtonWidth, itemButtonHeight, itemBorderColor);
        String itemBtnText = itemInHandVisible ? "Affiché" : "Masqué";
        int itemBtnTextWidth = this.textRenderer.getWidth(itemBtnText);
        int itemBtnTextX = itemButtonX + (itemButtonWidth - itemBtnTextWidth) / 2;
        int itemBtnTextY = itemButtonY + (itemButtonHeight - LINE_HEIGHT) / 2 + 6;
        context.drawText(this.textRenderer, itemBtnText, itemBtnTextX, itemBtnTextY, 0xFFFFFFFF, false);
        
        // Bouton R orange pour reset du Niveau d'île
        islandResetButtonHovered = mouseX >= islandResetButtonX && mouseX <= islandResetButtonX + islandResetButtonWidth && mouseY >= islandResetButtonY && mouseY <= islandResetButtonY + islandResetButtonHeight;
        int islandResetBorderColor = 0xFFFF9900;
        int islandResetFillColor = islandResetButtonHovered ? 0x33FF9900 : 0x00FFFFFF;
        context.fill(islandResetButtonX, islandResetButtonY, islandResetButtonX + islandResetButtonWidth, islandResetButtonY + islandResetButtonHeight, islandResetFillColor);
        context.drawBorder(islandResetButtonX, islandResetButtonY, islandResetButtonWidth, islandResetButtonHeight, islandResetBorderColor);
        String islandResetText = "R";
        int islandResetTextWidth = this.textRenderer.getWidth(islandResetText);
        int islandResetTextX = islandResetButtonX + (islandResetButtonWidth - islandResetTextWidth) / 2;
        int islandResetTextY = islandResetButtonY + 3;
        context.drawText(this.textRenderer, islandResetText, islandResetTextX, islandResetTextY, 0xFFFF9900, false);
        
        // Bouton X pour fermer
        int crossSize = 18;
        int crossX = popupX + POPUP_WIDTH - crossSize - 8;
        int crossY = titleY - 2;
        int crossBorderColor = 0xFFDD2222;
        int crossFillColor = 0x00FFFFFF;
        context.fill(crossX, crossY, crossX + crossSize, crossY + crossSize, crossFillColor);
        context.drawBorder(crossX, crossY, crossSize, crossSize, crossBorderColor);
        String crossText = "X";
        int crossTextWidth = this.textRenderer.getWidth(crossText);
        int crossTextX = crossX + (crossSize - crossTextWidth) / 2;
        int crossTextY = crossY + (crossSize - LINE_HEIGHT) / 2 + 6;
        context.drawText(this.textRenderer, crossText, crossTextX, crossTextY, 0xFFFFFFFF, false);
        this.crossButtonX = crossX;
        this.crossButtonY = crossY;
        this.crossButtonSize = crossSize;
        
        // Bouton R orange pour reset des Minions
        resetButtonHovered = mouseX >= resetButtonX && mouseX <= resetButtonX + resetButtonWidth && mouseY >= resetButtonY && mouseY <= resetButtonY + resetButtonHeight;
        int resetBorderColor = 0xFFFF9900;
        int resetFillColor = resetButtonHovered ? 0x33FF9900 : 0x00FFFFFF;
        context.fill(resetButtonX, resetButtonY, resetButtonX + resetButtonWidth, resetButtonY + resetButtonHeight, resetFillColor);
        context.drawBorder(resetButtonX, resetButtonY, resetButtonWidth, resetButtonHeight, resetBorderColor);
        String resetText = "R";
        int resetTextWidth = this.textRenderer.getWidth(resetText);
        int resetTextX = resetButtonX + (resetButtonWidth - resetTextWidth) / 2;
        int resetTextY = resetButtonY + 3;
        context.drawText(this.textRenderer, resetText, resetTextX, resetTextY, 0xFFFF9900, false);
        
        // Texte "by : 实 NoirTrou 实" en bas du popup
        int byTextY = popupY + popupHeight - 15; // Position en bas du popup
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
