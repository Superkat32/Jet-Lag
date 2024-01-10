package net.superkat.jetlag.rendering;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class AirStreak {
    public final ClientPlayerEntity player;
    public int maxPoints = 30;
    public List<Vec3d> points = Lists.newArrayList();
    public AirStreak(ClientPlayerEntity player) {
        this.player = player;
        addPoint();
    }
    public void addPoint() {
        points.add(player.getPos());
    }
    public void removeOldestPoint() {
        points.remove(0);
    }
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
    public boolean shouldStartDeletingPoints() {
        return points.size() >= maxPoints;
    }
}
