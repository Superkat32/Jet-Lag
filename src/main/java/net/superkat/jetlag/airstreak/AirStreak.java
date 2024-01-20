package net.superkat.jetlag.airstreak;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class AirStreak {
    public final ClientPlayerEntity player;
    public List<Vec3d> leftPoints = Lists.newArrayList();
    public List<Vec3d> rightPoints = Lists.newArrayList();
    public int maxPoints = 30;
    public int ticksUntilNextPoint = 2;
    public int ticksUntilRemovePoint = 10;
    public AirStreak(ClientPlayerEntity player) {
        this.player = player;
        addPoint();
    }
    public void addPoint() {
        float f = MathHelper.cos(7.448451F * 0.017453292F + 3.1415927F) * player.elytraRoll;

        //closest thing I've got to it working properly
        float x = MathHelper.cos(player.getYaw() * 0.017453292F) * player.elytraRoll;
        float y = ((0.3f + f * 0.45f) * (player.elytraRoll * player.elytraPitch + 1.0f + player.elytraPitch));
        float z = MathHelper.sin(player.getYaw() * 0.017453292F) * player.elytraRoll;


//        float x = MathHelper.cos(player.getYaw() * 0.017453292F) * (1.3f + 0.21f);
//        float y = (0.3f + f * 0.45f) * (1 * 0.2f + 1.0f);
//        float z = MathHelper.sin(player.getYaw() * 0.017453292F) * (1.3f + 0.21f);
//        System.out.println("yaw " + player.elytraYaw);
//        System.out.println("pitch " + player.elytraPitch);
//        System.out.println("roll " + player.elytraRoll);
        //-1.5707958 is the absolute max roll the left wing can have
        leftPoints.add(player.getPos().add(x, y, z));
        rightPoints.add(player.getPos());
    }

    public Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double)(i * j), (double)(-k), (double)(h * j));
    }

    public void removeOldestPoint() {
        leftPoints.remove(0);
        rightPoints.remove(0);
    }
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
    public boolean shouldStartDeletingPoints() {
        return rightPoints.size() >= maxPoints || leftPoints.size() >= maxPoints;
    }
    public void tick() {
        //if player is flying, start adding points
        if(player.isFallFlying()) {
            ticksUntilNextPoint--;
            if(ticksUntilNextPoint <= 0) {
                addPoint();
                ticksUntilNextPoint = 2;
            }

        }

        //if max points have been reached or the player has stopped flying
        if(shouldStartDeletingPoints() || !(player.isFallFlying())) {
            ticksUntilRemovePoint--;
            if(ticksUntilRemovePoint <= 0) {
                if(leftPoints.isEmpty() || rightPoints.isEmpty()) {
                    ((JetLagClientPlayerEntity) player).jetLag$setAirStreak(null);
                } else {
                    removeOldestPoint();
                }
                ticksUntilRemovePoint = 2;
            }
        }
    }
}
