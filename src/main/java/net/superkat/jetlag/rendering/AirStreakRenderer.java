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
import net.superkat.jetlag.config.JetLagConfig;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.include.com.google.common.collect.Lists;

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

    private static void renderTest(MatrixStack matrixStack, BufferBuilder buffer) {
        Vec3d point1 = new Vec3d(0, -58, 0);
        Vec3d point2 = new Vec3d(0, -58, -5);
        Vec3d point3 = new Vec3d(5, -58, -10);
        Vec3d point4 = new Vec3d(10, -58, -10);
        Vec3d point5 = new Vec3d(10, -58, -15);
        Vec3d point6 = new Vec3d(0, -58, -13);
        Vec3d point7 = new Vec3d(5, -56, -16);
        List<Vec3d> points = Lists.newArrayList();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(new Vec3d(5, -58, -20));
        points.add(new Vec3d(0, -58, -20));
        points.add(new Vec3d(-3, -58, -10));
        points.add(point6);
        points.add(point7);
        points.add(new Vec3d(5, -58, -25));

        JetLagPlayer jetLagPlayer = (JetLagPlayer) MinecraftClient.getInstance().player;
//        points = jetLagPlayer.jetlag$getAirStreaks().get(0).getRightPoints();

        Vec3d origin = new Vec3d(0, -58, 0);
        Vec3d target = new Vec3d(0, -58, -5);
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d transformedPos = origin.subtract(camera.getPos());
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);

//        drawCube(matrixStack, buffer, origin, target);
        matrixStack.pop();

//        drawSegment(matrixStack, buffer, origin, target);

        origin = new Vec3d(0, -58, -5);
        target = new Vec3d(5, -58, -10);
//        target = new Vec3d(10, -58, -10);
        transformedPos = origin.subtract(camera.getPos());
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrixStack.translate(transformedPos.x, transformedPos.y, transformedPos.z);

