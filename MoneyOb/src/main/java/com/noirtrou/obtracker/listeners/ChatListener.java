package com.noirtrou.obtracker.listeners;

import com.noirtrou.obtracker.tracker.DataTracker;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
    private static final Pattern GAIN_PATTERN = Pattern.compile("Vous avez gagné ([\\d.]+).*?\\((\\d+)x objets\\)");

    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String msg = message.getString();
            
            // Traitement des gains de minions
            Matcher matcher = GAIN_PATTERN.matcher(msg);
            if (matcher.find()) {
                double amount = Double.parseDouble(matcher.group(1).replace(",", ""));
                int objects = Integer.parseInt(matcher.group(2));
                DataTracker.addMinionGain(amount);
                DataTracker.addMinionObject(objects);
            }
        });
    }
}
