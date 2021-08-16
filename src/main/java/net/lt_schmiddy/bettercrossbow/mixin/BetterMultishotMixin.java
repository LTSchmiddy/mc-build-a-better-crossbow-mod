package net.lt_schmiddy.bettercrossbow.mixin;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;



@Mixin( net.minecraft.enchantment.MultishotEnchantment.class)
public class BetterMultishotMixin extends Enchantment {
	protected BetterMultishotMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}

	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	private void init(CallbackInfoReturnable<Object> info) {
		info.setReturnValue(ConfigHandler.config.misc.maxMultishotLevel);

	}

	@Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack)
			&& EnchantmentHelper.getLevel(Enchantments.POWER, stack) == 0;
    }
}
