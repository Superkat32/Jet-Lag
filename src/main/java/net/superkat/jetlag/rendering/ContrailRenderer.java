package net.superkat.jetlag.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.contrail.Contrail;
import net.superkat.jetlag.contrail.JetLagPlayer;
import org.joml.Matrix4f;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

import static net.superkat.jetlag.config.JetLagConfig.getInstance;

public class ContrailRenderer {
    public static final Identifier CONTRAIL_TEXTURE = new Identifier(JetLagMain.MOD_ID, "textures/contrail.png");

    public static void airStreakWorldRendering(WorldRenderContext context) {
        List<AbstractClientPlayerEntity> players = context.world().getPlayers();
        for(AbstractClientPlayerEntity abstractPlayer : players) {
            if(abstractPlayer instanceof ClientPlayerEntity player) {
                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                jetLagPlayer.jetlag$tick();
            }
        }
    }

    /**
     * Render a player's contrail's points.
     *
     * @param player Renders this player's existing contrails
     */
    public static void renderContrails(ClientPlayerEntity player) {
        JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
        List<Contrail> playerContrails = jetLagPlayer.jetlag$getContrails();
        playerContrails.forEach(ContrailRenderer::renderContrails);
    }

    /**
     * Render a contrail's points. The main method to be called.
     *
     * @param contrail The contrail to be rendered
     */
    public static void renderContrails(Contrail contrail) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();

        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);
        RenderSystem.setShaderTexture(0, CONTRAIL_TEXTURE);
