package net.superkat.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.superkat.SpeedlineRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Environment(EnvType.CLIENT)
//@Mixin(ClientPlayerEntity.class)
@Mixin(InGameHud.class)
public class ExampleMixin {


//	public ExampleMixin(ClientWorld world, GameProfile profile) {
//		super(world, profile);
//	}
//
//	@Shadow public abstract void tick();
//
//	@Shadow public abstract void tickMovement();
//
//	@Shadow private boolean falling;
	//	@Shadow protected abstract void renderOverlay(MatrixStack matrices, Identifier texture, float opacity);
//
//	@Shadow @Final private Random random;
//	private int scaledWidth;
//	private int scaledHeight;


	private static final SpeedlineRenderer lineRenderer = new SpeedlineRenderer();
//	ExampleModClient exampleModClient;
//	@Inject(method = "onTrackedDataSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getSoundManager()Lnet/minecraft/client/sound/SoundManager;"))
	@Inject(method = "render", at = @At("RETURN"))
	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
//		LOGGER.info("mixin activated");
		lineRenderer.renderTestItem(matrices);
//		LOGGER.info(String.valueOf(this.getAbilities().getFlySpeed()));

//		renderSpeedlineOverlay(defaultTexture, 1, 1);
//		renderSpeedlineOverlay(defaultTexture, 1, 2);
//		renderSpeedlineOverlay(defaultTexture, 1, 3);
//		renderSpeedlineOverlay(defaultTexture, 1, 4);
//		renderSpeedlineOverlay(defaultTexture, 1, 5);
//		renderSpeedlineOverlay(defaultTexture, 1, 6);
//		renderSpeedlineOverlay(defaultTexture, 1, 7);
//		renderSpeedlineOverlay(defaultTexture, 1, 8);
//		renderSpeedlineOverlay(defaultTexture, 1, 9);
//		renderSpeedlineOverlay(defaultTexture, 1, 10);
//		renderSpeedlineOverlay(defaultTexture, 1, 11);
//		renderSpeedlineOverlay(defaultTexture, 1, 12);
//		renderSpeedlineOverlay(defaultTexture, 1, 13);
//		renderSpeedlineOverlay(defaultTexture, 1, 14);
//		renderSpeedlineOverlay(defaultTexture, 1, 15);
//		renderSpeedlineOverlay(defaultTexture, 1, 16);
//		renderSpeedlineOverlay(matrices, defaultTexture, 1, 100);
//		renderSpeedlineOverlay(matrices, defaultTexture, 1, 2);
//		renderSpeedlineOverlay(matrices, defaultTexture, 1, 3);
//		renderSpeedlineOverlay(matrices, defaultTexture, 1, 4);
//		ExampleMod.LOGGER.info("rendering!");
	}
}