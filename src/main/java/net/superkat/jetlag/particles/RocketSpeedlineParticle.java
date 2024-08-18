package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.config.SpeedlineConfigInstance;

public class RocketSpeedlineParticle extends SpeedlineParticle {
    public RocketSpeedlineParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, x, y, z, velX, velY, velZ);
    }

    @Override
    protected SpeedlineConfigInstance getSpeedlineConfigInstance() {
        return JetLagConfig.getInstance().rocketConfig;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            RocketSpeedlineParticle particle = new RocketSpeedlineParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
