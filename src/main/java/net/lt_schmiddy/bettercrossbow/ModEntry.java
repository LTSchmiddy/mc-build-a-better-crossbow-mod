package net.lt_schmiddy.bettercrossbow;

import net.fabricmc.api.ModInitializer;


public class ModEntry implements ModInitializer {
	public static boolean powerOnCrossbow = true;
	public static boolean punchOnCrossbow = true;
	public static boolean flamesOnCrossbow = true;
	public static boolean infinityOnCrossbow = true;
	public static boolean explodingFireworks = true;
	public static boolean piercingSpeedUpArrows = true;
	public static boolean cantRecoverPiercingMultishotArrows = true;


	public static int maxQuickChargeLevel = 5;
	public static int maxMultishotLevel = 4;

	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Loading the 'Build a Better Crossbow' mod...");
	}

	public static void loadConfig() {

	}
}