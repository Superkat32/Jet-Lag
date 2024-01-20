package net.superkat.jetlag.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import net.superkat.jetlag.airstreak.AirStreakHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraEntityModel.class)
public class ElytraEntityModelMixin {

    @Shadow @Final private ModelPart rightWing;

    @Shadow @Final private ModelPart leftWing;

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void changeElytraRotation(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        AirStreakHandler.changeElytraRotation(leftWing, rightWing);
    }

}
