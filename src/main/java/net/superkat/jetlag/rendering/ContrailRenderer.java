package net.superkat.jetlag.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
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
import net.superkat.jetlag.contrail.ContrailHandler;
import net.superkat.jetlag.contrail.JetLagPlayer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.include.com.google.common.collect.Lists;
import java.awt.*;
import java.util.List;
import static net.superkat.jetlag.config.JetLagConfig.getInstance;

public class ContrailRenderer {
    public static final Identifier CONTRAIL_TEXTURE = new Identifier(JetLagMain.MOD_ID, "textures/contrail.png");

    public static final RenderLayer CONTRAIL = RenderLayer.of(
            "contrail",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
            VertexFormat.DrawMode.TRIANGLE_STRIP,
            1536,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(RenderPhase.TRIPWIRE_PROGRAM)
                    .texture(new RenderPhase.Texture(CONTRAIL_TEXTURE, false, false))
                    .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                    .build(false)
    );

    public static void contrailWorldRendering(WorldRenderContext context) {
        if(getInstance().modEnabled) {
            renderAllContrails(context);
        }
    }

    /**
     * Render all the contrails from
     *
     * @param context The WorldRenderContext
     */
    public static void renderAllContrails(WorldRenderContext context) {
//        ContrailHandler.contrails.forEach(contrail -> renderContrail(contrail, context));
        ContrailHandler.contrails.values().forEach(contrails -> {
            contrails.forEach(contrail -> renderContrail(contrail, context));
        });
    }

    /**
     * Render a player's contrail's points.
     *
     * @param player Renders this player's existing contrails
     */
    public static void renderContrails(AbstractClientPlayerEntity player, WorldRenderContext context) {
        JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
        List<Contrail> playerContrails = jetLagPlayer.jetlag$getContrails();
        playerContrails.forEach(contrail -> renderContrail(contrail, context));
    }

    /**
     * Render a contrail's points and sets the rendering settings. The main method to be called.
     *
     * @param contrail The contrail to be rendered
     */
    public static void renderContrail(Contrail contrail, WorldRenderContext context) {
        MatrixStack matrices = context.matrixStack();
        matrices.push();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        Color contrailColor = getInstance().contrailColor;
        RenderSystem.setShaderColor(contrailColor.getRed() / 255f, contrailColor.getGreen() / 255f, contrailColor.getBlue() / 255f, contrailColor.getAlpha() / 255f);
        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().enable();

        VertexConsumerProvider consumers = context.consumers();

        renderContrail(contrail, matrices, consumers);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().disable();

        matrices.pop();
    }

