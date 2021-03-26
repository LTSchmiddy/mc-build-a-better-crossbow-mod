package net.lt_schmiddy.bettercrossbow.mixin;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lt_schmiddy.bettercrossbow.BetterCrossbowMod;

// import net.minecraft.enchantment.QuickChargeEnchantment;


@Mixin( net.minecraft.enchantment.MultishotEnchantment.class)
public class BetterMultishotMixin {
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	private void init(CallbackInfoReturnable<Object> info) {
		
		info.setReturnValue(BetterCrossbowMod.maxMultishotLevel);
	}
}
