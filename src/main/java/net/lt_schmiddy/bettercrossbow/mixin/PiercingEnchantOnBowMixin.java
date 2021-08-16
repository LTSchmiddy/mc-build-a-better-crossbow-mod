package net.lt_schmiddy.bettercrossbow.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;

import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;

@Mixin(net.minecraft.enchantment.PiercingEnchantment.class)
public class PiercingEnchantOnBowMixin extends Enchantment {

	protected PiercingEnchantOnBowMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
    public boolean isAcceptableItem(ItemStack stack) {
        return (
			super.isAcceptableItem(stack)
			|| (
				ConfigHandler.config.bow.piercingOnBow 
				&& stack.getItem() instanceof BowItem
			)
		) && EnchantmentHelper.getLevel(Enchantments.POWER, stack) == 0;
    }
}
