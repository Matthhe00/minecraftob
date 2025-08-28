package com.noirtrou.obtracker;

import com.noirtrou.obtracker.listeners.ChatListener;
import com.noirtrou.obtracker.listeners.TitleListener;
import com.noirtrou.obtracker.listeners.TitleCaptureAlternative;
import com.noirtrou.obtracker.gui.ObTrackerConfig;
import net.fabricmc.api.ClientModInitializer;
import com.noirtrou.obtracker.input.Keybinds;
import com.noirtrou.obtracker.input.ClientKeyHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ObTrackerMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Charger la configuration au démarrage
        ObTrackerConfig.loadConfig();
    // Enregistrer les keybinds
    Keybinds.register();
    // Enregistrer le handler client tick pour détecter les appuis
    ClientTickEvents.END_CLIENT_TICK.register(new ClientKeyHandler());
        
        ChatListener.register();
        TitleListener.register();
        TitleCaptureAlternative.register();
        com.noirtrou.obtracker.gui.OverlayRenderer.register();
        com.noirtrou.obtracker.listeners.KeyListener.register();
    }
}
