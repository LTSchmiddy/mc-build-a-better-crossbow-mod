package net.lt_schmiddy.bettercrossbow.config;

public class Config {
	public static class CrossbowConfig {
		public boolean powerOnCrossbow = false;
		public boolean punchOnCrossbow = true;
		public boolean flameOnCrossbow = true;
		public boolean infinityOnCrossbow = false;
		
		public boolean infinityQuickchargeConflict = true;
		public boolean cantRecoverPiercingMultishotArrows = true;
		
		public double crossbowBaseDamageIncrease = 1.5D;

		public double powerDamageIncreasePerLevel = 0.5D;
		public float piercingSpeedIncreasePerLevel = 0.5f;
		public float piercingBaseDamageReductionPerLevel = 0.3f;
	} public CrossbowConfig crossbow = new CrossbowConfig();

	public static class BowConfig {
		public boolean tweakBow = false;

		public double powerDamageIncreasePerLevel = 0.5D;

		public boolean piercingOnBow = false;
		public float piercingSpeedIncreasePerLevel = 0.5f;
		public float piercingBaseDamageReductionPerLevel = 0.1f;
	} public BowConfig bow = new BowConfig();

	public static class PiercingConfig {
		public boolean piercingSpeedUpArrows = true;
	} public PiercingConfig piercing = new PiercingConfig();

	public static class MiscConfig {
		public int maxQuickChargeLevel = 5;
		public int maxMultishotLevel = 4;
	} public MiscConfig misc = new MiscConfig();
}
