package com.noirtrou.obtracker.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Mixin(ItemStack.class)
public class ItemTooltipMixin {
    private static String lastTooltipDurability = null;
    private static ItemStack lastTooltipItem = null;
    
    // Intercepter toute méthode getTooltip disponible
    @Inject(method = "getTooltip", at = @At("RETURN"), require = 0)
    private void onGetTooltip(CallbackInfoReturnable<List<Text>> cir) {
        try {
            ItemStack thisStack = (ItemStack)(Object)this;
            List<Text> tooltipLines = cir.getReturnValue();
            
            if (tooltipLines == null) return;
            
            // Chercher la durabilité dans les lignes de tooltip
            Pattern pattern = Pattern.compile("(\\d+)\\s*durabilités?\\s*restantes?", Pattern.CASE_INSENSITIVE);
            
            for (Text line : tooltipLines) {
                String lineText = line.getString();
                // Supprimer les codes de couleur
                lineText = lineText.replaceAll("§[0-9a-fk-or]", "");
                
                Matcher matcher = pattern.matcher(lineText);
                if (matcher.find()) {
                    // Stocker la durabilité trouvée avec l'item correspondant
                    lastTooltipDurability = matcher.group(1);
                    lastTooltipItem = thisStack.copy(); // Faire une copie pour éviter les problèmes de référence
                    break;
                }
            }
        } catch (Exception e) {
            // Ignorer silencieusement les erreurs pour éviter les crashs
        }
    }
    
    // Méthode statique pour récupérer la dernière durabilité trouvée
    public static String getLastTooltipDurability(ItemStack item) {
        if (lastTooltipItem != null && ItemStack.areEqual(lastTooltipItem, item)) {
            return lastTooltipDurability;
        }
        return null;
    }
}
