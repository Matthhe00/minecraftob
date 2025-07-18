package com.noirtrou.obtracker.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ConfigPopupScreen extends Screen {
    private boolean minionVisible;
    private int customButtonX, customButtonY, customButtonWidth, customButtonHeight;
    private boolean customButtonHovered;
    private final Runnable onClose;
    private int crossButtonX, crossButtonY, crossButtonSize;
    private int resetButtonX, resetButtonY, resetButtonWidth, resetButtonHeight;
    private boolean resetButtonHovered;

    public ConfigPopupScreen(boolean minionVisible, Runnable onClose) {
        super(Text.literal("ObTracker"));
        this.minionVisible = minionVisible;
        this.onClose = onClose;
    }

    @Override
    protected void init() {
        int popupWidth = 220;
        int lineHeight = 20;
        int numLines = 2;
        int popupHeight = 20 + numLines * (lineHeight + 10) + 20;
        int popupX = (this.width - popupWidth) / 2;
        int popupY = (this.height - popupHeight) / 2;
        int catY = popupY + 12 + lineHeight + 8;
        customButtonWidth = 120;
        customButtonHeight = 22;
        customButtonX = popupX + popupWidth - customButtonWidth - 18;
        customButtonY = catY - 2;
        customButtonHovered = false;
        // Positionnement du bouton R
        resetButtonWidth = 18;
        resetButtonHeight = 18;
        // Entre le texte "Minion" et le bouton Afficher
        int leftX = (this.width - 220) / 2 + 18;
        int catY2 = ((this.height - (20 + 2 * (20 + 10) + 20)) / 2) + 12 + 20 + 8;
        resetButtonX = leftX + 70; // ajuster selon l'espace
        resetButtonY = catY2 - 2;
        resetButtonHovered = false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int popupWidth = 220;
        int lineHeight = 20;
        int numLines = 2;
        int popupHeight = 20 + numLines * (lineHeight + 10) + 20;
        int popupX = (this.width - popupWidth) / 2;
        int popupY = (this.height - popupHeight) / 2;
        context.fill(popupX, popupY, popupX + popupWidth, popupY + popupHeight, 0xCC222222);
        int titleY = popupY + 12;
        int titleCenterX = popupX + popupWidth / 2;
        drawCenteredText(context, Text.literal("§lObTracker"), titleCenterX, titleY, 0xFFFFFF);
        int leftX = popupX + 18;
        int catY = titleY + lineHeight + 8;
        context.drawText(this.textRenderer, Text.literal("Minion"), leftX, catY, 0xFFFFFFFF, false);
        customButtonHovered = mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight;
        int borderColor = minionVisible ? 0xFF00FF00 : 0xFFFF0000;
        if (customButtonHovered) borderColor |= 0xAA000000;
        int fillColor = 0x00FFFFFF;
        customButtonWidth = 80;
        customButtonHeight = 16;
        customButtonX = popupX + popupWidth - customButtonWidth - 18;
        customButtonY = catY - 2;
        context.fill(customButtonX, customButtonY, customButtonX + customButtonWidth, customButtonY + customButtonHeight, fillColor);
        context.drawBorder(customButtonX, customButtonY, customButtonWidth, customButtonHeight, borderColor);
        String btnText = minionVisible ? "Affiché" : "Masqué";
        int btnTextWidth = this.textRenderer.getWidth(btnText);
        int btnTextX = customButtonX + (customButtonWidth - btnTextWidth) / 2;
        int btnTextY = customButtonY + (customButtonHeight - lineHeight) / 2 + 6;
        context.drawText(this.textRenderer, btnText, btnTextX, btnTextY, 0xFFFFFFFF, false);
        int crossSize = 18;
        int crossX = popupX + popupWidth - crossSize - 8;
        int crossY = titleY - 2;
        int crossBorderColor = 0xFFDD2222;
        int crossFillColor = 0x00FFFFFF;
        context.fill(crossX, crossY, crossX + crossSize, crossY + crossSize, crossFillColor);
        context.drawBorder(crossX, crossY, crossSize, crossSize, crossBorderColor);
        String crossText = "X";
        int crossTextWidth = this.textRenderer.getWidth(crossText);
        int crossTextX = crossX + (crossSize - crossTextWidth) / 2;
        int crossTextY = crossY + (crossSize - lineHeight) / 2 + 6;
        context.drawText(this.textRenderer, crossText, crossTextX, crossTextY, 0xFFFFFFFF, false);
        this.crossButtonX = crossX;
        this.crossButtonY = crossY;
        this.crossButtonSize = crossSize;
        // Bouton R orange
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
            if (mouseX >= customButtonX && mouseX <= customButtonX + customButtonWidth && mouseY >= customButtonY && mouseY <= customButtonY + customButtonHeight) {
                minionVisible = !minionVisible;
                this.init();
                return true;
            }
            if (mouseX >= crossButtonX && mouseX <= crossButtonX + crossButtonSize && mouseY >= crossButtonY && mouseY <= crossButtonY + crossButtonSize) {
                this.close();
                return true;
            }
            if (mouseX >= resetButtonX && mouseX <= resetButtonX + resetButtonWidth && mouseY >= resetButtonY && mouseY <= resetButtonY + resetButtonHeight) {
                com.noirtrou.obtracker.tracker.DataTracker.clearHistory();
                this.init();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
