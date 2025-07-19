package com.noirtrou.obtracker.listeners;

import com.noirtrou.obtracker.gui.ConfigPopupScreen;
import com.noirtrou.obtracker.gui.ObTrackerConfig;
import com.noirtrou.obtracker.utils.TitleAnalyzer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static boolean wasUPressed = false;
    private static boolean wasTPressed = false;
    private static boolean wasLPressed = false;
    private static boolean wasRPressed = false;
    private static boolean wasYPressed = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null) {
                long window = MinecraftClient.getInstance().getWindow().getHandle();
                
                // Touche U pour ouvrir la config
                boolean uPressed = org.lwjgl.glfw.GLFW.glfwGetKey(window, GLFW.GLFW_KEY_U) == GLFW.GLFW_PRESS;
                if (uPressed && !wasUPressed) {
                    client.setScreen(new ConfigPopupScreen(ObTrackerConfig.minionVisible, () -> {
                        ObTrackerConfig.minionVisible = ((ConfigPopupScreen)client.currentScreen).isMinionVisible();
                    }));
                }
                wasUPressed = uPressed;
                
                // Touche T pour afficher les titres capturés (pour test)
                boolean tPressed = org.lwjgl.glfw.GLFW.glfwGetKey(window, GLFW.GLFW_KEY_T) == GLFW.GLFW_PRESS;
                if (tPressed && !wasTPressed) {
                    TitleListener.logStatistics();
                    TitleAnalyzer.printRecentTitles(3);
                    TitleAnalyzer.printRecentActionBars(3);
                    TitleAnalyzer.analyzeAllCapturedContent();
                }
                wasTPressed = tPressed;
                
                // Touche L pour nettoyer l'historique et le log
                boolean lPressed = org.lwjgl.glfw.GLFW.glfwGetKey(window, GLFW.GLFW_KEY_L) == GLFW.GLFW_PRESS;
                if (lPressed && !wasLPressed) {
                    TitleListener.clearHistory();
                }
                wasLPressed = lPressed;
                
                // Touche R pour réinitialiser les compteurs d'écoute instantanée
                boolean rPressed = org.lwjgl.glfw.GLFW.glfwGetKey(window, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS;
                if (rPressed && !wasRPressed) {
                    TitleListener.resetCounters();
                }
                wasRPressed = rPressed;
                
                // Touche Y pour test manuel d'événement de titre
                boolean yPressed = org.lwjgl.glfw.GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Y) == GLFW.GLFW_PRESS;
                if (yPressed && !wasYPressed) {
                    net.minecraft.text.Text testTitle = net.minecraft.text.Text.literal("Test Manuel - Île Niveau " + System.currentTimeMillis() % 1000);
                    TitleListener.onTitleReceived(testTitle);
                }
                wasYPressed = yPressed;
            }
        });
    }
}
