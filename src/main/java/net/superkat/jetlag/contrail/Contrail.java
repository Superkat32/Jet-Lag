package net.superkat.jetlag.contrail;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.rendering.ContrailRenderer;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class Contrail {
    public final ClientPlayerEntity player;
    public List<ContrailPos> contrailPoints = Lists.newArrayList();
//    public List<Float> contrailWidthAdjustments = Lists.newArrayList();
    public List<Float> contrailOpacityAdjustments = Lists.newArrayList();
    public int maxPoints;
    public boolean startDeletingPoints = false;
    public int ticksUntilNextDelete = 10;
    public final Random random = Random.create();

    public Contrail(ClientPlayerEntity player) {
        this.player = player;
        this.maxPoints = JetLagConfig.getInstance().maxPoints;
    }

    public void addPoint() {
        contrailPoints.add(0, ContrailHandler.getAirStreakPos(player));

//        float maxWidthAdjustment = JetLagConfig.getInstance().contrailFluffiness / 20f;
//        contrailWidthAdjustments.add(0, MathHelper.nextBetween(this.random, -maxWidthAdjustment, maxWidthAdjustment));

        float maxOpacityAdjustment = JetLagConfig.getInstance().contrailOpacityAdjustment;
        boolean velocityOpacity = JetLagConfig.getInstance().velocityBasedOpacityAdjust;
        if(velocityOpacity) {
            maxOpacityAdjustment *= (float) this.player.getVelocity().lengthSquared();
        }
        contrailOpacityAdjustments.add(0, MathHelper.nextBetween(this.random, -maxOpacityAdjustment, maxOpacityAdjustment));
        if(shouldRemoveOldestPoint() && tickPoints()) {
            removeOldestPoint();
        }
    }

    public void render() {
        maxPoints = JetLagConfig.getInstance().maxPoints;

        if(startDeletingPoints && !noPointsLeft() && Contrail.tickPoints()) {
            ticksUntilNextDelete--;
            if(ticksUntilNextDelete <= 0) {
                for (int i = 0; i < JetLagConfig.getInstance().pointsDeletedPerDelay; i++) {
                    if(contrailPoints.isEmpty()) {
                        break;
                    }
                    removeOldestPoint();
                }
                ticksUntilNextDelete = JetLagConfig.getInstance().contrailDeletionDelay;
            }
        }

        ContrailRenderer.renderContrails(this);
    }

    public boolean shouldRemoveOldestPoint() {
        return contrailPoints.size() >= maxPoints;
    }

    public void removeOldestPoint() {
        contrailPoints.remove(contrailPoints.size() - 1);
//        contrailWidthAdjustments.remove(contrailWidthAdjustments.size() - 1);
        contrailOpacityAdjustments.remove(contrailOpacityAdjustments.size() - 1);
    }

    public void startDeletingPoints() {
        startDeletingPoints = true;
    }

    public boolean noPointsLeft() {
        return contrailPoints.isEmpty();
    }

//    public List<Float> getWidthAdjustments() {
//        return contrailWidthAdjustments;
//    }

    public List<Float> getOpacityAdjustments() {
        return contrailOpacityAdjustments;
    }

    public List<Vec3d> getLeftPoints() {
        List<Vec3d> points = Lists.newArrayList();
        contrailPoints.stream().map(ContrailPos::getLeftPoint).forEach(points::add);
        return points;
    }

    public List<Vec3d> getRightPoints() {
        List<Vec3d> points = Lists.newArrayList();
        contrailPoints.stream().map(ContrailPos::getRightPoint).forEach(points::add);
        return points;
    }
    public static boolean tickPoints() {
        MinecraftClient client = MinecraftClient.getInstance();
        return !client.isPaused() && (client.world == null || client.world.getTickManager().shouldTick());
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

    public static class ContrailPoint {
        public Vec3d vec3d;
        public float width;
        public float opacity;
        public ContrailPoint(Vec3d vec3d, float width, float opacity) {
            this.vec3d = vec3d;
            this.width = width;
            this.opacity = opacity;
        }
        public ContrailPoint(Vec3d vec3d) {
            this.vec3d = vec3d;
            this.width = (float) JetLagConfig.getInstance().contrailWidth;
            this.opacity = (float) JetLagConfig.getInstance().contrailColor.getAlpha() / 255f;
        }
        public Vec3d getVec3d() {
            return this.vec3d;
        }
        public float getWidth() {
            return this.width;
        }
        public float getOpacity() {
            return this.opacity;
        }
        public void setWidth(float width) {
            this.width = width;
        }
        public void setOpacity(float opacity) {
            this.opacity = opacity;
        }
    }
}
