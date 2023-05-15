package net.superkat.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.superkat.SpeedlineRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	private static final SpeedlineRenderer lineRenderer = new SpeedlineRenderer();
	@Inject(method = "render", at = @At("RETURN"))
	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		lineRenderer.renderTestItem(matrices);
	}
}