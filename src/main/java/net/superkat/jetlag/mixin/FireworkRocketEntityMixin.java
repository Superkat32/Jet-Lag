package net.superkat.jetlag.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.contrail.JetLagPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.superkat.jetlag.config.JetLagConfig.getInstance;

@Environment(EnvType.CLIENT)
@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {
    @Shadow @Nullable private LivingEntity shooter;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    public void jetlag$setPlayerRocketBoosting(CallbackInfo ci) {
        if(this.shooter != null) {
            if(this.shooter instanceof ClientPlayerEntity player) {
                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                jetLagPlayer.jetlag$setRocketBoosting(true);
            }
        }
    }

    @Inject(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;hasExplosionEffects()Z"))
    public void jetlag$removePlayerRocketBoosting(byte status, CallbackInfo ci) {
        if(this.shooter != null) {
            if(this.shooter instanceof ClientPlayerEntity player) {
                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                jetLagPlayer.jetlag$setRocketBoosting(false);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), cancellable = true)
    public void jetlag$replaceFireworkParticles(CallbackInfo ci) {
        FireworkRocketEntity self = (FireworkRocketEntity) (Object) this;

        //firework particles
        if(getInstance().alwaysUseAltFireworkParticles) {
            spawnAltFireworkParticles(0.05f, 0.5f);
            spawnAltFireworkParticles(0.07f, 0.7f);
            ci.cancel();
        } else if (getInstance().altFireworkParticles && this.shooter != null) {
            spawnAltFireworkParticles(0.05f, 0.5f);
            spawnAltFireworkParticles(0.07f, 0.7f);
            ci.cancel();
        }

        //wind particles
        if(getInstance().windGusts && self.age == 2 && this.shooter != null) {
            int windAmount = self.getWorld().random.nextBetween(4, 7);
            for (int i = 0; i < windAmount; i++) {
                DefaultParticleType windParticle = getWindParticleType();
                self.getWorld().addParticle(windParticle, self.getX() + self.getWorld().random.nextBetween(-2, 2), self.getY() + self.getWorld().random.nextBetween(-2, 2), self.getZ() + self.getWorld().random.nextBetween(-2, 2), self.getWorld().random.nextGaussian() * 0.07, self.getVelocity().y * 0.7, self.getWorld().random.nextGaussian() * 0.07);
            }
        }
    }

    private void spawnAltFireworkParticles(float xzVelAdjust, float yVelAdjust) {
        FireworkRocketEntity self = (FireworkRocketEntity) (Object) this;
        self.getWorld().addParticle(JetLagMain.FIREWORKPARTICLE, self.getX(), self.getY(), self.getZ(), self.getWorld().random.nextGaussian() * xzVelAdjust, self.getVelocity().y * yVelAdjust, self.getWorld().random.nextGaussian() * xzVelAdjust);
    }

    private DefaultParticleType getWindParticleType() {
        FireworkRocketEntity self = (FireworkRocketEntity) (Object) this;
        if(getInstance().useMinecraftWindGusts) {
            return ParticleTypes.GUST;
        }
        int wind = self.getWorld().random.nextBetween(0, JetLagMain.windParticles.size() - 1);
        return JetLagMain.windParticles.get(wind);
    }
}
