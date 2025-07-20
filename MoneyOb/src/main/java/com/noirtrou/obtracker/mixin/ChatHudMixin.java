package com.noirtrou.obtracker.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addMessage", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Text message, CallbackInfo ci) {
        // Ce mixin est maintenant désactivé car le filtrage se fait dans ChatFilterMixin
        // pour éviter le double traitement des messages
        
        // String msg = message.getString();
        // if (msg.startsWith("Vous avez gagné ")) {
        //     // Annule l'affichage du message analysé
        //     ci.cancel();
        // }
    }
}
