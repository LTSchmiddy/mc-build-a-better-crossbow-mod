package net.lt_schmiddy.bettercrossbow.mixin;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.entity.projectile.ProjectileEntity;

// import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.projectile.FireworkRocketEntity;

@Mixin(FireworkRocketEntity.class)
public abstract class MoreFireworksDamageMixin extends Entity implements FlyingItemEntity {

    MoreFireworksDamageMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    private static float powerMult = 1.0f;
    private static int minChargesForDestruction = 4;

    @Shadow
    private static TrackedData<ItemStack> ITEM;
    
    @Shadow
    private static TrackedData<OptionalInt> SHOOTER_ENTITY_ID;
    
    @Shadow
    private static TrackedData<Boolean> SHOT_AT_ANGLE;

    @Shadow
    private int life;

    @Shadow
    private int lifeTime;

    @Shadow
    private LivingEntity shooter;


    public FireworkRocketEntity targetThis() {
        return (FireworkRocketEntity)(Entity)this;
    }
    // private static float damageMult = 50.0f;

    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    void stronger_explode(CallbackInfo info) {
        info.cancel();

        // System.out.println("Using overridden rocket explosion logic.");

        ItemStack itemStack = (ItemStack) this.dataTracker.get(ITEM);
        CompoundTag compoundTag = itemStack.isEmpty() ? null : itemStack.getSubTag("Fireworks");
        ListTag listTag = compoundTag != null ? compoundTag.getList("Explosions", 10) : null;
        int charges = 0;
        if (listTag != null && !listTag.isEmpty()) {
            charges = listTag.size();
        }
        
        if (charges > 0){
            // this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 4.0F, Explosion.DestructionType.BREAK);
            this.world.createExplosion(
                this, 
                this.getX(), 
                this.getY(), 
                this.getZ(), 
                4.0F * (0.2f * (float)charges) * powerMult, 
                charges >= minChargesForDestruction ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE
            );
        }
    }
}


    /* Old rocket damage calculations

            if (this.shooter != null) {
                float g = f * (float) Math.sqrt((5.0D - (double) this.distanceTo(targetThis().getOwner())) / 5.0D);
                this.shooter.damage(DamageSource.firework(targetThis(), targetThis().getOwner()), g);
            }

            double d = 5.0D;
            Vec3d vec3d = this.getPos();
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class,
                    this.getBoundingBox().expand(5.0D));
            Iterator var9 = list.iterator();

            while (true) {
                LivingEntity livingEntity;
                do {
                    do {
                        if (!var9.hasNext()) {
                            return;
                        }

                        livingEntity = (LivingEntity) var9.next();
                    } while (livingEntity == this.shooter);
                } while (this.squaredDistanceTo(livingEntity) > 25.0D);

                boolean bl = false;

                for (int i = 0; i < 2; ++i) {
                    Vec3d vec3d2 = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5D * (double) i),
                            livingEntity.getZ());
                    HitResult hitResult = this.world.raycast(new RaycastContext(vec3d, vec3d2,
                            RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
                    if (hitResult.getType() == HitResult.Type.MISS) {
                        bl = true;
                        break;
                    }
                }

                if (bl) {
                    float g = f * (float) Math.sqrt((5.0D - (double) this.distanceTo(livingEntity)) / 5.0D);
                    livingEntity.damage(DamageSource.firework(targetThis(), targetThis().getOwner()), g);
                }
            }
        }
    */

    /*
     * Damage increase method:
     * 
     * @ModifyVariable( method="explode()V", // at=@At( // value = "INVOKE", //
     * target =
     * "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
     * // ordinal=1 // ), at=@At(value="STORE", ordinal=0), name="g", ordinal=0
     * 
     * ) private float explosionDamageIncrease(float g){
     * 
     * System.out.println("Start Damage: " + g);
     * System.out.println("Rocket explosion damage increased by " + damageMult +
     * " times." ); g = (g + 1) * damageMult; System.out.println("End Damage: " +
     * g); return g; }
     */