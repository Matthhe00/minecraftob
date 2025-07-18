package com.noirtrou.obtracker.gui;

import com.noirtrou.obtracker.tracker.DataTracker;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class OverlayRenderer {
    // Cache des textes pour limiter les calculs à chaque frame
    private static long lastSessionUpdate = -1;
    private static String cachedSessionTime = "";
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
    private static String cachedAverageIsland = "";
    private static String cachedIslandPerHour = "";
    private static String cachedIslandPerMinute = "";
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> render(drawContext));
    }

    private static void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getDebugHud().shouldShowDebugHud()) return;

        int x = 10;
        int y = 10;
        int color = 0xFFFFFF;
        int yellow = 0xFFFF00;

        int titleX = x;
        int titleWidth = client.textRenderer.getWidth("ObTracker") + 8;
        int titleHeight = 16;
        int bgColor = 0xCC222222;
        drawContext.fill(titleX, y - 4, titleX + titleWidth, y - 4 + titleHeight, bgColor);
        drawContext.drawText(client.textRenderer, Text.literal("§f§lObTracker"), x, y, 0xFFFFFF, true);
        y += 20;
        long sessionDuration = DataTracker.getSessionDuration();
        if (sessionDuration != lastSessionUpdate) {
            cachedSessionTime = formatTime(sessionDuration);
            cachedTotalGains = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalMinionGains());
            cachedTotalObjects = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalObjects());
            cachedAverageGain = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getAverageGain());
            cachedAverageObjects = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getAverageObjectsPerItem());
            cachedGainPerHour = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getMinionGainPerHour());
            cachedObjectsPerHour = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getObjectsPerHour());
            cachedGainPerMinute = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getMinionGainPerMinute());
            cachedObjectsPerMinute = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getObjectsPerMinute());
            // Cache Niveau d'île
            cachedTotalIsland = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getTotalIslandLevel());
            cachedAverageIsland = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getAverageIslandLevel());
            cachedIslandPerHour = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getIslandLevelPerHour());
            cachedIslandPerMinute = com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(DataTracker.getIslandLevelPerMinute());
            lastSessionUpdate = sessionDuration;
        }
        // Ajout du temps de jeu global (TDJ) au-dessus de la session
        long globalSessionDuration = com.noirtrou.obtracker.tracker.DataTracker.getGlobalSessionDuration();
        String tdjText = "TDJ: " + formatTime(globalSessionDuration);
        drawContext.drawText(client.textRenderer, Text.literal(tdjText), x, y, 0xFFAA00, true);
        y += 12;
        drawContext.drawText(client.textRenderer, Text.literal("Session: " + cachedSessionTime), x, y, color, true);
        y += 12;
        y += 4; // espace après Session
        if (com.noirtrou.obtracker.gui.ObTrackerConfig.minionVisible) {
            drawContext.drawText(client.textRenderer, Text.literal("§e[Minions]"), x, y, yellow, true);
            y += 12;
            float minionScale = 0.8f;
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(minionScale, minionScale, 1.0f);
            int minionX = (int) (x / minionScale);
            int minionY = (int) (y / minionScale);
            int tab = (int) (16 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Total: " + cachedTotalGains), minionX + tab, minionY, color, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Objets: " + cachedTotalObjects), minionX + tab, minionY, color, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Moyenne: " + cachedAverageGain), minionX + tab, minionY, color, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Moy/item: " + cachedAverageObjects), minionX + tab, minionY, color, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Gains/h: " + cachedGainPerHour + "/h"), minionX + tab, minionY, yellow, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Objets/h: " + cachedObjectsPerHour), minionX + tab, minionY, color, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Gains/min: " + cachedGainPerMinute + "/min"), minionX + tab, minionY, yellow, true);
            minionY += (int) (12 / minionScale);
            drawContext.drawText(client.textRenderer, Text.literal("Objets/min: " + cachedObjectsPerMinute), minionX + tab, minionY, color, true);
            drawContext.getMatrices().pop();
            // Niveau d'île après minion
            y = (int) (minionY * minionScale) + 8;
            y += 4; // espace après Minions
        }
        // Si minion désactivé, Niveau d'île après session
        if (!com.noirtrou.obtracker.gui.ObTrackerConfig.minionVisible) {
            y += 4; // espace après Session si pas de minion
        }
        drawContext.drawText(client.textRenderer, Text.literal("§b[Niveau d'île]"), x, y, 0x00FFFF, true);
        y += 12;
        float islandScale = 0.8f;
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(islandScale, islandScale, 1.0f);
        int islandX = (int) (x / islandScale);
        int islandY = (int) (y / islandScale);
        int tabIsland = (int) (16 / islandScale);
        drawContext.drawText(client.textRenderer, Text.literal("Total: " + cachedTotalIsland), islandX + tabIsland, islandY, color, true);
        islandY += (int) (12 / islandScale);
        drawContext.drawText(client.textRenderer, Text.literal("Moyenne: " + cachedAverageIsland), islandX + tabIsland, islandY, color, true);
        islandY += (int) (12 / islandScale);
        drawContext.drawText(client.textRenderer, Text.literal("/h: " + cachedIslandPerHour), islandX + tabIsland, islandY, yellow, true);
        islandY += (int) (12 / islandScale);
        drawContext.drawText(client.textRenderer, Text.literal("/min: " + cachedIslandPerMinute), islandX + tabIsland, islandY, color, true);
        drawContext.getMatrices().pop();
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
    private static String formatNumber(double n) {
        return com.noirtrou.obtracker.utils.MathUtils.formatNumberShort(n);
    }
}
