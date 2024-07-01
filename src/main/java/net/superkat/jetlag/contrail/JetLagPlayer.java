package net.superkat.jetlag.contrail;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface JetLagPlayer {

    //Contrails
    List<Contrail> jetlag$getContrails();
    @Nullable
    Contrail jetlag$getCurrentContrail();
    void jetlag$createContrail();
    void jetlag$removeAllContrails();
    void jetlag$endCurrentContrail();
    boolean jetlag$hasContrails();
    int jetlag$pointTicks();
    void jetlag$setPointTicks(int ticks);

    //Wind lines
    int jetlag$windLineTicks();
    void jetlag$setWindLineTicks(int ticks);
}
