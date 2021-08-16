package net.lt_schmiddy.bettercrossbow.mixin;

import java.util.List;
import java.util.Random;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
// import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;

@Mixin(net.minecraft.item.CrossbowItem.class)
public abstract class CrossbowEnchantmentHandlerMixin extends RangedWeaponItem {

    private static final float spreadMin = -10.0F;
    private static final float spreadMax = 10.0F;

    public CrossbowEnchantmentHandlerMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    private static boolean loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile,
            boolean simulated, boolean creative) {
        throw new AbstractMethodError();
    }

    @Shadow
    public static List<ItemStack> getProjectiles(ItemStack stack) {
        throw new AbstractMethodError();
    }

    @Shadow
    public static float[] getSoundPitches(Random random) {
        throw new AbstractMethodError();
    }

    @Shadow
    public static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile,
            float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        throw new AbstractMethodError();
    }

    @Shadow
    public static void postShoot(World world, LivingEntity entity, ItemStack stack) {
        throw new AbstractMethodError();
    }

    // Apply Enchantments to Fired Arrows:
    @Inject(at = @At("RETURN"), method = "createArrow(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;", cancellable = true)
    private static void apply_enchantments_to_arrows(World world, LivingEntity entity, ItemStack crossbow,
            ItemStack arrow, CallbackInfoReturnable<PersistentProjectileEntity> info) {
        PersistentProjectileEntity arrowEntity = info.getReturnValue();

        // Adjusting Base Damage:
        arrowEntity.setDamage(arrowEntity.getDamage() + ConfigHandler.config.crossbow.crossbowBaseDamageIncrease);

        int piercingLevel = EnchantmentHelper.getLevel(Enchantments.PIERCING, crossbow);
        if (piercingLevel > 0 && ConfigHandler.config.piercing.piercingSpeedUpArrows) {
            arrowEntity.setDamage(arrowEntity.getDamage()
                    - (piercingLevel * ConfigHandler.config.crossbow.piercingBaseDamageReductionPerLevel));
        }

        // Adding effects for enchantments to arrows. This is straight out of the
        // BowItem firing code.
        // - Power
        int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, crossbow);
        if (powerLevel > 0 && ConfigHandler.config.crossbow.powerOnCrossbow) {
            arrowEntity.setDamage(arrowEntity.getDamage()
                    + (double) powerLevel * ConfigHandler.config.crossbow.powerDamageIncreasePerLevel
                    + ConfigHandler.config.crossbow.powerDamageIncreasePerLevel);
        }
        // - Punch
        int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, crossbow);
        if (punchLevel > 0 && ConfigHandler.config.crossbow.punchOnCrossbow) {
            arrowEntity.setPunch(punchLevel);
        }
        // - Flame
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, crossbow) > 0
                && ConfigHandler.config.crossbow.flameOnCrossbow) {
            arrowEntity.setOnFireFor(100);
        }
    }

    // Changing loaded arrow count:
    @Inject(at = @At("HEAD"), method = "loadProjectiles", cancellable = true)
    private static void load_more_projectiles(LivingEntity shooter, ItemStack projectile,
            CallbackInfoReturnable<Object> info) {
        info.cancel();

        int i = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, projectile);
        // int j = i == 0 ? 1 : 3;
        int j = 1 + i * 2;
        boolean bl = (shooter instanceof PlayerEntity && (((PlayerEntity) shooter).getAbilities().creativeMode
                || (EnchantmentHelper.getLevel(Enchantments.INFINITY, projectile) > 0
                        && ConfigHandler.config.crossbow.infinityOnCrossbow)));
        ItemStack itemStack = shooter.getArrowType(projectile);
        ItemStack itemStack2 = itemStack.copy();

        for (int k = 0; k < j; ++k) {
            if (k > 0) {
                itemStack = itemStack2.copy();
            }

            if (itemStack.isEmpty() && bl) {
                itemStack = new ItemStack(Items.ARROW);
                itemStack2 = itemStack.copy();
            }

            if (!loadProjectile(shooter, projectile, itemStack, k > 0, bl)) {
                info.setReturnValue(false);
                return;
            }
        }

        info.setReturnValue(true);
        return;
    }

    // Rewriting arrow spread code for improved Multishot:
    @Inject(at = @At("HEAD"), method = "shootAll", cancellable = true)
    private static void shootAll_override(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed,
            float divergence, CallbackInfo info) {

        List<ItemStack> list = getProjectiles(stack);
        float[] fs = getSoundPitches(entity.getRandom());

        if (ConfigHandler.config.piercing.piercingSpeedUpArrows) {
            int piercingLevel = EnchantmentHelper.getLevel(Enchantments.PIERCING, stack);

            if (piercingLevel > 0) {
                float piercing_mod = ((float) (piercingLevel)
                        * ConfigHandler.config.crossbow.piercingSpeedIncreasePerLevel);
                // System.out.println("piercing_mod: " + piercing_mod);
                // System.out.println("qf_mod: " + qf_mod);
                speed += speed * (piercing_mod);
            }
        }
        // Piercing increases arrow speed:

        float spreadWidth = spreadMax - spreadMin;
        int spreadingArrowCount = list.size() - 1;

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemStack = (ItemStack) list.get(i);
            boolean bl = entity instanceof PlayerEntity && (((PlayerEntity) entity).getAbilities().creativeMode
                    || (EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0
                            && ConfigHandler.config.crossbow.infinityOnCrossbow)
                    || (ConfigHandler.config.crossbow.cantRecoverPiercingMultishotArrows
                            && (EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 0
                                    || EnchantmentHelper.getLevel(Enchantments.PIERCING, stack) > 0)));

            if (!itemStack.isEmpty()) {
                if (i == 0 || spreadingArrowCount < 2) {
                    shoot(world, entity, hand, stack, itemStack, fs[i % 3], bl, speed, divergence, 0.0F);
                } else {
                    int spreadingArrow = i - 1;
                    float spread = spreadMin
                            + spreadWidth * ((float) (spreadingArrow) / (float) (spreadingArrowCount - 1));
                    shoot(world, entity, hand, stack, itemStack, fs[i % 3], bl, speed, divergence, spread);
                }
            }
        }

        postShoot(world, entity, stack);

    }

}
