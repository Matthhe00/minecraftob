package com.noirtrou.obtracker;

import com.noirtrou.obtracker.listeners.ChatListener;
import com.noirtrou.obtracker.listeners.TitleListener;
import com.noirtrou.obtracker.listeners.TitleCaptureAlternative;
import com.noirtrou.obtracker.gui.ObTrackerConfig;
import net.fabricmc.api.ClientModInitializer;

public class ObTrackerMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Charger la configuration au démarrage
        ObTrackerConfig.loadConfig();
        
        ChatListener.register();
        TitleListener.register();
        TitleCaptureAlternative.register();
        com.noirtrou.obtracker.gui.OverlayRenderer.register();
        com.noirtrou.obtracker.listeners.KeyListener.register();
    }
}
