package net.lt_schmiddy.bettercrossbow;

import net.fabricmc.api.ModInitializer;
import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;
import net.minecraft.enchantment.Enchantments;


public class ModEntry implements ModInitializer {

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ConfigHandler.load();

		System.out.println("Loading the 'Build a Better Crossbow' mod...");
		loadConfig();

		System.out.println("Quickcharge Max: " + Enchantments.QUICK_CHARGE.getMaxLevel());
        System.out.println("Multishot Max: " + Enchantments.MULTISHOT.getMaxLevel());
	}

	public void loadConfig() {
		
	}
}
