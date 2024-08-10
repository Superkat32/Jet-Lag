package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class SpeedlineParticle extends CameraParticle {
    public SpeedlineParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, 0, 0, 1, velX, velY, velZ);
        //determine random rotation
        this.angle = (float) Math.toRadians(this.random.nextBetween(0, 360));
        this.prevAngle = this.angle;
        //find position based on rotation
        this.velocityX = Math.cos(this.angle) / 4.5f;
        this.velocityY = Math.sin(this.angle) / 4.5f;
        this.velocityMultiplier = 0.75f;
        //move away from center of screen
        setPositionFromRadius();

        this.stretch = 2.5f;

        this.maxAge = this.random.nextBetween(15, 50);
        this.alpha = 1f;
    }

    protected void setPositionFromRadius() {
        this.setBoundingBox(this.getBoundingBox().offset(this.velocityX * this.radius(), this.velocityY * this.radius(), 0f));
        this.repositionFromBoundingBox();
        this.prevPosX = this.x;
        this.prevPosY = this.y;
    }

    public float radius() {
        return 4.75f;
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
//            this.alpha = easeOutSine((float) this.age / this.maxAge);
        }
    }

    private static float easeOutSine(float time) {
//        return time == 1f ? 1 : (float) (1 - Math.pow(2, -10 * time));
//        return time == 0 ? 0f : (float) Math.pow(2, 10 * time - 10);
//        return time < 0.5 ? 4 * time * time * time : (float) (1 - Math.pow(-2 * time + 2, 3) / 2);
//        return (float) (-(Math.cos(Math.PI * time) - 1) / 2);
//        return (float) (1 - Math.pow(1 - time, 3));
        return (float) (Math.sin((time * Math.PI) / 2));
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
