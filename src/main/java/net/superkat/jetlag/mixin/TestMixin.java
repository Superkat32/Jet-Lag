package net.superkat.jetlag.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraEntityModel.class)
public abstract class TestMixin <T extends LivingEntity> extends AnimalModel<T>  {

    @Shadow @Final private ModelPart leftWing;

    @Shadow @Final private ModelPart rightWing;

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void init(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
//        LOGGER.info("OtherTestMixin activated!");
        if (livingEntity.isFallFlying()) {
            if (livingEntity instanceof AbstractClientPlayerEntity clientPlayerEntity) {
//                MinecraftClient mc = MinecraftClient.getInstance();
                Vec3d playerPos = livingEntity.getPos();
                Vec3d eyePos = new Vec3d(playerPos.x, playerPos.y + livingEntity.getEyeHeight(livingEntity.getPose()), playerPos.z);
                Vec3d motion = livingEntity.getVelocity();
//                Vec3d facingVector = new Vec3d(-motion.z, 0, motion.x).normalize().multiply(2.0);
                Vec3d facingVector = null;

                if (motion.lengthSquared() > 0.0) {
                    facingVector = new Vec3d(-motion.z, 0, motion.x).normalize().multiply(2.0);
                } else {
                    facingVector = new Vec3d(0, 0, 2.0);
                }
//                facingVector = rotateVectorY(facingVector, 90.0);

                Vec3d particlePos = eyePos.add(facingVector);
                livingEntity.world.addParticle(ParticleTypes.END_ROD, particlePos.x, particlePos.y, particlePos.z, 0.001, 0.001, 0.001);
            }
        }
    }

    private static Vec3d rotateVectorY(Vec3d vec, double degrees) {
        double radian = Math.toRadians(degrees);
        double newX = vec.x * Math.cos(radian) - vec.z * Math.sin(radian);
        double newZ = vec.x * Math.sin(radian) + vec.z * Math.cos(radian);
        return new Vec3d(newX, vec.y, newZ);
    }
}
