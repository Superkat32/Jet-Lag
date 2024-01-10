package net.superkat.jetlag.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.airstreak.JetLagClientPlayerEntity;
import net.superkat.jetlag.rendering.AirStreak;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements JetLagClientPlayerEntity {
    public AirStreak playerAirStreaks = null;
    public int ticksUntilNewPoint = 0;
    public int ticksUntilDeletePoint = 0;

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

    @Inject(method = "tick", at = @At("TAIL"))
    public void jetLag$updateAirStreaks(CallbackInfo ci) {
        if(playerAirStreaks != null) {

            if(((ClientPlayerEntity) (Object) this).isFallFlying()) {
                ticksUntilNewPoint++;
                if(ticksUntilNewPoint >= 2) {
                    playerAirStreaks.addPoint();
                    ticksUntilNewPoint = 0;
                }
            }

            if(playerAirStreaks.shouldStartDeletingPoints() || !((ClientPlayerEntity) (Object) this).isFallFlying()) {
                ticksUntilDeletePoint++;
                if(ticksUntilDeletePoint >= 2) {
                    if(playerAirStreaks.points.isEmpty()) {
                        jetLag$setAirStreak(null);
                    } else {
                        playerAirStreaks.removeOldestPoint();
                    }
                    ticksUntilDeletePoint = 0;
                }
            }
        }
    }
}
