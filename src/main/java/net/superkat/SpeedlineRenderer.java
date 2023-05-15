package net.superkat;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static net.superkat.ExampleMod.LOGGER;

@Environment(EnvType.CLIENT)
public class SpeedlineRenderer extends DrawableHelper {
    public Identifier defaultTexture = new Identifier("jetlag", "textures/overlay/texturefail.png");
    public Identifier line1 = new Identifier("jetlag", "textures/overlay/frame1.png");
    public Identifier line2 = new Identifier("jetlag", "textures/overlay/frame2.png");
    public Identifier line3 = new Identifier("jetlag", "textures/overlay/frame3.png");
    public Identifier line4 = new Identifier("jetlag", "textures/overlay/frame4.png");
    public Identifier line5 = new Identifier("jetlag", "textures/overlay/frame5.png");
    public Identifier line6 = new Identifier("jetlag", "textures/overlay/frame6.png");
    public Identifier line7 = new Identifier("jetlag", "textures/overlay/frame7.png");
    public Identifier line8 = new Identifier("jetlag", "textures/overlay/frame8.png");
    public Identifier line9 = new Identifier("jetlag", "textures/overlay/frame9.png");
    public Identifier line10 = new Identifier("jetlag", "textures/overlay/frame10.png");
    public Identifier line11 = new Identifier("jetlag", "textures/overlay/frame11.png");
    public Identifier line12 = new Identifier("jetlag", "textures/overlay/frame12.png");

    private static MinecraftClient client;
    public int tick = 0;
    public SpeedlineRenderer() {
        client = MinecraftClient.getInstance();
    }

    //TODO - Replace with config boolean later
    private boolean draw = true;
    public void renderTestItem(MatrixStack matrixStack) {
        //Only allows the mod to draw the speedlines if
        //the config boolean(currently draw boolean) is true
        //the player is flying with an elytra
        //the player isn't on the ground
        //the screen isn't anything(e.g. pause screen, inventory screen, chat screen, etc.)
        if (draw && client.player.isFallFlying() && !client.player.isOnGround() && client.currentScreen == null) {
            //The mod knows when to draw the speedlines depending on the player's velocity in any direction
            double velX = Math.abs(client.player.getVelocity().getX());
            double velY = Math.abs(client.player.getVelocity().getY());
            double velZ = Math.abs(client.player.getVelocity().getZ());
            if(velX >= 0.5 || velY >= 0.5 || velZ >= 0.5) {
                if(velX >= 1.25 || velY >= 1.25 || velZ >= 1.25) {
                    renderSpeedlineOverlay(matrixStack, defaultTexture, 0.75F);
                } else if(velX >= 1.1 || velY >= 1.1 || velZ >= 1.1) {
                    renderSpeedlineOverlay(matrixStack, defaultTexture, 0.40F);
                } else if(velX >= 0.85 || velY >= 0.85 || velZ >= 0.85) {
                    renderSpeedlineOverlay(matrixStack, defaultTexture, 0.25F);
                } else if(velX >= 0.7 || velY >= 0.7 || velZ >= 0.7) {
                    renderSpeedlineOverlay(matrixStack, defaultTexture, 0.15F);
                } else {
                    renderSpeedlineOverlay(matrixStack, defaultTexture, 0.07F);
                }
            }
        }
    }

    public void renderSpeedlineOverlay(MatrixStack matrices, Identifier texture, float opacity) {
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();
//        LOGGER.info(String.valueOf(client.player.getVelocity()));
        matrices.push();

        //Rendering animation
        //NOTE - There is a bug/side effect where if the player's FPS is less than 60, the animation slows down
        if(tick <= 70) {
//            LOGGER.info(String.valueOf(tick));
//            LOGGER.info("Texture switch tick: " + String.valueOf(textureSwitchTick));
            //The z position is set like that to allow the animation to be rendered AND changed every tick
            //The z position doesn't actually change ANYTHING according to my testing
            matrices.translate(0, 0, (System.currentTimeMillis() % 5000) / 5000F);
            tick++;
            //The animation part of the rendering
            //Works by detecting when the current tick is less than or equal to a pre-determined number
            //On the last tick of the last frame of animation, the tick number resets to repeat the animation loop
            //Somewhat janky, especially when playing the game on lower frame rates, but at least it works(better than what I could do otherwise)
            if(tick == 48) {
                tick = 0;
            }
            if(tick <= 4) {
                texture = line1;
            } else if (tick <= 8) {
                texture = line2;
            } else if (tick <= 12) {
                texture = line3;
            } else if (tick <= 16) {
                texture = line4;
            } else if (tick <= 20) {
                texture = line5;
            } else if (tick <= 24) {
                texture = line6;
            } else if (tick <= 28) {
                texture = line7;
            } else if (tick <= 32) {
                texture = line8;
            } else if (tick <= 36) {
                texture = line9;
            } else if (tick <= 40) {
                texture = line10;
            } else if (tick <= 44) {
                texture = line11;
            } else if (tick <= 48) {
                texture = line12;
            } else {
                texture = defaultTexture;
                LOGGER.warn("Default texture has been activated");
            }
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend(); // Required to allow the transparency to not act weird with chat and stuff
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(matrices, 0, 0, -90, 0.0F, 0.0F, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        matrices.pop();
    }
}
