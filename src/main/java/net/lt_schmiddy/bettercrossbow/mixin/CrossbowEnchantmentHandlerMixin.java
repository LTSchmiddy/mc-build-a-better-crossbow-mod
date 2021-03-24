package net.lt_schmiddy.bettercrossbow.mixin;

import java.util.List;
import java.util.Random;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
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


@Mixin(net.minecraft.item.CrossbowItem.class)
public abstract class CrossbowEnchantmentHandlerMixin extends RangedWeaponItem {
	public CrossbowEnchantmentHandlerMixin(Settings settings) {
        super(settings);
    }
    // Accessors:
    @Invoker("loadProjectile")
    public static boolean invokeLoadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative) {
        throw new AbstractMethodError();
    }
    
    @Invoker("getProjectiles")
    public static List<ItemStack> invokeGetProjectiles(ItemStack stack) {
        throw new AbstractMethodError();
    }
    
    @Invoker("getSoundPitches")
    public static float[] invokeGetSoundPitches(Random random) {
        throw new AbstractMethodError();
    }

    @Invoker("shoot")
    public static void invokeShoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        throw new AbstractMethodError();
    }

    @Invoker("postShoot")
    public static void invokePostShoot(World world, LivingEntity entity, ItemStack stack) {
        throw new AbstractMethodError();
    }

    // Apply Enchantments to Fired Arrows:
    @Inject(
        at = @At("RETURN"),
        method = "createArrow(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;",
        // method = "createArrow",
        // locals = LocalCapture.CAPTURE_FAILEXCEPTION,
        cancellable = true
    )
	private static void apply_enchantments_to_arrows (
        World world, 
        LivingEntity entity, 
        ItemStack crossbow, 
        ItemStack arrow, 
        CallbackInfoReturnable<PersistentProjectileEntity> info 
        // PersistentProjectileEntity persistentProjectileEntity
    ) {
        PersistentProjectileEntity arrowEntity = info.getReturnValue();
        
        //Increasing Base Damage:
        // arrowEntity.setDamage(arrowEntity.getDamage() + 1.0D); 

        // Adding effects for enchantments to arrows. This is straight out of the BowItem firing code.
        // - Power
        int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, crossbow);
        if (powerLevel > 0) {
            arrowEntity.setDamage(arrowEntity.getDamage() + (double)powerLevel * 0.5D + 0.5D);
        }
        // - Punch
        int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, crossbow);
        if (punchLevel > 0) {
            arrowEntity.setPunch(punchLevel);
        }
        // - Flame
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, crossbow) > 0) {
            arrowEntity.setOnFireFor(100);
        }

        // Piercing increases arrow damage:
        int piercingLevel = EnchantmentHelper.getLevel(Enchantments.PIERCING, crossbow);
        if (piercingLevel > 0) {
            arrowEntity.setDamage(arrowEntity.getDamage() * (1 + (float)(piercingLevel) * 0.05));
        }

        // System.out.println(arrowEntity.getDamage());
	}

    // Changing loaded arrow count:
    @Inject(
        // at = @At(value="", target="net/minecraft/entity/LivingEntity/getArrowType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),
        at = @At("HEAD"),
        method = "loadProjectiles",
        // locals = LocalCapture.CAPTURE_FAILEXCEPTION,
        cancellable = true
    )
    private static void load_more_projectiles(LivingEntity shooter, ItemStack projectile, CallbackInfoReturnable<Object> info){
        // System.out.println("Using overridden loadProjectiles");
        info.cancel();

        int i = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, projectile);
        // int j = i == 0 ? 1 : 3;
        int j = 1 + i * 2;
        boolean bl = shooter instanceof PlayerEntity && (((PlayerEntity)shooter).abilities.creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, projectile) > 0);
        ItemStack itemStack = shooter.getArrowType(projectile);
        ItemStack itemStack2 = itemStack.copy();
  
        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemStack = itemStack2.copy();
            }

            if (itemStack.isEmpty() && bl) {
                itemStack = new ItemStack(Items.ARROW);
                itemStack2 = itemStack.copy();
            }

            
            if (!invokeLoadProjectile(shooter, projectile, itemStack, k > 0, bl)) {
                info.setReturnValue(false);
                return;
            }
        }
        
        info.setReturnValue(true);
        return;
    }


    // Rewriting arrow spread code for improved Multishot:
    @Inject(
        at = @At("HEAD"),
        method = "shootAll",
        // locals = LocalCapture.CAPTURE_FAILEXCEPTION,
        cancellable = true
    )
    private static void shootAll_override(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence, CallbackInfo info) {
        // System.out.println("Using overridden shootAll");
        
        List<ItemStack> list = invokeGetProjectiles(stack);
        float[] fs = invokeGetSoundPitches(entity.getRandom());
        
        //Piercing increases arrow speed:
        int piercingLevel = EnchantmentHelper.getLevel(Enchantments.PIERCING, stack);
        if (piercingLevel > 0) {
            speed = speed * (1 + (float)(piercingLevel)/2);
        }
        
        // Store these values in a config later:
        float spreadMin = -10.0F;
        float spreadMax = 10.0F;

        float spreadWidth = spreadMax - spreadMin;
        int spreadingArrowCount = list.size() - 1;
        // System.out.println("spreadingArrowCount: " + spreadingArrowCount);
        // System.out.println("spreadMin: " + spreadMin);
        // System.out.println("spreadMax: " + spreadMax);
        // System.out.println("spreadWitdh: " + spreadWidth);

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemStack = (ItemStack)list.get(i);
            boolean bl = 
                entity instanceof PlayerEntity 
                && (
                    ((PlayerEntity)entity).abilities.creativeMode 
                    || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0
                    || EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 0
                    || EnchantmentHelper.getLevel(Enchantments.PIERCING, stack) > 0
                );

           if (!itemStack.isEmpty()) {
                if (i == 0 || spreadingArrowCount < 2) {
                    invokeShoot(world, entity, hand, stack, itemStack, fs[i % 3], bl, speed, divergence, 0.0F);
                } else {
                    int spreadingArrow = i - 1;
                    float spread = spreadMin + spreadWidth * ((float)(spreadingArrow)/(float)(spreadingArrowCount - 1));
                    // System.out.println("spreadingArrow: " + spreadingArrow);
                    // System.out.println("spread: " + spread);
                    invokeShoot(world, entity, hand, stack, itemStack, fs[i % 3], bl, speed, divergence, spread);
                }
                
                // } else if (i == 1) {
                //     invokeShoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, -10.0F);
                // } else if (i == 2) {
                //     invokeShoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 10.0F);
                // }
            }
        }
        
        invokePostShoot(world, entity, stack);
        info.cancel();
    }

}
