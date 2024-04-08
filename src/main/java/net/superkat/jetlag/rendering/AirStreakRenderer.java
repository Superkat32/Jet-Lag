package net.superkat.jetlag.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.airstreak.AirStreak;
import net.superkat.jetlag.airstreak.JetLagClientPlayerEntity;
import net.superkat.jetlag.airstreak.JetLagPlayer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class AirStreakRenderer {
    public static final Identifier AIRSTREAK_TEXTURE = new Identifier(JetLagMain.MOD_ID, "textures/airstreak.png");

    public static void airStreakWorldRendering(WorldRenderContext context) {
        List<AbstractClientPlayerEntity> players = context.world().getPlayers();
        for(AbstractClientPlayerEntity abstractPlayer : players) {
            if(abstractPlayer instanceof ClientPlayerEntity player) {
                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                jetLagPlayer.jetlag$tick();
//                JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
//                if(player.isFallFlying() || player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.SPYGLASS) {
//                    if(jetLagPlayer.jetLag$getPlayerAirStreaks() == null) {
//                        jetLagPlayer.jetLag$setAirStreak(new AirStreak(player));
//                    }
//                } if (jetLagPlayer.jetLag$getPlayerAirStreaks() != null) {
//                    AirStreakRenderer.renderAirStreaks(context, player);
//                }
            }
        }
    }

    public static void renderAirStreaks(ClientPlayerEntity player) {
        JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
        AirStreak playerAirStreaks = jetLagPlayer.jetLag$getPlayerAirStreaks();
        renderAirStreaks(playerAirStreaks);
    }

    public static void renderAirStreaks(AirStreak airStreak) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        renderAirStreak(matrixStack, buffer, airStreak);

        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderTexture(0, AIRSTREAK_TEXTURE);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.3f);

        tessellator.draw();

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();

        matrixStack.pop();
    }

    private static void renderAirStreak(MatrixStack matrixStack, BufferBuilder buffer, AirStreak airStreak) {
        Vec3d origin;
        Vec3d target;
        matrixStack.push();
        if(airStreak != null) {
            //renders all left wing points
            List<Vec3d> leftPoints = airStreak.getLeftPoints();
            for (int i = 0; i < leftPoints.size() - 1; i++) {
                origin = leftPoints.get(i);
                target = leftPoints.get(i + 1);
                Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                Vec3d transformedPos = origin.subtract(camera.getPos());
                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
                matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);

                drawCube(matrixStack, buffer, origin, target);
                matrixStack.pop();
            }

            //renders all right wing points
            List<Vec3d> rightPoints = airStreak.getRightPoints();
            for (int i = 0; i < rightPoints.size() - 1; i++) {
                origin = rightPoints.get(i);
                target = rightPoints.get(i + 1);

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
//        drawEdges(buffer, matrix4f, value, value, -value, -value, y);
    }

    private static void drawEdges(BufferBuilder buffer, Matrix4f matrix, float x1, float z1, float x2, float z2, float y) {
        //origin edge - Note: Has to be rendered in very specifically this order for some reason for both edges
        buffer.vertex(matrix, x1, 0f, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0f, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, 0f, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0f, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

        //target pos edge
        buffer.vertex(matrix, x1, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x2, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

    }

    private static void drawSides(BufferBuilder buffer, Matrix4f matrix, float x1, float z1, float x2, float z2, float y) {
        //left side(when facing north)
        buffer.vertex(matrix, x1, y, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0.0F, z1).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();

        //right side(when facing north)
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
