package net.superkat.jetlag.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityRenderer.class)
public class PlayerEntityRendererMixin {

//    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
//    public void captureWingAngle(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//        if(livingEntity instanceof ClientPlayerEntity) {
////            JetLagUtils.renderAirStreak(matrixStack, vertexConsumerProvider, f, g, livingEntity);
////        }
//    }



}
