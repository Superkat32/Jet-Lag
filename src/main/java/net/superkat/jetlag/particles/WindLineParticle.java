package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.config.JetLagConfig;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import java.awt.*;

@Environment(EnvType.CLIENT)
public class WindLineParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private float stretch;
    private float maxAlpha;
    private float pitch;
    private float yaw;

    WindLineParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, float pitch, float yaw) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.maxAge = 80 + this.random.nextBetween(0, 40);
        this.scale = 0.15F + this.random.nextFloat() / 2f;
        this.stretch = 3.75f + this.random.nextFloat();

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.x = x;
        this.y = y;
        this.z = z;
        this.collidesWithWorld = true;

        this.maxAlpha = MathHelper.clamp(1f - this.random.nextFloat() / 2f, 0.2f, 1f);
        this.alpha = 0.01f;

        this.pitch = pitch;
        this.yaw = -yaw + 90f;
        this.angle = (float) Math.toRadians(pitch);
        this.prevAngle = angle;

        Color color = JetLagConfig.getInstance().windLinesColor;
        this.setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

        this.setSpriteForAge(spriteProvider);
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge || this.scale <= 0 || this.stretch <= 0) {
            this.markDead();
        } else {
            if(this.age <= 7) {
                this.stretch *= 1.01f;
                this.alpha = MathHelper.lerp((float) this.age / 7, 0.01f, maxAlpha);
            } else {
                this.alpha *= 0.9f;
                this.stretch -= 0.05f;
                this.stretch *= 0.8f;
            }
            this.move(this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float lerpX = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
        float lerpY = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
        float lerpZ = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());
        int light = this.getBrightness(tickDelta);

        Quaternionf rotation = new Quaternionf();

        //angle var rotation
        this.getRotator().setRotation(rotation, camera, tickDelta);
        if (this.angle != 0.0f) {
            rotation.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
        }

        this.buildGeometry(vertexConsumer, rotation, lerpX, lerpY, lerpZ, tickDelta, light);
        rotation.rotateY((float) Math.toRadians(180f));
        rotation.rotateZ((float) Math.toRadians(180f));
        this.buildGeometry(vertexConsumer, rotation, lerpX, lerpY, lerpZ, tickDelta, light);
    }

    private void buildGeometry(VertexConsumer vertexConsumer, Quaternionf rotation, float lerpX, float lerpY, float lerpZ, float tickDelta, int light) {
        float size = this.getSize(tickDelta);
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-stretch, -1.0f, 0.0f), new Vector3f(-stretch, 1.0f, 0.0f), new Vector3f(stretch, 1.0f, 0.0f), new Vector3f(stretch, -1.0f, 0.0f)};
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = vector3fs[i];
            vector3f.rotate(rotation);
            vector3f.mul(size);
            vector3f.add(lerpX, lerpY, lerpZ);
        }
        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        createVertex(vertexConsumer, vector3fs[0], maxU, maxV, light);
        createVertex(vertexConsumer, vector3fs[1], maxU, minV, light);
        createVertex(vertexConsumer, vector3fs[2], minU, minV, light);
        createVertex(vertexConsumer, vector3fs[3], minU, maxV, light);
    }

    private void createVertex(VertexConsumer vertexConsumer, Vector3f vector3f, float u, float v, int light) {
        vertexConsumer.vertex(vector3f.x(), vector3f.y(), vector3f.z())
                .texture(u, v)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(light);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public Rotator getRotator() {
        return (quaternion, camera, tickDelta) -> {
            quaternion.set(0.0f, 0.0f, 0.0f, camera.getRotation().w); //removes billboard
            quaternion.rotateY((float) Math.toRadians(this.yaw)); //adds custom yaw
        };
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<WindLineParticleEffect> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(WindLineParticleEffect effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            WindLineParticle particle = new WindLineParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider, effect.getPitch(), effect.getYaw());
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
