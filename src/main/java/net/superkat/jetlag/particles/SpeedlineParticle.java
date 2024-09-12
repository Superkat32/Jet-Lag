package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.config.SpeedlineConfigInstance;
import net.superkat.jetlag.speedline.FancyParticleTextureSheet;
import net.superkat.jetlag.speedline.SpeedlineHandler;

import java.awt.*;

public class SpeedlineParticle extends CameraParticle {
    protected boolean fadeIn = false;
    protected float maxAlpha = 1f;
    protected float playerVelocity = 0f;
    protected boolean rainbowMode = false;

    protected final ParticleTextureSheet particleTextureSheet;
    public SpeedlineParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, 0, 0, 1, velX, velY, velZ);
        //determine random rotation
        this.angle = (float) Math.toRadians(this.random.nextBetween(0, 360));
        this.prevAngle = this.angle;

        //find position based on rotation
        this.velocityX = Math.cos(this.angle);
        this.velocityY = Math.sin(this.angle);
//        this.velocityZ = 0;

        //null check in case I decide to make a funny janky speedline editor screen using fake particles... somehow lol
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null) {
            this.playerVelocity = (float) player.getVelocity().lengthSquared();
        }

        setFieldsFromConfig(getSpeedlineConfigInstance());

        if(fancyRainbowMode()) {
            particleTextureSheet = fancyRainbowSheet();
        } else {
            particleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
        }
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
        if(config.velBasedSpawnRadius && this.playerVelocity < 1f) {
            spawnRadius += (1f - this.playerVelocity) * 5f;
        }
        setPositionFromRadius(spawnRadius, config.randomSpawnRadius, config.moveFromTurn, config.turnMoveMultiplier, config.maxMoveAmountX, config.maxMoveAmountY);

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
        this.rainbowMode = config.rainbowMode;

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

    protected void setPositionFromRadius(float radius, float randomSpawnRadius, boolean moveFromTurn, float turnMoveMultiplier, float maxMoveX, float maxMoveY) {
        radius += MathHelper.nextBetween(this.random, 0f, randomSpawnRadius);
        double multiplier = turnMoveMultiplier / 100;
        double xOffset = moveFromTurn ? MathHelper.clamp(SpeedlineHandler.speedlineXOffset(), -maxMoveX, maxMoveX) * multiplier : 0;
        double yOffset = moveFromTurn ? MathHelper.clamp(SpeedlineHandler.speedlineYOffset(), -maxMoveY, maxMoveY) * multiplier : 0;
        this.setBoundingBox(this.getBoundingBox().offset((this.velocityX * radius) + xOffset, (this.velocityY * radius) + yOffset, 0f));
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
        MinecraftClient client = MinecraftClient.getInstance();
        boolean shouldRender = true;

        if(JetLagConfig.getInstance().hideSpeedlinesInF1 && client.options.hudHidden) {
            shouldRender = false;
        } else if(!JetLagConfig.getInstance().thirdPersonSpeedlines && client.options.getPerspective() != Perspective.FIRST_PERSON) {
            shouldRender = false;
        }

        return shouldRender;
    }

    @Override
    public boolean ignoreFov() {
        return JetLagConfig.getInstance().speedlinesIgnoreFov;
    }

    @Override
    public boolean fancyRainbowMode() {
        return this.rainbowMode;
    }

    @Override
    public ParticleTextureSheet getType() {
        return this.particleTextureSheet;
    }

    protected ParticleTextureSheet fancyRainbowSheet() {
        return FancyParticleTextureSheet.JETLAG_FANCY_RAINBOW_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory extends JetLagParticleFactory {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
            SpeedlineParticle particle = new SpeedlineParticle(clientWorld, x, y, z, velX, velY, velZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
