package net.superkat.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
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
	public Identifier speedlineDebugTexture = new Identifier("jetlag", "textures/overlay/speedline1.png");
	private int scaledWidth;
	private int scaledHeight;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Identifier;F)V"))
	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 0F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 180F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 270F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 50F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 315F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 235F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 235F);
		renderSpeedlineOverlay(matrices, speedlineTexture, 1, this.random.nextBetween(0, 360));
//		ExampleMod.LOGGER.info("rendering!");
	}

	public void renderSpeedlineOverlay(MatrixStack matrices, Identifier texture, float opacity, float randomRotation) {
		matrices.push();

		// Randomly choose the initial position on the screen
		float posX = (float) (Math.random() * (this.scaledWidth / 2f)) + (this.scaledWidth / 4f);
		float posY = (float) (Math.random() * (this.scaledHeight / 2f)) + (this.scaledHeight / 4f);

		// Randomly choose the direction of movement
		float speedX = (float) (Math.random() * 2f) - 1f;
		float speedY = (float) (Math.random() * 2f) - 1f;

		// Normalize the speed to maintain the same speed regardless of direction
		float speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
		speedX /= speed;
		speedY /= speed;

		// Calculate the distance the texture will travel off-screen
		float distance = Math.max(this.scaledWidth, this.scaledHeight);

		// Set the initial translation to the center of the screen
		matrices.translate(posX, posY, 0f);

		// Move the texture off-screen in the chosen direction
		matrices.translate(distance * speedX, distance * speedY, 0f);

		// Rotate the texture if necessary
//		float randomRotation = (float) Math.random() * 360f;
		if (speedX > 0) {
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(randomRotation));
		} else {
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(randomRotation + 180f));
		}

		// Render the texture
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