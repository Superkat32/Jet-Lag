package net.superkat.jetlag.airstreak;

import java.util.List;

public interface JetLagPlayer {
    List<AirStreak> jetlag$getAirStreaks();
    void jetlag$createAirStreak();
    void jetlag$removeAllAirStreaks();
    void jetlag$onElytraEnd();
    void jetlag$tick();
    void jetlag$renderAirStreakSets();
    boolean jetlag$hasAirStreaks();
}
