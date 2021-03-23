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

@Mixin(net.minecraft.enchantment.QuickChargeEnchantment.class)
public class BetterQuickchargeMixin extends Enchantment {
	protected BetterQuickchargeMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
		//TODO Auto-generated constructor stub
	}

	@Inject(at = @At("RETURN"), method = "getMaxLevel()I", cancellable = true)
	private void init(CallbackInfoReturnable<Object> info) {
		
		info.setReturnValue(5);
	}

	@Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack) && EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) == 0;
    }
}
