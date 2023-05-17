package net.superkat.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ElytraEntityModel.class)
public abstract class OtherTestMixin <T extends LivingEntity> extends AnimalModel<T>  {

    @Shadow @Final private ModelPart leftWing;

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void init(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
//        LOGGER.info("OtherTestMixin activated!");
        if (livingEntity.isFallFlying()) {
            if (livingEntity instanceof AbstractClientPlayerEntity clientPlayerEntity) {
                double wingOffset = 0.5; // adjust this value to move the particles closer or further from the wings

                // Calculate the position of the left and right wing vertices
                double[] leftWingVertex = new double[]{clientPlayerEntity.getX() - Math.sin(Math.toRadians(clientPlayerEntity.getYaw())) * wingOffset, clientPlayerEntity.getY() + 1.5, clientPlayerEntity.getZ() + Math.cos(Math.toRadians(clientPlayerEntity.getYaw())) * wingOffset};
                double[] rightWingVertex = new double[]{clientPlayerEntity.getX() + Math.sin(Math.toRadians(clientPlayerEntity.getYaw())) * wingOffset, clientPlayerEntity.getY() + 1.5, clientPlayerEntity.getZ() - Math.cos(Math.toRadians(clientPlayerEntity.getYaw())) * wingOffset};

                // Calculate the offset vector from the player's position to the left and right wing vertices
                double[] leftOffset = new double[]{leftWingVertex[0] - clientPlayerEntity.getX(), leftWingVertex[1] - clientPlayerEntity.getY(), leftWingVertex[2] - clientPlayerEntity.getZ()};
                double[] rightOffset = new double[]{rightWingVertex[0] - clientPlayerEntity.getX(), rightWingVertex[1] - clientPlayerEntity.getY(), rightWingVertex[2] - clientPlayerEntity.getZ()};

                // Calculate the pitch and yaw angles of the player
                // Calculate the offset based on the player's pitch and yaw
                double pitch = Math.tan(Math.toRadians(clientPlayerEntity.getPitch())) * Math.cos(Math.toRadians(clientPlayerEntity.getYaw())) * wingOffset;
                double yaw = Math.toRadians(clientPlayerEntity.getYaw());

                // Calculate the offset for the particles based on the pitch and yaw of the player
                double xOffset = -Math.sin(yaw) * Math.cos(pitch);
                double yOffset = Math.sin(pitch) * wingOffset;
                double zOffset = Math.cos(yaw) * Math.cos(pitch);

                // Spawn particles behind the left wing
                clientPlayerEntity.world.addParticle(ParticleTypes.END_ROD, leftWingVertex[0] + xOffset, leftWingVertex[1] + yOffset, leftWingVertex[2] + zOffset, 0.0, 0.0, 0.0);

                // Spawn particles behind the right wing
                clientPlayerEntity.world.addParticle(ParticleTypes.END_ROD, rightWingVertex[0] + xOffset, rightWingVertex[1] + yOffset, rightWingVertex[2] + zOffset, 0.0, 0.0, 0.0);
            }
        }
    }
}
