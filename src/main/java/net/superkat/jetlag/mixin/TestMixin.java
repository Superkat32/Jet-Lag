package net.superkat.jetlag.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
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
            float f = MathHelper.cos(1 * 7.448451F * 0.017453292F + 3.1415927F);

            float h = MathHelper.cos(this.getYaw() * 0.017453292F) * (1.3f + 0.21f);
            float j = MathHelper.sin(this.getYaw() * 0.017453292F) * (1.3f + 0.21f);
            float k = (0.3f + f * 0.45f) * (1 * 0.2f + 1.0f);
            this.world.addParticle(ParticleTypes.END_ROD, this.getX() + (double) h, this.getY() + (double) k, this.getZ() + (double) j, 0.001, 0.001, 0.001);
            this.world.addParticle(ParticleTypes.END_ROD, this.getX() - (double) h, this.getY() - (double) k, this.getZ() - (double) j, 0.001, 0.001, 0.001);
        }
    }
}
