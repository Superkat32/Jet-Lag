package net.superkat.jetlag.contrail;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.superkat.jetlag.config.JetLagConfig;
import org.spongepowered.include.com.google.common.collect.Lists;
import java.util.List;

public class Contrail {
    public final AbstractClientPlayerEntity player;
    public List<ContrailPos> contrailPoints = Lists.newArrayList();
    public int maxPoints;
    public boolean startDeletingPoints = false;
    public int deleteTicks = 10;
    public final Random random = Random.create();

    public Contrail(AbstractClientPlayerEntity player) {
        this.player = player;
        this.maxPoints = JetLagConfig.getInstance().maxPoints;
    }

    public void addPoint() {
        boolean mirrorOpacity = JetLagConfig.getInstance().mirrorOpacity;
        float maxOpacityAdjust = getMaxOpacityAdjust();
        float leftOpacity = getRandomOpacity(maxOpacityAdjust);
        float rightOpacity = mirrorOpacity ? leftOpacity : getRandomOpacity(maxOpacityAdjust);

        ContrailPos contrailPos = ContrailHandler.getContrailPos(player).withOpacity(leftOpacity, rightOpacity);
        contrailPoints.add(0, contrailPos);

        if(shouldRemoveOldestPoint() && ContrailHandler.shouldTick()) {
            removeOldestPoint();
        }
    }

    private float getMaxOpacityAdjust() {
        float maxOpacityAdjustment = JetLagConfig.getInstance().contrailOpacityAdjustment;
        boolean velocityOpacity = JetLagConfig.getInstance().velocityBasedOpacityAdjust;
        if(velocityOpacity) {
            maxOpacityAdjustment *= (float) this.player.getVelocity().lengthSquared();
        }
        return maxOpacityAdjustment;
    }

    private float getRandomOpacity(float maxOpacityAdjust) {
        return MathHelper.nextBetween(this.random, -maxOpacityAdjust, maxOpacityAdjust);
    }

    public void tick() {
        maxPoints = JetLagConfig.getInstance().maxPoints;

        if(startDeletingPoints && !noPointsLeft() && ContrailHandler.shouldTick()) {
            deleteTicks--;
            if(deleteTicks <= 0) {
                //Delete multiple points should config says so
                for (int i = 0; i < JetLagConfig.getInstance().pointsDeletedPerDelay; i++) {
                    if(contrailPoints.isEmpty()) {
                        break;
                    }
                    removeOldestPoint();
                }
                deleteTicks = JetLagConfig.getInstance().contrailDeletionDelay;
            }
        }
    }

    public boolean shouldRemoveOldestPoint() {
        return contrailPoints.size() >= maxPoints;
    }

    public void removeOldestPoint() {
        contrailPoints.remove(contrailPoints.size() - 1);

        //if the max points was updated to a lower number than before basically
        if(contrailPoints.size() > maxPoints) {
            while (contrailPoints.size() > maxPoints) {
                contrailPoints.remove(contrailPoints.size() - 1);
            }
        }
    }

    public void startDeletingPoints() {
        startDeletingPoints = true;
    }

    public boolean noPointsLeft() {
        return contrailPoints.isEmpty();
    }

    public List<Vec3d> getLeftPoints() {
        return contrailPoints.stream().map(ContrailPos::getLeftPoint).toList();
    }

    public List<Float> getLeftOpacity() {
        return contrailPoints.stream().map(ContrailPos::getLeftOpacity).toList();
    }

    public List<Vec3d> getRightPoints() {
        return contrailPoints.stream().map(ContrailPos::getRightPoint).toList();
    }

    public List<Float> getRightOpacity() {
        return contrailPoints.stream().map(ContrailPos::getRightOpacity).toList();
    }

    public static class ContrailPos {
        public Vec3d left;
        public float leftOpacity;
        public Vec3d right;
        public float rightOpacity;

        public ContrailPos(Vec3d left, float leftOpacity, Vec3d right, float rightOpacity) {
            this.left = left;
            this.leftOpacity = leftOpacity;
            this.right = right;
            this.rightOpacity = rightOpacity;
        }

        public ContrailPos(Vec3d left, Vec3d right) {
            this.left = left;
            this.right = right;
        }

        //Fancy! :)
        public ContrailPos withOpacity(float leftOpacity, float rightOpacity) {
            this.leftOpacity = leftOpacity;
            this.rightOpacity = rightOpacity;
            return this;
        }

        public Vec3d getLeftPoint() {
            return left;
        }

        public float getLeftOpacity() {
            return leftOpacity;
        }

        public Vec3d getRightPoint() {
            return right;
        }

        public float getRightOpacity() {
            return rightOpacity;
        }
    }
}
