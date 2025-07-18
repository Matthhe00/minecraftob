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
                    System.out.println("[ObTracker] === RAPPORT DE CAPTURE ===");
                    TitleListener.logStatistics();
                    TitleAnalyzer.printRecentTitles(3);
                    TitleAnalyzer.printRecentActionBars(3);
                    TitleAnalyzer.analyzeAllCapturedContent();
                    System.out.println("[ObTracker] Voir le fichier: run/title_capture.log");
                }
                wasTPressed = tPressed;
                
                // Touche L pour nettoyer l'historique et le log
                boolean lPressed = org.lwjgl.glfw.GLFW.glfwGetKey(window, GLFW.GLFW_KEY_L) == GLFW.GLFW_PRESS;
                if (lPressed && !wasLPressed) {
                    TitleListener.clearHistory();
                    System.out.println("[ObTracker] Historique nettoyé!");
                }
                wasLPressed = lPressed;
            }
        });
    }
}
