package net.superkat.jetlag.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.ShriekParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ShriekParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.superkat.jetlag.JetLagClient;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * A particle that gets rendered right in front of the camera. The z value is used to determine the distance from the camera(1 should be default).
 */
public class CameraParticle extends SpriteBillboardParticle {
    //for some reason Mojang removed the field this.rotation in 1.21 ¯\_(ツ)_/¯
    private Quaternionf rotation = new Quaternionf();

    protected float width = 1f;
    protected float length = 1f;
    /**
     * NOTE - x, y, and z are set to 0, 0, and 1 respectfully by the {@link Factory}. Make sure this gets changed when inheriting.
     */
    public CameraParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
        super(clientWorld, x, y, z, velX, velY, velZ);
        this.collidesWithWorld = false;
    }

    /**
     * Render a particle right in front of the camera. This is basically the Billboard Particle without translating to world space, and with some misc additions I wanted.
     * @param vertexConsumer The buffer to render to
     * @param camera         The current active game {@link Camera}
     * @param tickDelta      Frame tick delta amount
     */
    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if(!shouldRender()) return;

        MatrixStack matrices = new MatrixStack();
        matrices.push();

        Quaternionf cameraRotation = camera.getRotation();
        //WHY MOJANG WHY - This literally took 2 days to figure out the issue
        //no clue why the camera rotation's values were moved around but okay
        matrices.multiply(new Quaternionf(cameraRotation.z, -cameraRotation.w, -cameraRotation.x, cameraRotation.y));

//        matrices.multiply(cameraRotation);

        if(ignoreFov()) {
            //Surely this won't cause any issues... right?
            GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;
            gameRenderer.loadProjectionMatrix(gameRenderer.getBasicProjectionMatrix(gameRenderer.getFov(camera, tickDelta, false)));
        }

        float x = (float) MathHelper.lerp(tickDelta, this.prevPosX, this.x);
        float y = (float) MathHelper.lerp(tickDelta, this.prevPosY, this.y);
        float z = (float) MathHelper.lerp(tickDelta, this.prevPosZ, this.z);

        Quaternionf rotation = new Quaternionf();
        if (this.angle != 0.0F) {
            rotation.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
        }

        Vector3f[] vector3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float i = this.getSize(tickDelta);

        for(int j = 0; j < 4; ++j) {
            Vector3f vector3f = vector3fs[j];
            vector3f.x *= length;
            vector3f.y *= width;
            vector3f.rotate(rotation);
            vector3f.mul(i);
            vector3f.add(x, y, z);
        }

        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();
        int o = this.getBrightness(tickDelta);

        if(fancyRainbowMode()) {
            RenderSystem.setShader(() -> JetLagClient.rainbowParticle);
        }

        Matrix4f posMatrix = matrices.peek().getPositionMatrix();
        createVertex(vertexConsumer, posMatrix, vector3fs[0], l, n, o);
        createVertex(vertexConsumer, posMatrix, vector3fs[1], l, m, o);
        createVertex(vertexConsumer, posMatrix, vector3fs[2], k, m, o);
        createVertex(vertexConsumer, posMatrix, vector3fs[3], k, n, o);

        matrices.pop();
    }

    private void createVertex(VertexConsumer vertexConsumer, Matrix4f posMatrix, Vector3f vector3f, float u, float v, int light) {
            vertexConsumer.vertex(posMatrix, vector3f.x(), vector3f.y(), vector3f.z())
                    .texture(u, v)
                    .color(this.red, this.green, this.blue, this.alpha)
                    .light(light);
    }

    public boolean shouldRender() {
        return true;
    }

    public boolean ignoreFov() {
        return true;
    }

    //funnily enough, my poor shader work due to inexperience makes this effect look crazy cool
    public boolean fancyRainbowMode() {
        return false;
    }

    //Prevents speedlines from appearing very dark when in a world coordinate far from the player
    @Override
    protected int getBrightness(float tint) {
        return 15728880;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

//    @Environment(EnvType.CLIENT)
//    public static class Factory implements ParticleFactory<ShriekParticleEffect> {
//        private final SpriteProvider spriteProvider;
//
//        public Factory(SpriteProvider spriteProvider) {
//            this.spriteProvider = spriteProvider;
//        }
//
//        public Particle createParticle(ShriekParticleEffect shriekParticleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
//            ShriekParticle shriekParticle = new ShriekParticle(clientWorld, d, e, f, shriekParticleEffect.getDelay());
//            shriekParticle.setSprite(this.spriteProvider);
//            shriekParticle.setAlpha(1.0F);
//            return shriekParticle;
//        }
//    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
            CameraParticle particle = new CameraParticle(clientWorld, 0, 0, 1, velX, velY, velZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
