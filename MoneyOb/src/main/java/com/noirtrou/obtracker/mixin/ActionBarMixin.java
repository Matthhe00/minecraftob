package com.noirtrou.obtracker.mixin;

import com.noirtrou.obtracker.listeners.ActionBarListener;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ActionBarMixin {
    
    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void onActionBarUpdate(Text message, boolean tinted, CallbackInfo ci) {
        if (message != null) {
            ActionBarListener.onActionBarUpdate(message);
        }
    }
}
