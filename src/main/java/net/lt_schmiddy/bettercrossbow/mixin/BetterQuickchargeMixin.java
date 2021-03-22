package net.lt_schmiddy.bettercrossbow.mixin;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// import net.minecraft.enchantment.QuickChargeEnchantment;

@Mixin(net.minecraft.enchantment.QuickChargeEnchantment.class)
public class BetterQuickchargeMixin {
	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	private void init(CallbackInfoReturnable<Object> info) {
		
		info.setReturnValue(5);
		System.out.println("This line is printed by an example mod mixin!");
	}
}
