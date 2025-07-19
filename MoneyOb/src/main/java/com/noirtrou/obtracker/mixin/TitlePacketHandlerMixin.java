package com.noirtrou.obtracker.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin pour intercepter directement les paquets de titre du serveur
 * Plus fiable que l'interception des méthodes HUD
 */
@Mixin(ClientPlayNetworkHandler.class)
public class TitlePacketHandlerMixin {
    
    /**
     * Intercepte tous les paquets de titre (approche générique)
     */
    @Inject(method = "onTitle", at = @At("HEAD"), require = 0)
    private void onTitlePacket(TitleS2CPacket packet, CallbackInfo ci) {
        try {
            // Le contenu sera traité par le TitleCaptureMixin lors de l'affichage
        } catch (Exception e) {
            // Erreur ignorée
        }
    }
    
    /**
     * Intercepte les paquets de sous-titre
     */
    @Inject(method = "onSubtitle", at = @At("HEAD"), require = 0)  
    private void onSubtitlePacket(SubtitleS2CPacket packet, CallbackInfo ci) {
        try {
            // Traitement par TitleCaptureMixin
        } catch (Exception e) {
            // Erreur ignorée
        }
    }
    
    /**
     * Intercepte les paquets d'overlay/action bar
     */
    @Inject(method = "onOverlayMessage", at = @At("HEAD"), require = 0)
    private void onOverlayMessagePacket(OverlayMessageS2CPacket packet, CallbackInfo ci) {
        try {
            // Traitement par TitleCaptureMixin
        } catch (Exception e) {
            // Erreur ignorée
        }
    }
}
