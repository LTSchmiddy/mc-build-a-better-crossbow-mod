package net.lt_schmiddy.bettercrossbow.config;

public class Config {
    public boolean powerOnCrossbow = false;
	public boolean punchOnCrossbow = true;
	public boolean flameOnCrossbow = true;
	public boolean piercingOnBow = true;
	public boolean infinityOnCrossbow = false;
	public boolean piercingSpeedUpArrows = true;
	public boolean cantRecoverPiercingMultishotArrows = true;
	public boolean infinityQuickchargeConflict = true;
	
	public int maxQuickChargeLevel = 5;
	public int maxMultishotLevel = 4;

    public float piercingSpeedIncreasePerLevelCrossbow = 0.125f;
    public float piercingSpeedIncreasePerLevelBow = 0.125f;
    public float piercingBaseDamageReductionPerLevel = 0.0f;
	
    public double crossbowBaseDamageIncrease = 2.0D;
}
