package com.noirtrou.obtracker.mixin;

import com.noirtrou.obtracker.listeners.TitleListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin pour capturer les titres affichés dans le HUD
 * Intercepte les méthodes de titre de InGameHud pour une capture directe des paquets
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
                TitleListener.onTitleReceived(title);
            }
        } catch (Exception e) {
            // Erreur ignorée
        }
    }
    
    /**
     * Capture l'affichage des sous-titres
     */
    @Inject(method = "setSubtitle", at = @At("HEAD"))
    private void onSubtitleSet(Text subtitle, CallbackInfo ci) {
        try {
            if (subtitle != null) {
                TitleListener.onSubtitleReceived(subtitle);
            }
        } catch (Exception e) {
            // Erreur ignorée
        }
    }
    
    /**
     * Capture l'affichage des messages overlay (action bar)
     */
    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void onOverlayMessageSet(Text message, boolean tinted, CallbackInfo ci) {
        try {
            if (message != null) {
                TitleListener.onActionBarReceived(message);
            }
        } catch (Exception e) {
            // Erreur ignorée
        }
    }
}
