package net.superkat.jetlag.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.superkat.jetlag.airstreak.AirStreakHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class TestMixin extends AbstractClientPlayerEntity {
//    private final BiFunction<Vec3d, Entity, Vec3d> offset;


    @Shadow public abstract float getYaw(float tickDelta);

    public TestMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tickMovement", at = @At("RETURN"))
    public void jetLag$test(CallbackInfo ci) {
        if(this.isFallFlying()) {
            AirStreakHandler.spawnInParticlesAtElytraTips((AbstractClientPlayerEntity) (Object) this);
        }
    }
}
