package com.noirtrou.obtracker.mixin;

import com.noirtrou.obtracker.listeners.TitleListener;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class TitlePacketMixin {

    @Inject(method = "onTitle", at = @At("HEAD"), require = 0)
    private void onTitlePacket(TitleS2CPacket packet, CallbackInfo ci) {
        // Logger la détection du paquet
        TitleListener.logDetection("TitlePacket-Raw", null);
        
        // Note: Les méthodes exactes dépendent de la version de Minecraft
        // Cette approche alternative capture les paquets de titre directement
        System.out.println("[ObTracker] Paquet de titre intercepté: " + packet.getClass().getSimpleName());
    }
}
