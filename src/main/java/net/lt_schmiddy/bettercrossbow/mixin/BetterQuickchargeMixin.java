package net.lt_schmiddy.bettercrossbow.mixin;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;


@Mixin(net.minecraft.enchantment.QuickChargeEnchantment.class)
public class BetterQuickchargeMixin extends Enchantment {
	protected BetterQuickchargeMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
		//
	}

	@Overwrite
	public int getMaxLevel() {
		// System.out.println("ModEntry.maxQuickChargeLevel : " + ModEntry.maxQuickChargeLevel);
		return ConfigHandler.config.maxQuickChargeLevel;
	}

	@Overwrite
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack)
		&& (
			EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) == 0
			|| !ConfigHandler.config.infinityQuickchargeConflict
		);
    }
}
