package net.superkat.jetlag.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.MooshroomMushroomFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.MooshroomEntity;
import net.superkat.jetlag.airstreak.AirStreakHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MooshroomMushroomFeatureRenderer.class)
public class MooshroomMushroomFeatureRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/MooshroomEntity;FFFFFF)V", at = @At("HEAD"))
    public void plsworkokthanks(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MooshroomEntity mooshroomEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        AirStreakHandler.test(matrixStack, mooshroomEntity);
    }
}
