package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.config.SpeedlineConfigInstance;
import net.superkat.jetlag.speedline.FancyParticleTextureSheet;

public class RocketSpeedlineParticle extends SpeedlineParticle {
    public RocketSpeedlineParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, x, y, z, velX, velY, velZ);
    }

    @Override
    protected SpeedlineConfigInstance getSpeedlineConfigInstance() {
        return JetLagConfig.getInstance().rocketConfig;
    }

    @Override
    protected ParticleTextureSheet fancyRainbowSheet() {
        return FancyParticleTextureSheet.JETLAG_FANCY_RAINBOW_TRANSLUCENT_ROCKET;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
            RocketSpeedlineParticle particle = new RocketSpeedlineParticle(clientWorld, x, y, z, velX, velY, velZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
