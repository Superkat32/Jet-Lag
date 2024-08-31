package net.superkat.jetlag.contrail;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public interface JetLagPlayer {
    //General
    boolean jetlag$rocketBoosting();
    void jetlag$setRocketBoosting(boolean boosting);

    //Contrail technically but whatever
    float jetlag$playerFakeElytraRoll();
    void jetlag$setPlayerFakeElytraRoll(float roll);

    //Contrails
    List<Contrail> jetlag$getContrails();
    @Nullable
    Contrail jetlag$getCurrentContrail();
    void jetlag$createContrail();
    void jetlag$removeAllContrails();
    void jetlag$endCurrentContrail();
    boolean jetlag$hasContrails();
}
