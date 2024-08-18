package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.config.SpeedlineConfigInstance;

import java.awt.*;

public class SpeedlineParticle extends CameraParticle {
    protected boolean fadeIn = false;
    protected float maxAlpha = 1f;
    protected float playerVelocity = 0f;
    public SpeedlineParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, 0, 0, 1, velX, velY, velZ);
        //determine random rotation
        this.angle = (float) Math.toRadians(this.random.nextBetween(0, 360));
        this.prevAngle = this.angle;
        //find position based on rotation
        this.velocityX = Math.cos(this.angle);
        this.velocityY = Math.sin(this.angle);

        //null check in case I decide to make a funny janky speedline editor screen using fake particles... somehow lol
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null) {
            this.playerVelocity = (float) player.getVelocity().lengthSquared();
        }
        setFieldsFromConfig(getSpeedlineConfigInstance());
    }

    /**
     * Set this speedline's fields based on a SpeedlineConfigInstance. The speedline's angle should already be set when called.
     * @param config The config to set the fields from
     */
    protected void setFieldsFromConfig(SpeedlineConfigInstance config) {
        this.width = config.width;
        this.length = config.length;

        this.velocityX /= 10f / config.speed;
        this.velocityY /= 10f / config.speed;
        this.velocityMultiplier = config.velMultiplier;

        float spawnRadius = config.spawnRadius;
        if(config.velBasedSpawnRadius) {
            spawnRadius /= (Math.min(this.playerVelocity, 1f));
        }
        setPositionFromRadius(spawnRadius, config.randomSpawnRadius);

        Color color = config.color;
        this.red = color.getRed() / 255f;
        this.green = color.getGreen() / 255f;
        this.blue = color.getBlue() / 255f;
        this.fadeIn = config.fadeIn;
        this.maxAlpha = color.getAlpha() / 255f;
        if(config.velBasedOpacity) {
            this.maxAlpha = MathHelper.clamp(maxAlpha * Math.min(this.playerVelocity, 1f), 0.15f, 1.0f);
        }
        this.alpha = this.fadeIn ? 0f : this.maxAlpha;

//        public boolean rainbowMode = false;

        int maxAge = config.maxAge;
        int minAge = config.minAge;
        if(minAge > maxAge) {
            maxAge = minAge;
        }
        this.maxAge = this.random.nextBetween(minAge, maxAge);
    }

    protected SpeedlineConfigInstance getSpeedlineConfigInstance() {
        return JetLagConfig.getInstance().speedlineConfig;
    }

    protected void setPositionFromRadius(float radius, float randomSpawnRadius) {
        radius += MathHelper.nextBetween(this.random, 0f, randomSpawnRadius);
        this.setBoundingBox(this.getBoundingBox().offset(this.velocityX * radius, this.velocityY * radius, 0f));
        this.repositionFromBoundingBox();
        this.prevPosX = this.x;
        this.prevPosY = this.y;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge || this.scale <= 0) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if(fadeIn) {
                this.alpha = easeOutCubic((float) this.age / this.maxAge);
            }
            this.velocityX *= this.velocityMultiplier;
            this.velocityY *= this.velocityMultiplier;
        }
    }

    private static float easeOutCubic(float time) {
        return (float) (1 - Math.pow(1 - time, 3));
    }

    @Override
    public boolean shouldRender() {
        return MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON && !MinecraftClient.getInstance().options.hudHidden;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            SpeedlineParticle particle = new SpeedlineParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
