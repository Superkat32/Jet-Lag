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
//    public Identifier line13 = new Identifier("jetlag", "textures/overlay/line13.png");
//    public Identifier line14 = new Identifier("jetlag", "textures/overlay/line14.png");
//    public Identifier line15 = new Identifier("jetlag", "textures/overlay/line15.png");
//    public Identifier line16 = new Identifier("jetlag", "textures/overlay/line16.png");
    public Identifier speedlineDebugTexture = new Identifier("jetlag", "textures/overlay/speedline1.png");

    private static MinecraftClient client;
    public int tick = 0;
    public int textureSwitchTick = 0;
    public int textureSwitchTickTime = 3;
    public SpeedlineRenderer() {
        client = MinecraftClient.getInstance();
    }

    private boolean draw = true;
    public void renderTestItem(MatrixStack matrixStack) {
        if (draw && client.player.isFallFlying() && !client.player.isOnGround() && client.currentScreen == null) {
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
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 1);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 2);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 3);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 4);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 5);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 6);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 7);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 8);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 9);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 10);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 11);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 12);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 13);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 14);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 15);
//            this.renderSpeedlineOverlay(matrixStack, defaultTexture, 1, 16);
//            LOGGER.info("rendering speedlines!");
//            int scaledWidth = client.getWindow().getScaledWidth();
//            int scaledHeight = client.getWindow().getScaledHeight();
//            matrixStack.push();
//
//            matrixStack.translate(40, 40, 0);
//            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((System.currentTimeMillis() % 5000) / 5000f * 360f));
//            matrixStack.translate(-40, -40, 0);
//
//            Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
//            Tessellator tessellator = Tessellator.getInstance();
//            BufferBuilder buffer = tessellator.getBuffer();
//
//            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
//            buffer.vertex(positionMatrix, 20, 20, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
//            buffer.vertex(positionMatrix, 20, 60, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
//            buffer.vertex(positionMatrix, 60, 60, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
//            buffer.vertex(positionMatrix, 60, 20, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();
//
//            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
//            RenderSystem.setShaderTexture(0, new Identifier("jetlag", "icon.png"));
//            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//
//            tessellator.draw();
//            matrixStack.pop();
        }
    }

    public void renderSpeedlineOverlay(MatrixStack matrices, Identifier texture, float opacity) {
//        MinecraftClient client = MinecraftClient.getInstance();
        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();
//        LOGGER.info(String.valueOf(client.player.getVelocity()));
//        MatrixStack matrices = new MatrixStack();
        matrices.push();

        int xMovement = 0;
        int yMovement = 0;

//        switch(path) {
//            case 1 -> {
//                xMovement = 500;
//                yMovement = 600;
//                texture = line1;
//            }
//            case 2 -> {
//                xMovement = 200;
//                yMovement = 600;
//                texture = line2;
//            }
//            case 3 -> {
//                xMovement = 75;
//                yMovement = 600;
//                texture = line3;
//            }
//            case 4 -> {
//                xMovement = 15;
//                yMovement = 600;
//                texture = line4;
//            }
//            case 5 -> {
//                xMovement = -300;
//                yMovement = 600;
//                texture = line5;
//            }
//            case 6 -> {
//                xMovement = -500;
//                yMovement = 600;
//                texture = line6;
//            }
//            case 7 -> {
//                xMovement = -500;
//                yMovement = 500;
//                texture = line7;
//            }
//            case 8 -> {
//                xMovement = -500;
//                yMovement = -600;
//                texture = line8;
//            }
//            case 9 -> {
//                xMovement = -500;
//                yMovement = -600;
//                texture = line9;
//            }
//            case 10 -> {
//                xMovement = -300;
//                yMovement = -600;
//                texture = line10;
//            }
//            case 11 -> {
//                xMovement = -115;
//                yMovement = -600;
//                texture = line11;
//            }
//            case 12 -> {
//                xMovement = 50;
//                yMovement = -600;
//                texture = line12;
//            }
//            case 13 -> {
//                xMovement = 150;
//                yMovement = -600;
//                texture = line13;
//            }
//            case 14 -> {
//                xMovement = 500;
//                yMovement = -600;
//                texture = line14;
//            }
//            case 15 -> {
//                xMovement = 500;
//                yMovement = -400;
//                texture = line15;
//            }
//            case 16 -> {
//                xMovement = 500;
//                yMovement = -50;
//                texture = line16;
//            }
////			case 3 -> { // moves towards bottom-left
////				xMovement = -2900;
////				yMovement = 3100;
////			}
////			case 4 -> { // moves towards left
////				xMovement = -2900;
////				yMovement = 0;
////			}
////			case 5 -> { // moves towards top-left
////				xMovement = -2900;
////				yMovement = -3100;
////			}
////			case 6 -> { // moves towards top
////				xMovement = 0;
////				yMovement = -3100;
////			}
////			case 7 -> { // moves towards top-right
////				xMovement = 2900;
////				yMovement = -3100;
////			}
////			case 8 -> { // moves towards right
////				xMovement = 2900;
////				yMovement = 0;
////			}
//            default -> { // fallback option
//                xMovement = 0;
//                yMovement = 0;
//                texture = defaultTexture;
//                LOGGER.warn("Default texture activated");
//            }
//        }

        //Rendering animation
        //NOTE - There is a bug/side effect where if the player's FPS is less than 60, the animation slows down
        if(tick <= 70) {
//            LOGGER.info(String.valueOf(tick));
//            LOGGER.info("Texture switch tick: " + String.valueOf(textureSwitchTick));
            matrices.translate(0, 0, (System.currentTimeMillis() % 5000) / 5000F);
            tick++;
            if(tick == 48) {
                tick = 0;
//                textureSwitchTick = 1;
            }
//            if(tick == textureSwitchTick) {
//                if(textureSwitchTick == 1) {
//                    textureSwitchTick += 4;
//                } else {
//                    textureSwitchTick += 5;
//                }
//                LOGGER.info("test");
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
//            }
//            if(tick == 50) {
//                tick = 0;
//                xMovement = xMovement;
//                yMovement = yMovement;
//            }
        }
//		matrices.translate((System.currentTimeMillis() % 5000) / 5000F * -xMovement, (System.currentTimeMillis() % 5000) / 5000F * -yMovement, 0);


//		switch(path) {
//			case 0 -> {
//				matrices.translate((System.currentTimeMillis() % 5000) / 5000F * 2900, (System.currentTimeMillis() % 5000) / 5000F * 3100, 0);
//			}
//			case 1 -> {
//				matrices.translate((System.currentTimeMillis() % 5000) / 5000F * -2900, (System.currentTimeMillis() % 5000) / 5000F * 3100, 0);
//			}
//		}
//		if(rotation >= 0 && rotation < 90) {
//			matrices.translate(0, 0, 0);
//			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
//			matrices.translate(0, 0, 0);
//		} else if (rotation >= 90 && rotation < 180) {
//			matrices.translate(210, 100, 0);
//			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
//			matrices.translate(-210, -100, 0);
//		} else if (rotation >= 180 && rotation < 270) {
//			matrices.translate(213, 120, 0);
//			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
//			matrices.translate(-213, -120, 0);
//		} else if (rotation >= 270 && rotation < 360) {
//			matrices.translate(330, 110, 0);
//			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
//			matrices.translate(-330, -110, 0);
//		} else {
//			ExampleMod.LOGGER.info("Not rendering speedline, doesn't meet any rotation degrees");
//		}

        //Use a bunch of if-else statements to determine which angle the texture should be at

//		matrices.translate(0, 0, 0);
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0));
//		matrices.translate(0, 0, 0);

//		matrices.translate(90, 90, 0);
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
//		matrices.translate(-90, -90, 0);

//		matrices.translate(213, 120, 0);
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
//		matrices.translate(-213, -120, 0);

//		matrices.translate(120, 120, 0);
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(270));
//		matrices.translate(-120, -120, 0);
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
