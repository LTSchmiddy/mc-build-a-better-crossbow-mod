package net.lt_schmiddy.bettercrossbow.mixin;

import java.util.function.Consumer;
import java.util.function.Predicate;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

import net.lt_schmiddy.bettercrossbow.config.ConfigHandler;

@Mixin(net.minecraft.item.BowItem.class)
public class BowEnchantmentHandlerMixin extends RangedWeaponItem implements Vanishable {

    public BowEnchantmentHandlerMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "onStoppedUsing", cancellable = true)
    public void modded_bow(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (!ConfigHandler.config.bow.tweakBow) {
            return;
        }
        info.cancel();

        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) user;
            boolean bl = playerEntity.getAbilities().creativeMode
                    || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getArrowType(stack);
            if (!itemStack.isEmpty() || bl) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }

                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                float f = getPullProgress(i);
                if (!((double) f < 0.1D)) {
                    boolean bl2 = bl && itemStack.isOf(Items.ARROW);
                    if (!world.isClient) {
                        ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem
                                ? itemStack.getItem()
                                : Items.ARROW);
                        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack,
                                playerEntity);

                        float speed_mult = 1f;

                        int piercingLevel = EnchantmentHelper.getLevel(Enchantments.PIERCING, stack);
                        if (piercingLevel > 0) {
                            if (ConfigHandler.config.piercing.piercingSpeedUpArrows) {
                                speed_mult += ConfigHandler.config.bow.piercingSpeedIncreasePerLevel * piercingLevel;
                            }
                            persistentProjectileEntity.setPierceLevel((byte) piercingLevel);
                        }
                        persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(),
                                playerEntity.getYaw(), 0.0F, f * 3.0F * speed_mult, 1.0F);


                        if (f == 1.0F) {
                            persistentProjectileEntity.setCritical(true);
                        }

                        int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (powerLevel > 0) {
                            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage()
                                    + (double) powerLevel * ConfigHandler.config.bow.powerDamageIncreasePerLevel
                                    + ConfigHandler.config.bow.powerDamageIncreasePerLevel);
                        }

                        if (piercingLevel > 0 && ConfigHandler.config.piercing.piercingSpeedUpArrows) {
                            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage()
                                    - (piercingLevel * ConfigHandler.config.bow.piercingBaseDamageReductionPerLevel));
                        }

                        int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (punchLevel > 0) {
                            persistentProjectileEntity.setPunch(punchLevel);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            persistentProjectileEntity.setOnFireFor(100);
                        }

                        stack.damage(1, (LivingEntity) playerEntity, (Consumer<LivingEntity>) ((p) -> {
                            p.sendToolBreakStatus(playerEntity.getActiveHand());
                        }));
                        if (bl2 || playerEntity.getAbilities().creativeMode
                                && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }

                        world.spawnEntity(persistentProjectileEntity);
                    }

                    world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                            1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            playerEntity.getInventory().removeOne(itemStack);
                        }
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Shadow
    private static float getPullProgress(int i) {
        throw new AbstractMethodError();
    }

    @Shadow
    public Predicate<ItemStack> getProjectiles() {
        throw new AbstractMethodError();
    }

    @Shadow
    public int getRange() {
        throw new AbstractMethodError();
    }

}