//        drawCube(matrixStack, buffer, origin, target);

        matrixStack.pop();
        int curvePoints = JetLagConfig.getInstance().airStreakCurvePoints;
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3d prevPoint = points.get(i != 0 ? i - 1 : 0);
            Vec3d originPoint = points.get(i);
            Vec3d targetPoint = points.get(i + 1);
            Vec3d nextPoint = points.get(i + 2 <= points.size() - 1 ? i + 2 : i + 1);

            Vec3d prevCurvePoint = prevPoint;
            for (int j = 0; j <= curvePoints; j++) {
                float delta = 1f / curvePoints * j;

                float curveX = MathHelper.catmullRom(delta, (float) prevPoint.getX(), (float) originPoint.getX(), (float) targetPoint.getX(), (float) nextPoint.getX());
                float curveY = MathHelper.catmullRom(delta, (float) prevPoint.getY(), (float) originPoint.getY(), (float) targetPoint.getY(), (float) nextPoint.getY());
                float curveZ = MathHelper.catmullRom(delta, (float) prevPoint.getZ(), (float) originPoint.getZ(), (float) targetPoint.getZ(), (float) nextPoint.getZ());
                Vec3d curvePoint = new Vec3d(curveX, curveY, curveZ);
                if(delta > 0f) {
                    renderSegment(matrixStack, buffer, curvePoint, prevCurvePoint, 1f);
                }
                prevCurvePoint = curvePoint;
            }
        }
    }

    private static void renderAirStreak(MatrixStack matrixStack, BufferBuilder buffer, AirStreak airStreak) {
        Vec3d origin;
        Vec3d target;
        matrixStack.push();
        renderTest(matrixStack, buffer);
        if(airStreak != null) {
            //renders all left wing points
            List<Vec3d> leftPoints = airStreak.getLeftPoints();
            renderList(matrixStack, buffer, leftPoints);

            //renders all right wing points
            List<Vec3d> rightPoints = airStreak.getRightPoints();
            renderList(matrixStack, buffer, rightPoints);
        }
        matrixStack.pop();
    }

    /**
     * Renders a connected line of Vec3d's given in a list. The list is rendering in order starting from 0. Called by each set of player air streaks.
     * @param matrixStack The MatrixStack used in rendering.
     * @param buffer The BufferBuilder used in rendering
     * @param points The list of Vec3d points to be rendered.
     */
    private static void renderList(MatrixStack matrixStack, BufferBuilder buffer, List<Vec3d> points) {
        matrixStack.push();
        int curvePoints = JetLagConfig.getInstance().airStreakCurvePoints;
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3d prevPoint = points.get(i != 0 ? i - 1 : 0);
            Vec3d originPoint = points.get(i);
            Vec3d targetPoint = points.get(i + 1);
            Vec3d nextPoint = points.get(i + 2 <= points.size() - 1 ? i + 2 : i + 1);

            Vec3d prevCurvePoint = prevPoint;
            for (int j = 0; j <= curvePoints; j++) {
                float delta = 1f / curvePoints * j;

                float curveX = MathHelper.catmullRom(delta, (float) prevPoint.getX(), (float) originPoint.getX(), (float) targetPoint.getX(), (float) nextPoint.getX());
                float curveY = MathHelper.catmullRom(delta, (float) prevPoint.getY(), (float) originPoint.getY(), (float) targetPoint.getY(), (float) nextPoint.getY());
                float curveZ = MathHelper.catmullRom(delta, (float) prevPoint.getZ(), (float) originPoint.getZ(), (float) targetPoint.getZ(), (float) nextPoint.getZ());
                Vec3d curvePoint = new Vec3d(curveX, curveY, curveZ);
                if(delta > 0f) {
                    renderSegment(matrixStack, buffer, curvePoint, prevCurvePoint, 1f);
                }
                prevCurvePoint = curvePoint;
            }
        }
        matrixStack.pop();
    }

    /**
     * Translates and rotates the MatrixStack. Also calculates the to-be rendered line's width and length.
     *
     * @param matrixStack The MatrixStack used for rendering.
     * @param buffer The BufferBuilder used for rendering.
     * @param origin The starting point to render from.
     * @param target The ending point to render to.
     * @param opacity The rendered segment's opacity/alpha value.
     */
    private static void renderSegment(MatrixStack matrixStack, BufferBuilder buffer, Vec3d origin, Vec3d target, float opacity) {
        //FIXME - Gap between each segment. Perhaps translate entire matrixStack only from the first or last point, and draw from there?
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d transformedMatrixPos = origin.subtract(camera.getPos());
        matrixStack.push();

        //offsets to the origin's pos
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrixStack.translate(transformedMatrixPos.x, transformedMatrixPos.y, transformedMatrixPos.z);

        //calculates length
        float length = (float) origin.distanceTo(target);

        //rotates towards target from origin
        Vec3d transformedPos = target.subtract(origin);
        transformedPos = transformedPos.normalize();
        float rightAngle = (float) Math.toRadians(90);
        float n = (float)Math.acos(transformedPos.y);
        float o = (float)Math.atan2(transformedPos.z, transformedPos.x);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) Math.toDegrees(rightAngle - o))); //rotates left/right
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) Math.toDegrees(rightAngle + n))); //rotates up/down

        drawRectangle(matrixStack.peek().getPositionMatrix(), buffer, 0.1f, -length, opacity);

        matrixStack.pop();
    }

    /**
     * Renders a rectangle with a specific width and length. For Air Streak rendering, this is called after the MatrixStack has been translated/rotated.
     *
     * @param matrix The MatrixStack's Position Matrix used for rendering.
     * @param buffer The BufferBuilder used for rendering
     * @param width The rendered rectangle's width. Should be determined by a config option.
     * @param length The rendered rectangle's length. Should be determined by the length from the origin point to the target point in {@link #renderSegment(MatrixStack, BufferBuilder, Vec3d, Vec3d, float)}.
     * @param opacity The rendered rectangle's opacity/alpha value.
     */
    private static void drawRectangle(Matrix4f matrix, BufferBuilder buffer, float width, float length, float opacity) {
        buffer.vertex(matrix, 0, 0, length).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
        buffer.vertex(matrix, 0, 0, 0).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
        buffer.vertex(matrix, width, 0, 0).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
        buffer.vertex(matrix, width, 0, length).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
    }

    private static void drawCube(MatrixStack matrixStack, BufferBuilder buffer, Vec3d origin, Vec3d target) {
        Vec3d vec3d = target.subtract(origin);
        float y = (float)(vec3d.length()); //needs to be calculated here
        vec3d = vec3d.normalize();
        float n = (float)Math.acos(vec3d.y);
        float o = (float)Math.atan2(vec3d.z, vec3d.x);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - o) * 57.295776F));
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * 57.295776F));
        float value = MathHelper.cos(1) * 0.2f; //multiplying here to reduce the size of the beam
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        drawTopAndBottom(buffer, matrix4f, value, o, -value, -value, y, 1f);
//        drawSides(buffer, matrix4f, value, value, -value, -value, y);
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

    private static void drawTopAndBottom(BufferBuilder buffer, Matrix4f matrix, float x1, float z1, float x2, float z2, float y, float opacity) {
        //top center
        buffer.vertex(matrix, 0, 0, y).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
        buffer.vertex(matrix, 0, 0, 0).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0, 0).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
        buffer.vertex(matrix, x1, 0, y).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();

//        buffer.vertex(matrix, 0, 0, 1).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
//        buffer.vertex(matrix, 0, 0, 0).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
//        buffer.vertex(matrix, 1, 0, 0).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();
//        buffer.vertex(matrix, 1, 0, 1).color(1f, 1f, 1f, opacity).texture(1f, 1f).next();

        //bottom center
//        buffer.vertex(matrix, x1, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix, x1, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix, x2, 0.0F, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
//        buffer.vertex(matrix, x2, y, z2).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
    }

}
