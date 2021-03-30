package net.lt_schmiddy.bettercrossbow;

import net.fabricmc.api.ModInitializer;


public class ModEntry implements ModInitializer {
	private static SimpleConfig CONFIG;

	// Custom config provider, returnes the default config content
	// if the custom provider is not specified SimpleConfig will create an empty file instead

	public static boolean powerOnCrossbow = false;
	public static boolean punchOnCrossbow = true;
	public static boolean flameOnCrossbow = true;
	public static boolean infinityOnCrossbow = false;
	public static boolean piercingSpeedUpArrows = true;
	public static boolean cantRecoverPiercingMultishotArrows = true;
	public static boolean infinityQuickchargeConflict = true;

	
	public static int maxQuickChargeLevel = 5;
	public static int maxMultishotLevel = 4;

    public static float piercingSpeedIncreasePerLevel = 0.125f;
    public static double crossbowBaseDamageIncrease = 2.0D;


    
	private String provider( String filename ) {
		return "#Default Config:\n";
	  }
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Loading the 'Build a Better Crossbow' mod...");
		loadConfig();
	}

	public void loadConfig() {
		CONFIG = SimpleConfig.of( "bettercrossbow" ).provider( this::provider ).request();

		powerOnCrossbow = CONFIG.getOrDefault("b_powerOnCrossbow", powerOnCrossbow);
		punchOnCrossbow = CONFIG.getOrDefault("b_punchOnCrossbow", punchOnCrossbow);
		flameOnCrossbow = CONFIG.getOrDefault("b_flameOnCrossbow", flameOnCrossbow);
		infinityOnCrossbow = CONFIG.getOrDefault("b_infinityOnCrossbow", infinityOnCrossbow);
		piercingSpeedUpArrows = CONFIG.getOrDefault("b_piercingSpeedUpArrows", piercingSpeedUpArrows);
		cantRecoverPiercingMultishotArrows = CONFIG.getOrDefault("b_cantRecoverPiercingMultishotArrows", cantRecoverPiercingMultishotArrows);
		infinityQuickchargeConflict = CONFIG.getOrDefault("b_infinityQuickchargeConflict", infinityQuickchargeConflict);


		maxQuickChargeLevel = CONFIG.getOrDefault("i_maxQuickChargeLevel", maxQuickChargeLevel);
		maxMultishotLevel = CONFIG.getOrDefault("i_maxMultishotLevel", maxMultishotLevel);

		piercingSpeedIncreasePerLevel = (float)CONFIG.getOrDefault("f_piercingSpeedIncreasePerLevel", piercingSpeedIncreasePerLevel);
		crossbowBaseDamageIncrease = CONFIG.getOrDefault("f_crossbowBaseDamageIncrease", crossbowBaseDamageIncrease);

		CONFIG.update();
	}
}
