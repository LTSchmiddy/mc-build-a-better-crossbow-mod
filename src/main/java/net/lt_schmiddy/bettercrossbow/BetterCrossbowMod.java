package net.lt_schmiddy.bettercrossbow;

import net.fabricmc.api.ModInitializer;

public class BetterCrossbowMod implements ModInitializer {

	private static SimpleConfig CONFIG;

	// Custom config provider, returnes the default config content
	// if the custom provider is not specified SimpleConfig will create an empty file instead

	public static boolean powerOnCrossbow = true;
	public static boolean punchOnCrossbow = true;
	public static boolean flameOnCrossbow = true;
	public static boolean infinityOnCrossbow = true;
	public static boolean explodingFireworks = true;
	public static boolean piercingSpeedUpArrows = true;
	public static boolean cantRecoverPiercingMultishotArrows = true;
	public static boolean infinityQuickchargeConflict = true;

	
	public static int maxQuickChargeLevel = 5;
	public static int maxMultishotLevel = 4;
	public static int fireworkMinChargesForBreaking = 4;
	
    public static float fireworkExplosionMult = 1.0f;
    public static float piercingSpeedIncreasePerLevel = 0.25f;
    public static float piercingSpeedReductionPerQuickchargeLevel = 0.1f;
    
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

		powerOnCrossbow = CONFIG.getOrDefault("b_powerOnCrossbow", true);
		punchOnCrossbow = CONFIG.getOrDefault("b_punchOnCrossbow", true);
		flameOnCrossbow = CONFIG.getOrDefault("b_flameOnCrossbow", true);
		infinityOnCrossbow = CONFIG.getOrDefault("b_infinityOnCrossbow", true);
		explodingFireworks = CONFIG.getOrDefault("b_explodingFireworks", true);
		piercingSpeedUpArrows = CONFIG.getOrDefault("b_piercingSpeedUpArrows", true);
		cantRecoverPiercingMultishotArrows = CONFIG.getOrDefault("b_cantRecoverPiercingMultishotArrows", true);
		infinityQuickchargeConflict = CONFIG.getOrDefault("b_infinityQuickchargeConflict", true);


		maxQuickChargeLevel = CONFIG.getOrDefault("i_maxQuickChargeLevel", 5);
		maxMultishotLevel = CONFIG.getOrDefault("i_maxMultishotLevel", 4);
		fireworkMinChargesForBreaking = CONFIG.getOrDefault("i_fireworkMinChargesForBreaking", 4);

		fireworkExplosionMult = (float)CONFIG.getOrDefault("f_fireworkExplosionMult", 1.0f);
		piercingSpeedIncreasePerLevel = (float)CONFIG.getOrDefault("f_piercingSpeedIncreasePerLevel", 0.25f);
		piercingSpeedReductionPerQuickchargeLevel = (float)CONFIG.getOrDefault("f_piercingSpeedReductionPerQuickchargeLevel", 0.1f);

		CONFIG.update();
	}
}
