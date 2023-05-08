package net.superkat.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.superkat.ExampleMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class ExampleMixin {
	@Shadow protected abstract void renderOverlay(MatrixStack matrices, Identifier texture, float opacity);

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Identifier;F)V"))
	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		renderOverlay(matrices, new Identifier("jetlag", "textures/particle/myparticletexture.png"), 1);
		ExampleMod.LOGGER.info("rendering!");
	}
}