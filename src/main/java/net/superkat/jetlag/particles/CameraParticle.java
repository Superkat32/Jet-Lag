package net.superkat.jetlag.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * A particle that gets rendered right in front of the camera. The z value is used to determine the distance from the camera(1 should be default).
 */
public class CameraParticle extends SpriteBillboardParticle {
    protected float stretch = 1f;
    /**
     * NOTE - x, y, and z are set to 0, 0, and 1 respectfully by the {@link Factory}. Make sure this gets changed when inheriting.
     */
    public CameraParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, x, y, z, velX, velY, velZ);
    }

    /**
     * Render a particle right in front of the camera.
     * @param vertexConsumer The buffer to render to
     * @param camera         The current active game {@link Camera}
     * @param tickDelta      Frame tick delta amount
     */
    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        MatrixStack matrices = new MatrixStack();
        matrices.push();
        matrices.multiply(camera.getRotation());

        float x = (float) MathHelper.lerp(tickDelta, this.prevPosX, this.x);
        float y = (float) MathHelper.lerp(tickDelta, this.prevPosY, this.y);
        float z = (float) MathHelper.lerp(tickDelta, this.prevPosZ, this.z);
        this.rotation.set(new Quaternionf());
        if (this.angle != 0.0F) {
            this.rotation.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
//            this.rotation.z = (float) Math.toRadians(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
        }

        Vector3f[] vector3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float i = this.getSize(tickDelta);

        for(int j = 0; j < 4; ++j) {
            Vector3f vector3f = vector3fs[j];
            vector3f.x += vector3f.x == -1.0f ? -stretch : stretch;
            vector3f.rotate(this.rotation);
            vector3f.mul(i);
//            vector3f.x += vector3f.x == 1 ? stretch : -stretch;
            vector3f.add(x, y, z);
        }

        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();
        int o = this.getBrightness(tickDelta);
        Matrix4f posMatrix = matrices.peek().getPositionMatrix();
        vertexConsumer.vertex(posMatrix, vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
                .texture(l, n)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(o)
                .next();
        vertexConsumer.vertex(posMatrix, vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
                .texture(l, m)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(o)
                .next();
        vertexConsumer.vertex(posMatrix, vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
                .texture(k, m)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(o)
                .next();
        vertexConsumer.vertex(posMatrix, vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
                .texture(k, n)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(o)
                .next();

        matrices.pop();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            CameraParticle particle = new CameraParticle(clientWorld, 0, 0, 1, g, h, i);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
