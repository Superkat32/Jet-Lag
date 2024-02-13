package net.superkat.jetlag.airstreak;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public interface JetLagClientPlayerEntity {
    @Nullable
    AirStreak jetLag$getPlayerAirStreaks();
    void jetLag$setAirStreak(AirStreak airStreak);
    void jetLag$updateAirStreakPoint();
    void addPoint(MatrixStack matrixStack, ClientPlayerEntity entity);
}
