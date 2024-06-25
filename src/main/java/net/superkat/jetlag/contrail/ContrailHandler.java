package net.superkat.jetlag.contrail;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.JetLagClient;
import net.superkat.jetlag.compat.DABRCompat;
import net.superkat.jetlag.contrail.Contrail.ContrailPos;
import nl.enjarai.doabarrelroll.api.RollEntity;
import org.joml.*;

import java.lang.Math;

public class ContrailHandler {
    //This is really just a utils class which can be hot swapped
    private static final float maxElytraRoll = 1.5707958f; //can probably be modified by other mods... hopefully shouldn't though

    public static ContrailPos getAirStreakPos(ClientPlayerEntity player) {
        double yaw = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevYaw, player.getYaw(MinecraftClient.getInstance().getTickDelta()));
        double pitch = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevPitch, player.getPitch(MinecraftClient.getInstance().getTickDelta()));
        double yawRadians = Math.toRadians(yaw);
        double pitchRadians = Math.toRadians(pitch);
        double rollRadians = getPlayerRoll(player, MinecraftClient.getInstance().getTickDelta());
        float delta = MinecraftClient.getInstance().getTickDelta();

        double elytraWingOffset = -player.elytraRoll / maxElytraRoll;
        double width = 1.45;
        double height = 0.65;
        double forward = 0.45;

        Vec3d lerpedVel = player.lerpVelocity(delta);

        double playerX = MathHelper.lerp(delta, player.prevX, player.getX()) + lerpedVel.getX();
        double playerY = MathHelper.lerp(delta, player.prevY, player.getY()) + lerpedVel.getY();
        double playerZ = MathHelper.lerp(delta, player.prevZ, player.getZ()) + lerpedVel.getZ();
        Vec3d playerPos = new Vec3d(playerX, playerY, playerZ);

        //NOTE - the Z and Y coordinates ARE SWAPPED!!!!! This is extremely important if you want to reuse this code.
        //Pitch is also seemingly negative of what is expected.
        //This solution took almost 2-3 months of constant trying to figure out. I learned about Forward Kinematics, Rotation matrices, and multiplying matrices.
        //Forward Kinematics never worked out because I struggled to apply the concepts. I believe this is rotation & multiplication matrices being used.
        // ¯\_(ツ)_/¯ <- my honest reaction to any of this actually making sense... because it doesn't
        // ¯\(°_o)/¯ <- also my honest reaction
        Quaterniond rotation = new Quaterniond().rotateZYX(yawRadians, -rollRadians, -pitchRadians);
        Vector3d leftRot = new Vector3d(width, forward, height).rotate(rotation).mul(elytraWingOffset);
        Vec3d left = new Vec3d(leftRot.x, leftRot.z, leftRot.y).add(playerPos);
        Vector3d rightRot = new Vector3d(-width, forward, height).rotate(rotation).mul(elytraWingOffset);
        Vec3d right = new Vec3d(rightRot.x, rightRot.z, rightRot.y).add(playerPos);

        //FIXME - for some reason the contrails appear on a different spot(almost perpendicular) than the end rod particles...?????????
        //Check contrail rendering code - something to do with the camera?
        MinecraftClient.getInstance().world.addParticle(ParticleTypes.END_ROD, left.getX(), left.getY(), left.getZ(), 0, 0, 0);
        MinecraftClient.getInstance().world.addParticle(ParticleTypes.END_ROD, right.getX(), right.getY(), right.getZ(), 0, 0, 0);
        return new ContrailPos(left, right);
    }

    /**
     * Get a player's roll in radians using the same math that the PlayerModel would use. Compat for Do A Barrel Roll is also in this method.
     *
     * @param player The player whose roll should be returned.
     * @param tickDelta Minecraft tick delta.
     * @return The player's roll in radians.
     */
    public static double getPlayerRoll(AbstractClientPlayerEntity player, float tickDelta) {
        MinecraftClient.getInstance().getTickDelta();
        boolean DABRLoaded = JetLagClient.DABRLoaded();
        double roll = 0;

        if(DABRLoaded) {
            if(DABRCompat.DABREnabled()) {
                //FIXME - riptide don't work here
                return Math.toRadians(DABRCompat.getPlayerRoll(player, tickDelta));
            }
        }

        if(player.isUsingRiptide()) {
            //needs some work for a perfect spiral, but fun for now
            roll = ((Math.sin(player.age) + tickDelta));
        } else {
            Vec3d rotationVec = player.getRotationVec(tickDelta);
            Vec3d lerpVelocity = player.lerpVelocity(tickDelta);
            double d = rotationVec.horizontalLengthSquared();
            double e = lerpVelocity.horizontalLengthSquared();
            if(d > 0 && e > 0) {
                double m = (lerpVelocity.x * rotationVec.x + lerpVelocity.z * rotationVec.z) / Math.sqrt(d * e);
                double n = lerpVelocity.x * rotationVec.z - lerpVelocity.z * rotationVec.x;
                roll = Math.signum(n) * Math.acos(m);
            }
        }

        //fixes flickering
        if(Double.isNaN(roll)) {
            roll = 0d;
        }

        return roll;
    }
}
