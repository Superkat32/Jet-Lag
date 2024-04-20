package net.superkat.jetlag.contrail;

import java.util.List;

public interface JetLagPlayer {
    List<Contrail> jetlag$getAirStreaks();
    void jetlag$createAirStreak();
    void jetlag$removeAllAirStreaks();
    void jetlag$onElytraEnd();
    void jetlag$tick();
    void jetlag$renderAirStreakSets();
    boolean jetlag$hasAirStreaks();
}
