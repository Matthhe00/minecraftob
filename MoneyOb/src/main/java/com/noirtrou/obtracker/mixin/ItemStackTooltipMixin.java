package com.noirtrou.obtracker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import com.noirtrou.obtracker.utils.DurabilityCache;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Mixin(ItemStack.class)
public class ItemStackTooltipMixin {
    
    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void captureTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        try {
            ItemStack itemStack = (ItemStack) (Object) this;
            String itemName = itemStack.getName().getString().replaceAll("§[0-9a-fk-or]", "");
            
            List<Text> tooltip = cir.getReturnValue();
            if (tooltip != null && !tooltip.isEmpty()) {
                // Pattern pour capturer "XXXX durabilités restantes"
                Pattern durabilityPattern = Pattern.compile("(\\d+)\\s+durabilités?\\s+restantes?", Pattern.CASE_INSENSITIVE);
                Pattern looseDurabilityPattern = Pattern.compile("(\\d+)\\s*durabilités?\\s*restantes?", Pattern.CASE_INSENSITIVE);
                
                for (Text line : tooltip) {
                    String lineText = line.getString().replaceAll("§[0-9a-fk-or]", "");
                    
                    // Test pattern exact
                    Matcher exactMatcher = durabilityPattern.matcher(lineText);
                    if (exactMatcher.find()) {
                        try {
                            int durability = Integer.parseInt(exactMatcher.group(1));
                            DurabilityCache.storeDurability(itemName, durability);
                            return;
                        } catch (NumberFormatException e) {
                            // Continuer
                        }
                    }
                    
                    // Test pattern loose
                    Matcher looseMatcher = looseDurabilityPattern.matcher(lineText);
                    if (looseMatcher.find()) {
                        try {
                            int durability = Integer.parseInt(looseMatcher.group(1));
                            DurabilityCache.storeDurability(itemName, durability);
                            return;
                        } catch (NumberFormatException e) {
                            // Continuer
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignorer les erreurs pour ne pas casser le jeu
        }
    }
}
