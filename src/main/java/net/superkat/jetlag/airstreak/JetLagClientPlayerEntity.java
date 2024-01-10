package net.superkat.jetlag.airstreak;

import net.superkat.jetlag.rendering.AirStreak;
import org.jetbrains.annotations.Nullable;

public interface JetLagClientPlayerEntity {
    @Nullable
    AirStreak jetLag$getPlayerAirStreaks();
    void jetLag$setAirStreak(AirStreak airStreak);
    void jetLag$updateAirStreakPoint();

}
