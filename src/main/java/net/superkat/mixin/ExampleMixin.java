package net.superkat.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
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
	public Identifier speedlineTexture2 = new Identifier("jetlag", "textures/overlay/speedline2.png");
	public Identifier speedlineCenteredTexture = new Identifier("jetlag", "textures/overlay/speedlinecenter.png");
	public Identifier speedlineDebugTexture = new Identifier("jetlag", "textures/overlay/speedline1.png");
	private int scaledWidth;
	private int scaledHeight;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Identifier;F)V"))
	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		renderSpeedlineOverlay(matrices, speedlineTexture , 1, 0);
		renderSpeedlineOverlay(matrices, speedlineTexture2 , 1, 1);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 180F);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 270F);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 50F);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 315F);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 235F);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, 235F);
//		renderSpeedlineOverlay(matrices, speedlineTexture, 1, this.random.nextBetween(0, 360));
//		ExampleMod.LOGGER.info("rendering!");
	}

	public void renderSpeedlineOverlay(MatrixStack matrices, Identifier texture, float opacity, int direction) {
		matrices.push();

		switch(direction) {
			case 0 -> {
				matrices.translate((System.currentTimeMillis() % 5000) / 5000F * 2900, (System.currentTimeMillis() % 5000) / 5000F * 3100, 0);
			}
			case 1 -> {
				matrices.translate((System.currentTimeMillis() % 5000) / 5000F * -2900, (System.currentTimeMillis() % 5000) / 5000F * 3100, 0);
			}
		}


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