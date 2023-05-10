package net.superkat.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.superkat.ExampleMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class ExampleMixin extends DrawableHelper {
	@Shadow protected abstract void renderOverlay(MatrixStack matrices, Identifier texture, float opacity);

	@Shadow @Final private Random random;
	public Identifier speedlineTexture = new Identifier("jetlag", "textures/overlay/speedline.png");
	public Identifier speedlineTexture2 = new Identifier("jetlag", "textures/overlay/speedline2.png");
	public Identifier speedlineCenteredTexture = new Identifier("jetlag", "textures/overlay/speedlinecenter.png");
	public Identifier line1 = new Identifier("jetlag", "textures/overlay/line1.png");
	public Identifier line2 = new Identifier("jetlag", "textures/overlay/line2.png");
	public Identifier line3 = new Identifier("jetlag", "textures/overlay/line3.png");
	public Identifier line4 = new Identifier("jetlag", "textures/overlay/line4.png");
	public Identifier speedlineDebugTexture = new Identifier("jetlag", "textures/overlay/speedline1.png");
	private int scaledWidth;
	private int scaledHeight;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Identifier;F)V"))
	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		renderSpeedlineOverlay(matrices, line1, 1, 1);
		renderSpeedlineOverlay(matrices, line2, 1, 3);
		renderSpeedlineOverlay(matrices, line3, 1, 5);
		renderSpeedlineOverlay(matrices, line4, 1, 7);
//		ExampleMod.LOGGER.info("rendering!");
	}

	public void renderSpeedlineOverlay(MatrixStack matrices, Identifier texture, float opacity, int path) {
		matrices.push();

		int xMovement = 0;
		int yMovement = 0;

		switch(path) {
			case 1 -> { // moves towards bottom-right
				xMovement = 2900;
				yMovement = 3100;
			}
			case 2 -> { // moves towards bottom
				xMovement = 0;
				yMovement = 3100;
			}
			case 3 -> { // moves towards bottom-left
				xMovement = -2900;
				yMovement = 3100;
			}
			case 4 -> { // moves towards left
				xMovement = -2900;
				yMovement = 0;
			}
			case 5 -> { // moves towards top-left
				xMovement = -2900;
				yMovement = -3100;
			}
			case 6 -> { // moves towards top
				xMovement = 0;
				yMovement = -3100;
			}
			case 7 -> { // moves towards top-right
				xMovement = 2900;
				yMovement = -3100;
			}
			case 8 -> { // moves towards right
				xMovement = 2900;
				yMovement = 0;
			}
			default -> { // fallback option
				xMovement = 2900;
				yMovement = 3100;
				ExampleMod.LOGGER.info("default case activated");
			}
		}

		matrices.translate((System.currentTimeMillis() % 5000) / 5000F * xMovement + this.random.nextBetween(-10, 10), (System.currentTimeMillis() % 5000) / 5000F * yMovement + this.random.nextBetween(-10, 10), 0);
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
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		RenderSystem.setShaderTexture(0, texture);
		drawTexture(matrices, 0, 0, -90, 0.0F, 0.0F, this.scaledWidth, this.scaledHeight, this.scaledWidth, this.scaledHeight);
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}