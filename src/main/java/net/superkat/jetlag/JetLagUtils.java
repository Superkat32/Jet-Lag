package net.superkat.jetlag;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class JetLagUtils {

    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");
    public static float wingAngle = 0f;
    private Vec3d point1 = new Vec3d(5, 5, 5);
    private Vec3d point2 = null;

//    public static Float currentYaw(ClientPlayerEntity player) {
//        //0 = facing South
//        float playerYaw = Math.abs(player.getYaw());
//        float yaw = playerYaw % 360;
////        float yaw = MathHelper.wrapDegrees(playerYaw);
//        return yaw;
//    }
//
//    public static Vec3d particleLeft(ClientPlayerEntity player) {
////
//    }

//    public static void renderAirStreak(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
////        matrices.push();
////        matrices.translate(0f, 0f, 0.125f);
//        Vec3d targetPos = new Vec3d(0, 0, 0);
//        Vec3d truePos = targetPos.subtract(entity.getPos());
//
//        MatrixStack matrixStack = new MatrixStack();
//        Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
////        matrixStack.push();
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cameraEntity.getPitch()));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cameraEntity.getYaw() + 180f));
//        matrixStack.translate(0, 0, truePos.z);
//        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
////
////        posMatrix.translate(0, 0, 0.125f);
//        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
//        buffer.vertex(posMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
//        buffer.vertex(posMatrix, 0, 0, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
//        buffer.vertex(posMatrix, 1, 0, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
//        buffer.vertex(posMatrix, 1, 1, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();
//
//        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
//        RenderSystem.setShaderTexture(0, BEAM_TEXTURE);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//
//        tessellator.draw();
////        matrixStack.pop();
////        matrices.pop();
//    }

//    public static void renderAirStreak(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity entity) {
//        Vec3d targetPos = new Vec3d(0, -55, 0);
//        Vec3d originPos = new Vec3d(0, entity.getY(), 0);
//        Vec3d truePos = targetPos.subtract(entity.getPos());
//
//        Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
////        matrixStack.push();
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cameraEntity.getPitch()));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cameraEntity.getYaw() + 180f));
//        matrixStack.translate(0, 0, truePos.z);
//        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
//
////
////        posMatrix.translate(0, 0, 0.125f);
////        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
////        buffer.vertex(posMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
////        buffer.vertex(posMatrix, 0, 0, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
////        buffer.vertex(posMatrix, 1, 0, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
////        buffer.vertex(posMatrix, 1, 1, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();
//
//        drawCube(buffer, posMatrix, matrixStack, originPos, targetPos);
//
//        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
//        RenderSystem.setShaderTexture(0, BEAM_TEXTURE);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//
//        tessellator.draw();
////        matrixStack.pop();
////        matrices.pop();
//    }

//    public static void renderAirStreak(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float yaw, float delta, LivingEntity entity) {
//        Vec3d targetPos = new Vec3d(0, -55, 0);
//        Vec3d originPos = new Vec3d(0, entity.getY(), 0);
//        Vec3d truePos = targetPos.subtract(entity.getPos());
//
//        Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
//        matrixStack.push();
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cameraEntity.getPitch()));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cameraEntity.getYaw() + 180f));
//        matrixStack.translate(0, 0, truePos.z);
//        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
//
////
////        posMatrix.translate(0, 0, 0.125f);
////        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
////        buffer.vertex(posMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
////        buffer.vertex(posMatrix, 0, 0, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
////        buffer.vertex(posMatrix, 1, 0, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
////        buffer.vertex(posMatrix, 1, 1, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();
//
//        drawCube(buffer, posMatrix, matrixStack, originPos, targetPos);
//
//        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
//        RenderSystem.setShaderTexture(0, BEAM_TEXTURE);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//
//        tessellator.draw();
//        matrixStack.pop();
////        matrices.pop();
//    }

//    public static void renderAirStreak(WorldRenderContext context, ClientPlayerEntity player) {
//        Camera camera = context.camera();
//
//        Vec3d originPos = new Vec3d(0, -55, 0);
////        Vec3d targetPos = player.getPos();
//        Vec3d targetPos = new Vec3d(5, -55, 5);
//        Vec3d transformedPos = originPos.subtract(camera.getPos());
//        MatrixStack matrixStack = new MatrixStack();
//        matrixStack.push();
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
//        matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);
//        matrixStack.multiply(camera.getRotation());
//
//        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
//
//        drawCube(buffer, posMatrix, matrixStack, originPos, targetPos);
//
//        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
//        RenderSystem.setShaderTexture(0, BEAM_TEXTURE);
//        RenderSystem.disableCull();
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//
//        tessellator.draw();
//
//        RenderSystem.enableCull();
//        matrixStack.pop();
//    }

    public static void renderAirStreaks(WorldRenderContext context, ClientPlayerEntity player) {
        Camera camera = context.camera();

        Vec3d originPos = new Vec3d(0, -55, 0);
//        Vec3d targetPos = player.getPos();
        Vec3d targetPos = new Vec3d(5, -55, 5);
        Vec3d transformedPos = originPos.subtract(camera.getPos());
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees());
//        matrixStack.multiply(camera.getRotation());

        Matrix4f posMatrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

//        drawCube(buffer, posMatrix, originPos, targetPos);

        renderAirStreak(matrixStack, buffer, originPos, targetPos);

        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderTexture(0, BEAM_TEXTURE);
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        tessellator.draw();

        RenderSystem.enableCull();
        matrixStack.pop();
