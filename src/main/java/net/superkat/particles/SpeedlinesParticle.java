package net.superkat.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class SpeedlinesParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
//    private final Model model;
//    private final RenderLayer layer;

    SpeedlinesParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.maxAge = 200;
        this.scale = 0.3F;
//        this.gravityStrength = 0.008F;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
//        this.angle = 0.00001F;
//        setSprite(MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture).apply(new Identifier("particle/particles")));
        gravityStrength = 0;
        setBoundingBoxSpacing(0.1F, 0.1F);
//        this.layer = RenderLayer.getEntityTranslucent(ElderGuardianEntityRenderer.TEXTURE);
//        this.model = new GuardianEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.ELDER_GUARDIAN));
        this.setSpriteForAge(spriteProvider);
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.velocityY -= (double)this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.setSpriteForAge(this.spriteProvider);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

//    @Override
//    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
//
//    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float ticks) {
        float f = ((float)this.age + ticks) / (float)this.maxAge;
        float g = 0.05F + 0.5F * MathHelper.sin(f * 3.1415927F);
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};

        float f4 = this.getSize(ticks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = vector3fs[i];
            vector3f.rotate(camera.getRotation());
            vector3f.rotate(RotationAxis.POSITIVE_X.rotationDegrees(0.0F));
            vector3f.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(0.0F));
            vector3f.rotate(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
            vector3f.mul(f4);
//            vector3f.add(camera.getPitch(), -0.5F, camera.getPitch()); // this does something funny. Only in one direction though
            vector3f.add(0.0F, 0.2F, 0.0F);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int light = this.getBrightness(ticks);

        // Render the top faces
        vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(f8, f6).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(f8, f5).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(f7, f5).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(f7, f6).color(this.red, this.green, this.blue, this.alpha).light(light).next();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new SpeedlinesParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}