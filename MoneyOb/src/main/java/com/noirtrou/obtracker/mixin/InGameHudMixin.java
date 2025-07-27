package com.noirtrou.obtracker.mixin;

import com.noirtrou.obtracker.listeners.TitleListener;
import com.noirtrou.obtracker.listeners.ActionBarListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    // Intercepter les titres
    @Inject(method = "setTitle", at = @At("HEAD"), require = 0)
    private void onSetTitle(Text title, CallbackInfo ci) {
        try {
            TitleListener.logDetection("setTitle", title);
            TitleListener.onTitleReceived(title);
        } catch (Exception e) {
            System.err.println("[ObTracker] Erreur dans onSetTitle: " + e.getMessage());
        }
    }

    // Intercepter les sous-titres
    @Inject(method = "setSubtitle", at = @At("HEAD"), require = 0)
    private void onSetSubtitle(Text subtitle, CallbackInfo ci) {
        try {
            TitleListener.logDetection("setSubtitle", subtitle);
            TitleListener.onSubtitleReceived(subtitle);
        } catch (Exception e) {
            System.err.println("[ObTracker] Erreur dans onSetSubtitle: " + e.getMessage());
        }
    }

    // Intercepter l'action bar
    @Inject(method = "setOverlayMessage", at = @At("HEAD"), require = 0)
    private void onSetOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        try {
            TitleListener.logDetection("setOverlayMessage", message);
            TitleListener.onActionBarReceived(message);
            // Appeler aussi notre ActionBarListener pour le tracking des métiers
            ActionBarListener.onActionBarUpdate(message);
        } catch (Exception e) {
            System.err.println("[ObTracker] Erreur dans onSetOverlayMessage: " + e.getMessage());
        }
    }
}
