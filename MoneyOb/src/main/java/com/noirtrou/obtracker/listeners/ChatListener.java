package com.noirtrou.obtracker.listeners;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class ChatListener {
    
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String msg = message.getString();
            
            // Ce listener peut être utilisé pour d'autres types de messages
            // Les gains de minions, ventes et solde sont maintenant gérés dans ChatFilterMixin
            // avant le filtrage pour s'assurer que les données sont capturées même si les messages sont filtrés
            
            // Ici vous pouvez ajouter d'autres patterns de messages si nécessaire
        });
    }
}
