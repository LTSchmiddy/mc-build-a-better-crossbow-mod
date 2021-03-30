package net.lt_schmiddy.bettercrossbow.mixin;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.RangedWeaponItem;

import net.lt_schmiddy.bettercrossbow.ModEntry;
@Mixin(net.minecraft.enchantment.PunchEnchantment.class)
public class PunchEnchantOnCrossbowMixin extends Enchantment {

	protected PunchEnchantOnCrossbowMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack)
		|| (
			ModEntry.punchOnCrossbow 
			&& stack.getItem() instanceof RangedWeaponItem
		);
    }
}

