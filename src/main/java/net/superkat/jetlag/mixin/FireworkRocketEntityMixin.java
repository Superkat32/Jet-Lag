package net.superkat.jetlag.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity {

    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow @Nullable private LivingEntity shooter;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    public void init(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
//        if(INSTANCE.instance().altFireworkParticles || INSTANCE.instance().windGusts || INSTANCE.instance().alwaysUseAltFireworkParticles) {
//            //Works this way to prevent the game from crashing if a normal firework is used on a block
//            try {
//                if (INSTANCE.instance().alwaysUseAltFireworkParticles) {
//                    instance.addParticle(JetLagMain.FIREWORKPARTICLE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//                    instance.addParticle(JetLagMain.FIREWORKPARTICLE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.07, this.getVelocity().y * 0.7, this.random.nextGaussian() * 0.07);
//                }
//                else if (this.shooter != null) {
//                    if (this.shooter.isPlayer() && this.shooter.isFallFlying()) {
//                        if(INSTANCE.instance().altFireworkParticles) {
//                            instance.addParticle(JetLagMain.FIREWORKPARTICLE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//                            instance.addParticle(JetLagMain.FIREWORKPARTICLE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.07, this.getVelocity().y * 0.7, this.random.nextGaussian() * 0.07);
//                        } else {
//                            instance.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//                        }
//                        if(this.age == 2 && INSTANCE.instance().windGusts) {
//                            int windAmount = this.random.nextBetween(4, 7);
//                            for (int i = 0; i < windAmount; i++) {
//                                int windgust = this.random.nextBetween(1, 3);
//                                if(windgust == 1) {
//                                    instance.addParticle(JetLagMain.WIND1, this.getX() + this.random.nextBetween(-2, 2), this.getY() + this.random.nextBetween(-2, 2), this.getZ() + this.random.nextBetween(-2, 2), this.random.nextGaussian() * 0.07, this.getVelocity().y * 0.7, this.random.nextGaussian() * 0.07);
//                                } else if (windgust == 2) {
//                                    instance.addParticle(JetLagMain.WIND2, this.getX() + this.random.nextBetween(-2, 2), this.getY() + this.random.nextBetween(-2, 2), this.getZ() + this.random.nextBetween(-2, 2), this.random.nextGaussian() * 0.07, this.getVelocity().y * 0.7, this.random.nextGaussian() * 0.07);
//                                }else if (windgust == 3) {
//                                    instance.addParticle(JetLagMain.WIND3, this.getX() + this.random.nextBetween(-2, 2), this.getY() + this.random.nextBetween(-2, 2), this.getZ() + this.random.nextBetween(-2, 2), this.random.nextGaussian() * 0.07, this.getVelocity().y * 0.7, this.random.nextGaussian() * 0.07);
//                                }
//                            }
//                        }
//                    }
//                }
//                else {
//                    instance.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//                }
//            } catch (Exception e) {
//                if(this.shooter == null) {
//                    LOGGER.info("null");
//                    instance.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//                } else {
//                    instance.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//                    LOGGER.warn("Something went wrong with the particles");
//                    LOGGER.warn("Exception: " + e);
//                }
//            }
//        } else {
//            instance.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
//        }
    }
}
