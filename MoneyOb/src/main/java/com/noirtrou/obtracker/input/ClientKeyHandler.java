package com.noirtrou.obtracker.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ClientKeyHandler implements ClientTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player == null) return;
        if (Keybinds.OPEN_CONFIG != null && Keybinds.OPEN_CONFIG.wasPressed()) {
            // Ouvrir le popup de config
            client.execute(() -> {
                // Garder la logique simple: afficher l'écran de configuration
                client.setScreen(new com.noirtrou.obtracker.gui.ConfigPopupScreen(com.noirtrou.obtracker.gui.ObTrackerConfig.minionVisible, () -> client.setScreen((Screen)null)));
            });
        }
    }
}
