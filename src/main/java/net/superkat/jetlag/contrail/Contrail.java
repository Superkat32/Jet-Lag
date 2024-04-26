package net.superkat.jetlag.contrail;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.rendering.ContrailRenderer;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class Contrail {
    public final ClientPlayerEntity player;
    public List<ContrailPos> airStreakPoints = Lists.newArrayList();
    public int maxPoints;
    public boolean startDeletingPoints = false;
    public int ticksUntilNextDelete = 10;

    public Contrail(ClientPlayerEntity player) {
        this.player = player;
        this.maxPoints = JetLagConfig.getInstance().maxPoints;
    }

    public void addPoint() {
        airStreakPoints.add(ContrailHandler.getAirStreakPos(player));
        if(shouldRemoveOldestPoint()) {
            removeOldestPoint();
        }
    }

    public void render() {

        maxPoints = JetLagConfig.getInstance().maxPoints;

        if(startDeletingPoints && !noPointsLeft()) {
            ticksUntilNextDelete--;
            if(ticksUntilNextDelete <= 0) {
                removeOldestPoint();
                ticksUntilNextDelete = 3;
            }
        }

        ContrailRenderer.renderContrails(this);
    }

    public boolean shouldRemoveOldestPoint() {
        return airStreakPoints.size() >= maxPoints;
    }

    public void removeOldestPoint() {
        airStreakPoints.remove(0);
    }

    public void startDeletingPoints() {
        startDeletingPoints = true;
    }

    public boolean noPointsLeft() {
        return airStreakPoints.isEmpty();
    }

    public List<Vec3d> getLeftPoints() {
        List<Vec3d> points = Lists.newArrayList();
        airStreakPoints.stream().map(ContrailPos::getLeftPoint).forEach(points::add);
        return points;
    }

    public List<Vec3d> getRightPoints() {
        List<Vec3d> points = Lists.newArrayList();
        airStreakPoints.stream().map(ContrailPos::getRightPoint).forEach(points::add);
        return points;
    }

    public static class ContrailPos {
        public Vec3d left;
        public Vec3d right;
        public ContrailPos(Vec3d left, Vec3d right) {
            this.left = left;
            this.right = right;
        }

        public Vec3d getLeftPoint() {
            return left;
        }

        public Vec3d getRightPoint() {
            return right;
        }
    }
}