    private static void renderTest(MatrixStack matrixStack, VertexConsumerProvider consumers) {

//        BufferBuilder buffer = tessellator.getBuffer();
//        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

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

        VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLeash());
        renderList(matrixStack, vertexConsumer, points);

//        tessellator.draw();
    }

    /**
     * Renders a contrail.
     *
     * @param contrail The contrail to be rendered.
     * @param matrixStack The MatrixStack to use.
     * @param consumers The VertexConsumerProvider to use.
     */
    private static void renderContrail(Contrail contrail, MatrixStack matrixStack, VertexConsumerProvider consumers) {
        matrixStack.push();
        if(contrail != null) {
            //The VertexConsumerProvider given by the WorldRenderContext is the same as getting the
            //MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            //which returns a VertexConsumerProvider.Immediate
            VertexConsumerProvider.Immediate immediate = (VertexConsumerProvider.Immediate) consumers;

            //It is EXTREMELY important that TRIANGLES_STRIP is used on the RenderLayer,
            //as it has "shareVertices" option enabled allowing the drawn line here to be connected seamlessly.

            //It is also important the Immediate gets drawn individually for each list,
            //as not doing so would connect the two drawn lists together, forming an "N" shape

            //Two different VertexConsumers are used instead of one because
            //the "getBuffer" method calls some extra methods for drawing/beginning the rendering

            //renders all left wing points
            VertexConsumer leftVertexConsumer = immediate.getBuffer(CONTRAIL);
            List<Vec3d> leftPoints = contrail.getLeftPoints();
            List<Float> leftOpacity = contrail.getLeftOpacity();
            renderList(matrixStack, leftVertexConsumer, leftPoints, leftOpacity);
            immediate.draw(CONTRAIL);

            //renders all right wing points
            VertexConsumer rightVertexConsumer = immediate.getBuffer(CONTRAIL);
            List<Vec3d> rightPoints = contrail.getRightPoints();
            List<Float> rightOpacity = contrail.getRightOpacity();
            renderList(matrixStack, rightVertexConsumer, rightPoints, rightOpacity);
            immediate.draw(CONTRAIL);
        }
        matrixStack.pop();
    }

    /**
     * Renders a connected line of Vec3d's given in a list. The list is rendering in order starting from 0(oldest point). Called by each set of player contrails.
     * @param matrixStack The MatrixStack used in rendering.
     * @param vertexConsumer The VertexConsumer used in rendering
     * @param points The list of Vec3d points to be rendered.
     */
    private static void renderList(MatrixStack matrixStack, VertexConsumer vertexConsumer, List<Vec3d> points) {
        renderList(matrixStack, vertexConsumer, points, null);
    }

    /**
     * Renders a connected line of Vec3d's given in a list. The list is rendering in order starting from 0(oldest point). Called by each set of player contrails.
     * @param matrixStack The MatrixStack used in rendering.
     * @param vertexConsumer The VertexConsumer used in rendering
     * @param points The list of Vec3d points to be rendered.
     * @param opacityAdjustment A list matching the size of the points, used for adjusting the opacity per point
     */
    private static void renderList(MatrixStack matrixStack, VertexConsumer vertexConsumer, List<Vec3d> points, @Nullable List<Float> opacityAdjustment) {
        matrixStack.push();
        int curvePoints = getInstance().contrailCurvePoints;

        int fadeoutPoints = getInstance().fadeOutPoints;
        int fadeinPoints = getInstance().fadeInPoints;

        float opacity = 0f;
        if(fadeinPoints <= 0) {
            opacity = 1f;
        }

        float contrailAlpha = getInstance().contrailColor.getAlpha() / 255f;
        float minOpacity = 0.11f + (1f - contrailAlpha);

        boolean shouldAdjustOpacity = opacityAdjustment != null;

        float width = (float) getInstance().contrailWidth;
        float widthAdd = (float) (getInstance().contrailWidthAddition / 10f);

        for (int i = 0; i < points.size() - 1; i++) {
            //Catmull spline points
            Vec3d prevPoint = points.get(i != 0 ? i - 1 : 0);
            Vec3d originPoint = points.get(i);
            Vec3d targetPoint = points.get(i + 1);
            Vec3d nextPoint = points.get(i + 2 <= points.size() - 1 ? i + 2 : i + 1);

            //Extra point used for catmull spline during smoothing
            Vec3d prevCurvePoint = prevPoint;

            //light adjustment - interpolates between the origin and target points
            int originBlockLight = getLightLevel(LightType.BLOCK, originPoint);
            int targetBlockLight = getLightLevel(LightType.BLOCK, targetPoint);
            int originSkyLight = getLightLevel(LightType.SKY, originPoint);
            int targetSkyLight = getLightLevel(LightType.SKY, targetPoint);

            float originOpacityAdjustment = 0f;
            float targetOpacityAdjustment = 0f;

            if(shouldAdjustOpacity) {
                originOpacityAdjustment = opacityAdjustment.get(i);
                targetOpacityAdjustment = opacityAdjustment.get(i + 1);
            }

            for (int j = 0; j <= curvePoints; j++) {
                float delta = (float) j / curvePoints;

                //easing/interpolation
                float curveX = MathHelper.catmullRom(delta, (float) prevPoint.getX(), (float) originPoint.getX(), (float) targetPoint.getX(), (float) nextPoint.getX());
                float curveY = MathHelper.catmullRom(delta, (float) prevPoint.getY(), (float) originPoint.getY(), (float) targetPoint.getY(), (float) nextPoint.getY());
                float curveZ = MathHelper.catmullRom(delta, (float) prevPoint.getZ(), (float) originPoint.getZ(), (float) targetPoint.getZ(), (float) nextPoint.getZ());
                Vec3d curvePoint = new Vec3d(curveX, curveY, curveZ);

                //light adjustment per segment
                int blockLight = MathHelper.lerp(delta, originBlockLight, targetBlockLight);
                int skyLight = MathHelper.lerp(delta, originSkyLight, targetSkyLight);
                int light = LightmapTextureManager.pack(blockLight, skyLight);

                //opacity randomness per point
                float opacityAdjust = MathHelper.lerp(delta, originOpacityAdjustment, targetOpacityAdjustment);
                float usedOpacity = MathHelper.clamp(opacity + opacityAdjust, minOpacity, 1f);

                if(delta > 0f) {
                    renderSegment(matrixStack, vertexConsumer, curvePoint, prevCurvePoint, usedOpacity, light, width);
                }
                prevCurvePoint = curvePoint;
            }

            //contrail fading - combines both fade in and fade out (i can't believe this worked lol)
            boolean fadeIn = i <= fadeinPoints && fadeinPoints != 0;
            float fadeInOpacity = contrailAlpha;
            boolean fadeOut = i >= points.size() - fadeoutPoints;
            float fadeOutOpacity = contrailAlpha;

            if(fadeIn) {
                fadeInOpacity = (float) i / fadeinPoints;
            }

            if(fadeOut) {
                fadeOutOpacity = (float) (points.size() - i) / fadeoutPoints;
            }

            opacity = MathHelper.lerp((float) i / points.size(), fadeInOpacity, fadeOutOpacity);

            //lower opacity values get discarded by Minecraft's rendering,
            //so this ensures that all points have a better chance of getting rendered at lower opacity
            //(not guaranteed but helpful I think)
            if(opacity <= minOpacity) {
                opacity = minOpacity + 0.01f;
            }

            width += widthAdd;
        }
        matrixStack.pop();
    }

    /**
     * Translates and rotates the MatrixStack. Also calculates the to-be rendered line's width and length.
     *
     * @param matrixStack The MatrixStack used for rendering.
     * @param vertexConsumer The VertexConsumer used for rendering.
     * @param origin The starting point to render from.
     * @param target The ending point to render to.
     * @param opacity The rendered segment's opacity/alpha value.
     */
    private static void renderSegment(MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d origin, Vec3d target, float opacity, int light, float width) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d transformedMatrixPos = origin.subtract(camera.getPos());
        matrixStack.push();

        //offsets to the origin's pos
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

        drawTriangle(matrixStack.peek().getPositionMatrix(), vertexConsumer, width, -length, opacity, light);

        matrixStack.pop();
    }

    /**
     * Renders a triangle with a specific width and length. For Contrail rendering, this is called after the MatrixStack has been translated/rotated.
     *
     * @param matrix The MatrixStack's Position Matrix used for rendering.
     * @param vertexConsumer The VertexConsumer used for rendering
     * @param width The rendered rectangle's width. Should be determined by a config option.
     * @param length The rendered rectangle's length. Should be determined by the length from the origin point to the target point in {@link #renderSegment(MatrixStack, VertexConsumer, Vec3d, Vec3d, float, int, float)}.
     * @param opacity The rendered rectangle's opacity/alpha value.
     */
    private static void drawTriangle(Matrix4f matrix, VertexConsumer vertexConsumer, float width, float length, float opacity, int light) {
        //dividing the width by 2 to ensure that the width given is accurately rendered
        vertexConsumer.vertex(matrix, width / 2f, 0, length).color(1f, 1f, 1f, opacity).texture(0f, 0f).light(light).next();
        vertexConsumer.vertex(matrix, -width / 2f, 0, length).color(1f, 1f, 1f, opacity).texture(1f, 1f).light(light).next();
    }

    private static int getLightLevel(LightType lightType, Vec3d pos) {
        BlockPos blockPos = new BlockPos((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
        if(MinecraftClient.getInstance().world != null) {
            return MinecraftClient.getInstance().world.getLightLevel(lightType, blockPos);
        }
        return LightmapTextureManager.pack(7, 15);
    }
}
