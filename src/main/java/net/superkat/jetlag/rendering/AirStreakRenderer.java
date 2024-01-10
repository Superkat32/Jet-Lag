package net.superkat.jetlag.rendering;

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
import net.superkat.jetlag.airstreak.JetLagClientPlayerEntity;
import org.joml.Matrix4f;

public class AirStreakRenderer {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    public static void renderAirStreaks(WorldRenderContext context, ClientPlayerEntity player) {
        Camera camera = context.camera();

//        Vec3d originPos = new Vec3d(0, -55, 0);
//        Vec3d targetPos = player.getPos();
//        Vec3d targetPos = new Vec3d(5, -55, 5);
//        Vec3d transformedPos = originPos.subtract(camera.getPos());
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
//        matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
        renderAirStreak(matrixStack, buffer, jetLagPlayer);

//        renderAirStreak(matrixStack, buffer, originPos, targetPos);

        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderTexture(0, BEAM_TEXTURE);
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        tessellator.draw();

        RenderSystem.enableCull();
        matrixStack.pop();
    }

    private static void renderAirStreak(MatrixStack matrixStack, BufferBuilder buffer, JetLagClientPlayerEntity player) {
        Vec3d origin;
        Vec3d target;
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        matrixStack.push();
        AirStreak playerAirStreaks = player.jetLag$getPlayerAirStreaks();
        if(playerAirStreaks != null) {
            for (int i = 0; i < playerAirStreaks.points.size() - 1; i++) {
                origin = playerAirStreaks.points.get(i);
                target = playerAirStreaks.points.get(i + 1);

                Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                Vec3d transformedPos = origin.subtract(camera.getPos());
                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
                matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);

                drawCube(matrixStack, buffer, origin, target);
                matrixStack.pop();
            }
        }
        matrixStack.pop();
    }

    private static void drawCube(MatrixStack matrixStack, BufferBuilder buffer, Vec3d origin, Vec3d target) {
        Vec3d vec3d = target.subtract(origin);
        float y = (float)(vec3d.length()); //needs to be calculated here
        vec3d = vec3d.normalize();
        float n = (float)Math.acos(vec3d.y);
        float o = (float)Math.atan2(vec3d.z, vec3d.x);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - o) * 57.295776F));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * 57.295776F));
        float value = MathHelper.cos(1) * 0.2f; //multiplying here to reduce the size of the beam
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        drawTopAndBottom(buffer, matrix4f, value, value, -value, -value, y);
        drawSides(buffer, matrix4f, value, value, -value, -value, y);
        drawEdges(buffer, matrix4f, value, value, -value, -value, y);
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