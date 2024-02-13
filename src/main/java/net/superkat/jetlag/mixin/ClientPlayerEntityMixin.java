package net.superkat.jetlag.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.superkat.jetlag.airstreak.AirStreak;
import net.superkat.jetlag.airstreak.AirStreakHandler;
import net.superkat.jetlag.airstreak.JetLagClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements JetLagClientPlayerEntity {
    public AirStreak playerAirStreaks = null;
    @Override
    public AirStreak jetLag$getPlayerAirStreaks() {
        return playerAirStreaks;
    }

    @Override
    public void jetLag$setAirStreak(AirStreak airStreak) {
        this.playerAirStreaks = airStreak;
    }

    @Override
    public void jetLag$updateAirStreakPoint() {
        playerAirStreaks.addPoint();
    }

    @Override
    public void addPoint(MatrixStack matrixStack, ClientPlayerEntity entity) {
        if(playerAirStreaks != null) {
            playerAirStreaks.addPoint(matrixStack, entity);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void jetLag$updateAirStreaks(CallbackInfo ci) {
        if(playerAirStreaks != null) {
            AirStreakHandler.updatePlayerAirStreaks((ClientPlayerEntity) (Object) this);
        }
    }
}
