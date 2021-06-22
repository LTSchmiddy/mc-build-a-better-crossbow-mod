package net.lt_schmiddy.bettercrossbow.mixin;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import net.lt_schmiddy.bettercrossbow.ModEntry;


@Mixin(net.minecraft.enchantment.QuickChargeEnchantment.class)
public class BetterQuickchargeMixin extends Enchantment {
	protected BetterQuickchargeMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
		//TODO Auto-generated constructor stub
	}

	@Override
	public int getMaxLevel() {
		// System.out.println("ModEntry.maxQuickChargeLevel : " + ModEntry.maxQuickChargeLevel);
		return ModEntry.maxQuickChargeLevel;
	}

	@Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack)
		&& (
			EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) == 0
			|| !ModEntry.infinityQuickchargeConflict
		);
    }
}
