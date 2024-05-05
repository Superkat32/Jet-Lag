package net.superkat.jetlag.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ElytraEntityModel.class)
public class ElytraEntityModelMixin extends AnimalModelMixin{

    @Shadow @Final private ModelPart rightWing;
    @Shadow @Final private ModelPart leftWing;
    public ModelPart testEmitter;
    public LivingEntity entity;
    List<Vec3d> points = Lists.newArrayList();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ModelPart root, CallbackInfo ci) {
        this.testEmitter = root.getChild("trail_emitter_one");
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void changeElytraRotation(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
//        ContrailHandler.changeElytraRotation(leftWing, rightWing);
        if(testEmitter != null) {
            if(entity == null) {
                this.entity = livingEntity;
            }
        }
    }

    @Inject(method = "getTexturedModelData", at = @At("RETURN"))
    private static void injectTrailEmitters(CallbackInfoReturnable<TexturedModelData> cir, @Local ModelPartData modelPartData, @Local Dilation dilation) {
        modelPartData.addChild("trail_emitter_one",
                ModelPartBuilder.create().uv(34, 0).cuboid(-2.0F, 20.0F, 0.0F, 2f, 2.0F, 2.0F, dilation),
                ModelTransform.of(0.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (-Math.PI / 12))
        );
    }

    @Inject(method = "getBodyParts", at = @At("RETURN"), cancellable = true)
    public void injectTrailerEmitters2(CallbackInfoReturnable<Iterable<ModelPart>> cir) {
        ArrayList<ModelPart> parts = new ArrayList<>();

        Iterable<ModelPart> returnValues = cir.getReturnValue();
        for (ModelPart returnValue : returnValues) {
            parts.add(returnValue);
        }
//        parts.add(testEmitter); //adds the part to the model
        cir.setReturnValue(parts);
    }

    @Override
    protected void test(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if(entity != null) {
            if(entity.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.SPYGLASS) {
                if(entity instanceof ClientPlayerEntity clientPlayer) {
//                    if(clientPlayer instanceof JetLagClientPlayerEntity jetLagPlayer) {
//                        jetLagPlayer.addPoint(matrices, clientPlayer);
//                        jetLagPlayer.addPoint(clientPlayer.getPos());
//                        ContrailRenderer.renderAirStreaks(matrices, clientPlayer);
//                    }
                }
            }
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void setEmitterAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        testEmitter.pitch = rightWing.pitch;
        testEmitter.yaw = rightWing.yaw;
        testEmitter.roll = rightWing.roll;
    }
}