//        RenderSystem.enableDepthTest(); //I have no clue what these 2 commented out things here do
//        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 0.3f);
        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().enable();

        Tessellator tessellator = Tessellator.getInstance();
        renderContrail(matrixStack, tessellator, contrail);
        renderTest(matrixStack, tessellator);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().disable();

        matrixStack.pop();
    }

    private static void renderTest(MatrixStack matrixStack, Tessellator tessellator) {

        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

        Vec3d point1 = new Vec3d(0, -58, 0);
        Vec3d point2 = new Vec3d(0, -58, -5);
        Vec3d point3 = new Vec3d(5, -58, -5);
        Vec3d point4 = new Vec3d(5, -58, -10);
        Vec3d point5 = new Vec3d(10, -58, -10);
        Vec3d point6 = new Vec3d(10, -58, -15);
        Vec3d point7 = new Vec3d(0, -58, -13);
        Vec3d point8 = new Vec3d(5, -56, -16);
        List<Vec3d> points = Lists.newArrayList();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(new Vec3d(5, -58, -20));
        points.add(new Vec3d(0, -58, -20));
        points.add(new Vec3d(-3, -58, -10));
        points.add(point7);
        points.add(point8);
        points.add(new Vec3d(5, -58, -25));
        points.add(new Vec3d(5, -53, -25));

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
//        int curvePoints = JetLagConfig.getInstance().airStreakCurvePoints;
//        for (int i = 0; i < points.size() - 1; i++) {
//            Vec3d prevPoint = points.get(i != 0 ? i - 1 : 0);
//            Vec3d originPoint = points.get(i);
//            Vec3d targetPoint = points.get(i + 1);
//            Vec3d targetPoint = points.get(i + 2 <= points.size() - 1 ? i + 2 : i + 1);
//
//            Vec3d prevCurvePoint = prevPoint;
//            for (int j = 0; j <= curvePoints; j++) {
//                float delta = 1f / curvePoints * j;
//
//                float curveX = MathHelper.catmullRom(delta, (float) prevPoint.getX(), (float) originPoint.getX(), (float) targetPoint.getX(), (float) targetPoint.getX());
//                float curveY = MathHelper.catmullRom(delta, (float) prevPoint.getY(), (float) originPoint.getY(), (float) targetPoint.getY(), (float) targetPoint.getY());
//                float curveZ = MathHelper.catmullRom(delta, (float) prevPoint.getZ(), (float) originPoint.getZ(), (float) targetPoint.getZ(), (float) targetPoint.getZ());
//                Vec3d curvePoint = new Vec3d(curveX, curveY, curveZ);
//                if(delta > 0f) {
//                    renderSegment(matrixStack, buffer, curvePoint, prevCurvePoint, 1f);
//                }
//                prevCurvePoint = curvePoint;
//            }
//        }

        renderList(matrixStack, buffer, points);

        tessellator.draw();
    }

    private static void renderContrail(MatrixStack matrixStack, Tessellator tessellator, Contrail contrail) {
        matrixStack.push();
        if(contrail != null) {
            //It is EXTREMELY important that TRIANGLES_STRIP is used, as it has "shareVertices" option enabled,
            //allowing the drawn line here to be connected seamlessly.
            //It is also important the teh tessellator gets drawn individually for each list,
            //as not doing so would connect the two drawn lists together, forming an "N" shape
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
            //renders all left wing points
            List<Vec3d> leftPoints = contrail.getLeftPoints();
            renderList(matrixStack, buffer, leftPoints);
            tessellator.draw();

            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
            //renders all right wing points
            List<Vec3d> rightPoints = contrail.getRightPoints();
            renderList(matrixStack, buffer, rightPoints);
            tessellator.draw();
        }
        matrixStack.pop();
    }

    /**
     * Renders a connected line of Vec3d's given in a list. The list is rendering in order starting from 0(oldest point). Called by each set of player contrails.
     * @param matrixStack The MatrixStack used in rendering.
     * @param buffer The BufferBuilder used in rendering
     * @param points The list of Vec3d points to be rendered.
     */
    private static void renderList(MatrixStack matrixStack, BufferBuilder buffer, List<Vec3d> points) {
        matrixStack.push();
        int curvePoints = getInstance().contrailCurvePoints;

        int fadeoutPoints = getInstance().fadeOutPoints;
        int fadeinPoints = getInstance().fadeInPoints;

        float opacity = 0f;
        if(fadeoutPoints <= 0) {
            opacity = 1f;
        }
        for (int i = 0; i < points.size() - 1; i++) {
            Vec3d prevPoint = points.get(i != 0 ? i - 1 : 0);
            Vec3d originPoint = points.get(i);
            Vec3d targetPoint = points.get(i + 1);
            Vec3d nextPoint = points.get(i + 2 <= points.size() - 1 ? i + 2 : i + 1);

            Vec3d prevCurvePoint = prevPoint;

            //light adjustment
            int originBlockLight = getLightLevel(LightType.BLOCK, originPoint);
            int targetBlockLight = getLightLevel(LightType.BLOCK, targetPoint);
            int originSkyLight = getLightLevel(LightType.SKY, originPoint);
            int targetSkyLight = getLightLevel(LightType.SKY, targetPoint);

            for (int j = 0; j <= curvePoints; j++) {
                float delta = (float) j / curvePoints;

                //easing/interpolation
                float curveX = MathHelper.catmullRom(delta, (float) prevPoint.getX(), (float) originPoint.getX(), (float) targetPoint.getX(), (float) nextPoint.getX());
                float curveY = MathHelper.catmullRom(delta, (float) prevPoint.getY(), (float) originPoint.getY(), (float) targetPoint.getY(), (float) nextPoint.getY());
                float curveZ = MathHelper.catmullRom(delta, (float) prevPoint.getZ(), (float) originPoint.getZ(), (float) targetPoint.getZ(), (float) nextPoint.getZ());
                Vec3d curvePoint = new Vec3d(curveX, curveY, curveZ);

                //light adjustment per segment
//                int blockLight = MathHelper.lerp(delta, originBlockLight, targetBlockLight);
//                int skyLight = MathHelper.lerp(delta, originSkyLight, targetSkyLight);
//                int light = LightmapTextureManager.pack(blockLight, skyLight);

                //at midnight with no block light around, this equals 15728640 (blockLight = 0, skyLight = 15)
                int skyLight = originSkyLight;
                int blockLight = originBlockLight;
                int light = skyLight << 20 | blockLight << 4;

                if(delta > 0f) {
                    renderSegment(matrixStack, buffer, curvePoint, prevCurvePoint, opacity, light);
                }
                prevCurvePoint = curvePoint;
            }

            //fading stuff
            //fading out takes priority over fading in
            if(i <= fadeoutPoints && fadeoutPoints != 0) { //fade out
                opacity = (float) i / fadeoutPoints;
            } else if(i >= points.size() - fadeinPoints) { //fade in
                opacity = (float) (points.size() - i) / fadeinPoints;
            }

            //lower opacity values get discarded by Minecraft,
            //so this ensures that all points still get rendered to some degree
            if(opacity <= 0.1f) {
                opacity = 0.11f;
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
    private static void renderSegment(MatrixStack matrixStack, BufferBuilder buffer, Vec3d origin, Vec3d target, float opacity, int light) {
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

        float width = (float) getInstance().contrailWidth;

        drawTriangle(matrixStack.peek().getPositionMatrix(), buffer, width, -length, opacity, light);

        matrixStack.pop();
    }

    /**
     * Renders a triangle with a specific width and length. For Contrail rendering, this is called after the MatrixStack has been translated/rotated.
     *
     * @param matrix The MatrixStack's Position Matrix used for rendering.
     * @param buffer The BufferBuilder used for rendering
     * @param width The rendered rectangle's width. Should be determined by a config option.
     * @param length The rendered rectangle's length. Should be determined by the length from the origin point to the target point in {@link #renderSegment(MatrixStack, BufferBuilder, Vec3d, Vec3d, float, int)}.
     * @param opacity The rendered rectangle's opacity/alpha value.
     */
    private static void drawTriangle(Matrix4f matrix, BufferBuilder buffer, float width, float length, float opacity, int light) {
        buffer.vertex(matrix, width, 0, length).color(1f, 1f, 1f, opacity).texture(0f, 0f).light(light).next();
        buffer.vertex(matrix, 0, 0, length).color(1f, 1f, 1f, opacity).texture(0f, 0f).light(light).next();
    }

    private static int getLightLevel(LightType lightType, Vec3d pos) {
        BlockPos blockPos = new BlockPos((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
        return MinecraftClient.getInstance().world.getLightLevel(lightType, blockPos);
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