//        renderAirStreak(context.matrixStack(), origin, target);
    }

    private static void renderAirStreak(MatrixStack matrixStack, BufferBuilder buffer, Vec3d origin, Vec3d target) {

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        float h = 1;
        float j = 1;
        float k = j * 0.5F % 1.0F;
        float l = player.getStandingEyeHeight();
        matrixStack.push();
//        matrixStack.translate(0.0F, l, 0.0F);
        Vec3d vec3d3 = target.subtract(origin);
        float m = (float)(vec3d3.length() + 1.0);
        vec3d3 = vec3d3.normalize();
        float n = (float)Math.acos(vec3d3.y);
        float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - o) * 57.295776F));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * 57.295776F));
        float q = j * 0.05F * -1.5F;
        float r = h * h;
        int s = 64 + (int)(r * 191.0F);
        int t = 32 + (int)(r * 191.0F);
        int u = 128 - (int)(r * 64.0F);
        float v = 0.2F;
        float w = 0.282F;
        float x = MathHelper.cos(q + 2.3561945F) * 0.282F;
        float y = MathHelper.sin(q + 2.3561945F) * 0.282F;
        float z = MathHelper.cos(q + 0.7853982F) * 0.282F;
        float aa = MathHelper.sin(q + 0.7853982F) * 0.282F;
        float ab = MathHelper.cos(q + 3.926991F) * 0.282F;
        float ac = MathHelper.sin(q + 3.926991F) * 0.282F;
        float ad = MathHelper.cos(q + 5.4977875F) * 0.282F;
        float ae = MathHelper.sin(q + 5.4977875F) * 0.282F;
        float af = MathHelper.cos(q + 3.1415927F) * 0.2F; // left - using in x equals left(when facing north)
        float ag = MathHelper.sin(q + 3.1415927F) * 0.2F;
        float ah = MathHelper.cos(q + 0.0F) * 0.2F; //right - using in x equals right(when facing north)
        float ai = MathHelper.sin(q + 0.0F) * 0.2F;
        float aj = MathHelper.cos(q + 1.5707964F) * 0.2F;
        float ak = MathHelper.sin(q + 1.5707964F) * 0.2F; //bottom y - using in z equals bottom
        float al = MathHelper.cos(q + 4.712389F) * 0.2F;
        float am = MathHelper.sin(q + 4.712389F) * 0.2F; //top y - using in z equals top
        float ao = 0.0F;
        float ap = 0.4999F;
        float aq = -1.0F + k;
        float ar = m * 2.5F + aq;
//        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.c);
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        drawTopAndBottom(buffer, matrix4f, af, am, ah, ak, m);

        //top center
//        buffer.vertex(matrix4f, af, m, am).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, af, 0.0F, am).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, 0.0F, am).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, m, am).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();

        //bottom center
//        buffer.vertex(matrix4f, af, m, ak).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, af, 0.0F, ak).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, 0.0F, ak).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, m, ak).color(1f, 1f, .5f, 1f).texture(1f, 1f).next();

        drawSides(buffer, matrix4f, af, ak, ah, am, m);

        //left side(when facing north)
//        buffer.vertex(matrix4f, af, m, ak).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, af, 0.0F, ak).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, af, 0.0F, am).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, af, m, am).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

        //right side(when facing north)
//        buffer.vertex(matrix4f, ah, m, ak).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, 0.0F, ak).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, 0.0F, am).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, m, am).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

        drawEdges(buffer, matrix4f, af, am, ah, ak, m);

        //origin edge;
//        buffer.vertex(matrix4f, af, 0f, am).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
//        buffer.vertex(matrix4f, ah, 0f, am).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, 0f, ak).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();
//        buffer.vertex(matrix4f, af, 0f, ak).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();

        //target edge
//        buffer.vertex(matrix4f, af, m, am).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
//        buffer.vertex(matrix4f, ah, m, am).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix4f, ah, m, ak).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();
//        buffer.vertex(matrix4f, af, m, ak).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();

    }

    private static void drawCube(BufferBuilder buffer, Matrix4f posMatrix, Vec3d origin, Vec3d target) {

    }

    private static void drawEdges(BufferBuilder buffer, Matrix4f matrix, float x1, float z1, float x2, float z2, float y) {
        //origin edge - Note: Has to be rendered in very specifically this order for some reason for both edges
        buffer.vertex(matrix, x1, 0f, z1).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
        buffer.vertex(matrix, x2, 0f, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0f, z2).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();
        buffer.vertex(matrix, x1, 0f, z2).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();

        //target pos edge
        buffer.vertex(matrix, x1, y, z1).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
        buffer.vertex(matrix, x2, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();
        buffer.vertex(matrix, x1, y, z2).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();

    }

    private static void drawSides(BufferBuilder buffer, Matrix4f matrix, float x1, float z1, float x2, float z2, float y) {
        //left side(when facing north)
        buffer.vertex(matrix, x1, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0.0F, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

        //right side(when fang north)
        buffer.vertex(matrix, x2, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0.0F, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
    }


    private static void drawTopAndBottom(BufferBuilder buffer, Matrix4f matrix, float x1, float z1, float x2, float z2, float y) {
        //top center
        buffer.vertex(matrix, x1, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0.0F, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0.0F, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

        //bottom center
        buffer.vertex(matrix, x1, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
    }

}
