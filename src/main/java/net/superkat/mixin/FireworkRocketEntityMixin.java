package net.superkat.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.superkat.JetLagMain;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.superkat.JetLagMain.LOGGER;

@Environment(EnvType.CLIENT)
@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity {

    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow @Nullable private LivingEntity shooter;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    public void init(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        //Works this way to prevent the game from crashing if a normal firework is used on a block
        try {
            if (this.shooter != null) {
                if (this.shooter.isPlayer() && this.shooter.isFallFlying()) {
//                    LOGGER.info("Player used firework in flight");
                    world.addParticle(JetLagMain.FIREWORKPARTICLE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
                    world.addParticle(JetLagMain.FIREWORKPARTICLE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.07, this.getVelocity().y * 0.7, this.random.nextGaussian() * 0.07);
                }
            }
            else {
                this.world.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
            }
        } catch (Exception e) {
            if(this.shooter == null) {
                LOGGER.info("null");
                this.world.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
            } else {
                this.world.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
                LOGGER.warn("Something went wrong with the particles");
                LOGGER.warn("Exception: " + e);
            }
        }
    }
}
