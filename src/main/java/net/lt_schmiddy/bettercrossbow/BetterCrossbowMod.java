package net.lt_schmiddy.bettercrossbow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;
import net.minecraft.enchantment.Enchantments;


public class BetterCrossbowMod implements ModInitializer {

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("reload_better_crossbow").executes(context -> {
				loadConfig();
                System.out.println("Better Crossbow config reloaded...");
                return 1;
            }));
        });

		System.out.println("Loading the 'Build a Better Crossbow' mod...");
		loadConfig();

		System.out.println("Quickcharge Max: " + Enchantments.QUICK_CHARGE.getMaxLevel());
        System.out.println("Multishot Max: " + Enchantments.MULTISHOT.getMaxLevel());
	}

	public void loadConfig() {
		ConfigHandler.load();
		ConfigHandler.save();
	}
}
