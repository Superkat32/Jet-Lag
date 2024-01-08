package net.superkat.jetlag.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public class InGameHudMixin {
//	private static final SpeedlineRenderer lineRenderer = new SpeedlineRenderer();
//	@Inject(method = "render", at = @At("RETURN"))
//	private void init(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
//		lineRenderer.renderTestItem(matrices);
//	}
}