package net.superkat.jetlag.contrail;

import java.util.List;

public interface JetLagPlayer {
    List<Contrail> jetlag$getContrails();
    void jetlag$createContrail();
    void jetlag$removeAllContrails();
    void jetlag$onElytraEnd();
    void jetlag$tick();
    void jetlag$renderContrailSets();
    boolean jetlag$hasContrails();
}
