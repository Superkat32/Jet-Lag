package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class FireworkParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    FireworkParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
//        this.velocityMultiplier = 0.6F;
        this.spriteProvider = spriteProvider;
        this.maxAge = 80 + this.random.nextBetween(0, 40);
        this.scale = 0.15F + this.random.nextFloat() / 2;
        this.velocityX = velocityX / 3;
        this.velocityY = velocityY / 3;
        this.velocityZ = velocityZ / 3;
        this.x = x;
        this.y = y;
        this.z = z;
        this.collidesWithWorld = true;
        this.alpha = 0.85F;
        this.setSpriteForAge(spriteProvider);
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge || this.scale <= 0) {
            this.markDead();
        } else {
//            this.setSpriteForAge(this.spriteProvider);
//            velocityX *= 1.025;
//            velocityY *= 1.025;
//            velocityZ *= 1.025;
            if(this.age >= this.maxAge * 0.85) {
                this.scale *= 0.92;
                this.alpha *= 0.90;
            }
            this.move(this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            FireworkParticle speedlinesParticle = new FireworkParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            speedlinesParticle.setSprite(this.spriteProvider);
            return speedlinesParticle;
        }
    }
}