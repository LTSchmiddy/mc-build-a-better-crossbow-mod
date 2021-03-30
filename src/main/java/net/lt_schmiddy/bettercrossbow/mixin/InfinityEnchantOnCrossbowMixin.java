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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;

import net.lt_schmiddy.bettercrossbow.ModEntry;

@Mixin(net.minecraft.enchantment.InfinityEnchantment.class)
public class InfinityEnchantOnCrossbowMixin extends Enchantment {


	protected InfinityEnchantOnCrossbowMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack) 
        || (
			ModEntry.infinityOnCrossbow 
			&& (
                stack.getItem() instanceof RangedWeaponItem 
                && (
                    EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack) == 0
                    || !ModEntry.infinityQuickchargeConflict
                )
                && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) == 0
            )
        );
    }
}
