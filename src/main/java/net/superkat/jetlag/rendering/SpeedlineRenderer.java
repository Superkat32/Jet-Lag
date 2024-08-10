package net.superkat.jetlag.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.JetLagMain;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class SpeedlineRenderer {
    public static final Identifier SPEEDLINE = new Identifier(JetLagMain.MOD_ID, "blank");

    /**
     * Draw a pixelated line using the Bresenham's line algorithm.
     *
     * @param context The DrawContext to use.
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @param size The pixel size.
     * @param color The rendered color.
     */
    private static void drawPixelLine(DrawContext context, int x1, int y1, int x2, int y2, int size, Color color) {
        //technically allows width but KILLS performance
//        float width = 5f;
//
//        int dx = Math.abs(x2 - x1);
//        int dy = Math.abs(y2 - y1);
//        int sx = x1 < x2 ? 1 : -1;
//        int sy = y1 < y2 ? 1 : -1;
//
//        int err = dx - dy;
//        float ed = dx + dy == 0 ? 1 : (float) Math.sqrt((float) (dx * dx) + (float) (dy * dy));
//        int e2;
//        int x3;
//        int y3;
//
//        width = (width + 1f) / 2f;
//        while (true) {
//            context.fill(x1, y1, x1 + 1, y1 + 1, color.getRGB());
//            e2 = err;
//            x3 = x1;
//            if(e2 * 2 >= -dx) {
//                for (e2 += dy, y3 = y1; e2 < ed*width && (y2 != y3 || dx > dy); e2 += dx) {
//                    y3 += sy;
//                    context.fill(x1, y3, x1 + 1, y3 + 1, color.getRGB());
//                }
//                if(x1 == x2) break;
//                e2 = err;
//                err -= dy;
//                x1 += sx;
//            }
//            if(e2 * 2 <= dy) {
//                for (e2 = dx-e2; e2 < ed*width && (x2 != x3 || dx < dy); e2 += dy) {
//                    x3 += sx;
//                    context.fill(x3, y1, x3 + 1, y1 + 1, color.getRGB());
//                }
//                if(y1 == y2) break;
//                err += dx;
//                y1 += sy;
//            }
//        }

        int dx = Math.abs(x2 - x1);
        int dy = -Math.abs(y2 - y1);

        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int err = dx + dy;
        int e2;

        while (true) {
            context.fill(x1, y1, x1 + size, y1 + size, color.getRGB());

            if (x1 == x2 && y1 == y2) break;
            e2 = err * 2;
            if(e2 >= dy) {
                err += dy;
                x1 += sx;
            }
            if(e2 <= dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

//    public static void speedlineRendering(DrawContext drawContext, float tickDelta) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if(getInstance().showSpeedlines && client.player.isFallFlying()) {
//
//            if(getInstance().onlyShowSpeedlinesInFirstPerson && !client.options.getPerspective().isFirstPerson()) return;
//
////            Identifier blank = new Identifier(JetLagMain.MOD_ID, "blank"); //debug texture for making the rainbow shader
//            Identifier speedline = new Identifier(JetLagMain.MOD_ID, "speedlinemain");
//
//            Color speedlinesColor = getInstance().speedlinesColor;
//            float alpha = speedlinesColor.getAlpha() / 255f;
//            if(getInstance().velocityBasedSpeedlinesOpacity) {
//                alpha = (float) MathHelper.clamp(client.player.getVelocity().length() / 3f, 0f, 1f);
//            }
//
//            RenderSystem.enableBlend(); //prevents overlaps like chat from ruining the opacity
//            RenderSystem.setShaderColor(speedlinesColor.getRed() / 255f, speedlinesColor.getGreen() / 255f, speedlinesColor.getBlue() / 255f, alpha);
//
//            if(getInstance().rainbowSpeedlines) {
//                drawSpeedline(drawContext, speedline, 0, 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), JetLagClient.rainbowShader);
//            } else { //fallback in case other mods mess with this method
//                drawContext.drawGuiTexture(speedline, 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight());
//            }
//
//            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//            RenderSystem.disableBlend();
//        }
//    }

//    private static void drawSpeedline(DrawContext context, Identifier texture, int x, int y, int z, int width, int height, ShaderProgram shaderProgram) {
//        int x2 = x + width;
//        int y2 = y + height;
//
//        Sprite sprite = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(texture);
//        Identifier trueTexture = sprite.getAtlasId();
//
//        float u1 = sprite.getMinU();
//        float u2 = sprite.getMaxU();
//        float v1 = sprite.getMinV();
//        float v2 = sprite.getMaxV();
//
//        RenderSystem.setShaderTexture(0, trueTexture);
//        RenderSystem.setShader(() -> shaderProgram);
//        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
//        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
//        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//        buffer.vertex(matrix4f, x, y, z).texture(u1, v1).next();
//        buffer.vertex(matrix4f, x, y2, z).texture(u1, v2).next();
//        buffer.vertex(matrix4f, x2, y2, z).texture(u2, v2).next();
//        buffer.vertex(matrix4f, x2, y, z).texture(u2, v1).next();
//        BufferRenderer.drawWithGlobalProgram(buffer.end());
//    }
}
