package com.noirtrou.obtracker.mixin;

import com.noirtrou.obtracker.listeners.TitlePacketListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin pour capturer les titres affichés dans le HUD
 * Intercepte les méthodes de titre de InGameHud pour une capture directe
 */
@Mixin(InGameHud.class)
public class TitleCaptureMixin {
    
    /**
     * Capture l'affichage des titres principaux
     */
    @Inject(method = "setTitle", at = @At("HEAD"))
    private void onTitleSet(Text title, CallbackInfo ci) {
        try {
            if (title != null) {
                TitlePacketListener.onTitleReceived(title);
            }
        } catch (Exception e) {
            System.err.println("[ObTracker] Erreur lors de la capture du titre: " + e.getMessage());
        }
    }
    
    /**
     * Capture l'affichage des sous-titres
     */
    @Inject(method = "setSubtitle", at = @At("HEAD"))
    private void onSubtitleSet(Text subtitle, CallbackInfo ci) {
        try {
            if (subtitle != null) {
                TitlePacketListener.onSubtitleReceived(subtitle);
            }
        } catch (Exception e) {
            System.err.println("[ObTracker] Erreur lors de la capture du sous-titre: " + e.getMessage());
        }
    }
    
    /**
     * Capture l'affichage des messages overlay (action bar)
     */
    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void onOverlayMessageSet(Text message, boolean tinted, CallbackInfo ci) {
        try {
            if (message != null) {
                TitlePacketListener.onActionBarReceived(message);
            }
        } catch (Exception e) {
            System.err.println("[ObTracker] Erreur lors de la capture de l'action bar: " + e.getMessage());
        }
    }
}
