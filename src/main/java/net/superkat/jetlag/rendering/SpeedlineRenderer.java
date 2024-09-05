package net.superkat.jetlag.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.superkat.jetlag.JetLagClient;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.config.JetLagConfig;
import org.joml.Matrix4f;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class SpeedlineRenderer {
    public static final Identifier SPEEDLINE = new Identifier(JetLagMain.MOD_ID, "speedlinemain");

    public static void speedlineRendering(DrawContext drawContext, float tickDelta) {
        JetLagConfig config = JetLagConfig.getInstance();
        if(!config.modEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if(config.showSpeedlinesTexture && client.player.isFallFlying()) {

            if(config.onlyShowSpeedlinesInFirstPerson && !client.options.getPerspective().isFirstPerson()) return;

//            Identifier blank = new Identifier(JetLagMain.MOD_ID, "blank"); //debug texture for making the rainbow shader
//            Identifier speedline = new Identifier(JetLagMain.MOD_ID, "speedlinemain");

            Color speedlinesColor = config.speedlinesColor;
            float alpha = speedlinesColor.getAlpha() / 255f;
            if(config.velocityBasedSpeedlinesOpacity) {
                alpha = (float) MathHelper.clamp(client.player.getVelocity().length() / 3f, 0f, 1f);
            }

            RenderSystem.enableBlend(); //prevents overlaps like chat from ruining the opacity
            RenderSystem.setShaderColor(speedlinesColor.getRed() / 255f, speedlinesColor.getGreen() / 255f, speedlinesColor.getBlue() / 255f, alpha);

            if(config.rainbowSpeedlines) {
                drawSpeedline(drawContext, SPEEDLINE, 0, 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), JetLagClient.rainbowShader);
            } else { //fallback in case other mods mess with this method
                drawContext.drawGuiTexture(SPEEDLINE, 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight());
            }

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableBlend();
        }
    }

    private static void drawSpeedline(DrawContext context, Identifier texture, int x, int y, int z, int width, int height, ShaderProgram shaderProgram) {
        int x2 = x + width;
        int y2 = y + height;

        Sprite sprite = MinecraftClient.getInstance().getGuiAtlasManager().getSprite(texture);
        Identifier trueTexture = sprite.getAtlasId();

        float u1 = sprite.getMinU();
        float u2 = sprite.getMaxU();
        float v1 = sprite.getMinV();
        float v2 = sprite.getMaxV();

        RenderSystem.setShaderTexture(0, trueTexture);
        RenderSystem.setShader(() -> shaderProgram);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrix4f, x, y, z).texture(u1, v1).next();
        buffer.vertex(matrix4f, x, y2, z).texture(u1, v2).next();
        buffer.vertex(matrix4f, x2, y2, z).texture(u2, v2).next();
        buffer.vertex(matrix4f, x2, y, z).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}
