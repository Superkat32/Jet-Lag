package net.superkat.jetlag.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {


//    @ModifyExpressionValue(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingRiptide()Z"))
//    private boolean test(boolean original) {
//        return true;
//    }

    @Inject(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/RotationAxis;rotation(F)Lorg/joml/Quaternionf;"), cancellable = true)
    public void reomveRoll(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo ci) {
//        ContrailHandler.getPlayerRoll(abstractClientPlayerEntity, h);
//        ci.cancel();
    }

//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void addAirStreakRenderLayer(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
//
//    }

//    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
//    public void captureWingAngle(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//        if(livingEntity instanceof ClientPlayerEntity) {
////            ContrailRenderer.renderAirStreak(matrixStack, vertexConsumerProvider, f, g, livingEntity);
////        }
//    }



}
