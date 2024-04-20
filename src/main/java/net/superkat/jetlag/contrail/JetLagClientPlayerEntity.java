package net.superkat.jetlag.contrail;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface JetLagClientPlayerEntity {
    @Nullable
    Contrail jetLag$getPlayerAirStreaks();
    void jetLag$setAirStreak(Contrail contrail);
    void jetLag$updateAirStreakPoint();
    void addPoint(MatrixStack matrixStack, ClientPlayerEntity entity);
    void addPoint(Vec3d pointLoc);
}
