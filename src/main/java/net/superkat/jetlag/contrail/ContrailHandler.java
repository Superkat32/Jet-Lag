package net.superkat.jetlag.contrail;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.contrail.Contrail.ContrailPos;

public class ContrailHandler {
    //This is really just a utils class which can be hot swapped

    public static ContrailPos getAirStreakPos(ClientPlayerEntity player) {
        float yaw = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevYaw, player.getYaw(MinecraftClient.getInstance().getTickDelta()));
        float pitch = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevPitch, player.getPitch(MinecraftClient.getInstance().getTickDelta()));
        float roll = (float) getPlayerRoll(player, MinecraftClient.getInstance().getTickDelta());
        float yawRadians = (float) Math.toRadians(yaw);
        float elytraWingOffset = Math.abs(player.elytraRoll + 0.2617994f);

        //fixes flickering after glider while not turning camera for a while
        if(Float.isNaN(roll)) {
            roll = 0f;
        }

        //start of attempt 4
        //configurable variables to change the point pos
        float initHorizontalOffset = 1f;
        float initYOffset = 0.5f;

        //begin by offsetting player pos to default elytra edge
        //could also be considered yaw offset
        //Offsets the position to the edge of the elytra if the player's pitch was 0
        float initXOffset = (float) Math.cos(yawRadians) * initHorizontalOffset;
        float initZOffset = (float) Math.sin(yawRadians) * initHorizontalOffset;

        //pitch offset
        float pitchHeightOffset = (float) Math.tan(-pitch / 90f);
        float pitchXOffset = (float) (Math.sin(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;
        float pitchZOffset = (float) (Math.cos(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;

        //roll offset
        float rollHeightOffset = (float) Math.atan(roll);

        //pos roll = right, neg roll = left
        boolean rollingRight = roll > 0f;
        float i = 1.2f;
        float rollRightOffset = rollHeightOffset * (rollingRight ? 1f : i);
        float rollLeftOffset = rollHeightOffset * (rollingRight ? i : 1f);
        float rollXOffset = (float) (Math.atan(initXOffset) * Math.abs(roll));
        float rollZOffset = (float) (Math.atan(initZOffset) * Math.abs(roll));

        //final variables
        float leftX = (initXOffset + pitchXOffset - rollXOffset) * elytraWingOffset;
        float leftY = initYOffset + rollRightOffset + pitchHeightOffset;
        float leftZ = (initZOffset + pitchZOffset - rollZOffset) * elytraWingOffset;
        float rightX = (-initXOffset + pitchXOffset + rollXOffset) * elytraWingOffset;
        float rightY = initYOffset - rollLeftOffset + pitchHeightOffset;
        float rightZ = (-initZOffset + pitchZOffset + rollZOffset) * elytraWingOffset;

        double lerpedPlayerX = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevX, player.getX());
        double lerpedPlayerY = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevY, player.getY());
        double lerpedPlayerZ = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevZ, player.getZ());

        Vec3d left = new Vec3d(lerpedPlayerX + leftX, lerpedPlayerY + leftY, lerpedPlayerZ + leftZ);
        Vec3d right = new Vec3d(lerpedPlayerX + rightX, lerpedPlayerY + rightY, lerpedPlayerZ + rightZ);
        return new ContrailPos(left, right);

//        //start of attempt 3
//        //configurable variables to change the particle pos
//        float initHorizontalOffset = 1.385f;
//        float initYOffset = 0.7f;
//
//        //elytra wing offset
//        //the player.elytra<Pitch,Yaw,Roll> is all for the left wing
//        //Offsets the position towards the default resting position of the elytra
//        float elytraWingOffset = Math.abs(player.elytraRoll + 0.2617994f);
//
//        //begin by offsetting player pos to default elytra edge
//        //could also be considered yaw offset
//        //Offsets the position to the edge of the elytra if the player's pitch was 0
//        float initXOffset = (float) Math.cos(yawRadians) * initHorizontalOffset;
//        float initZOffset = (float) Math.sin(yawRadians) * initHorizontalOffset;
//
//        //pitch offset
//        //Offsets the position to either behind or in front of the player
//        //depending on if they are looking upwards or downwards respectively
//        float pitchXOffset = (float) (Math.sin(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;
//        float pitchZOffset = (float) (Math.cos(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;
//
//        //roll offset
//        //Offsets the position up or down and to some degree left or right
//        //depending on how far the player has turned
//        float rollHeightOffset = (float) (Math.atan(roll) * (Math.toRadians(Math.abs(pitch)) + 0.5f)); //adjust for pitch as well
//        float rollXOffset = (float) (Math.atan(initXOffset) * Math.abs(roll));
//        float rollZOffset = (float) (Math.atan(initZOffset) * Math.abs(roll));
//
//
//        //final variables
//        float leftX = (initXOffset + pitchXOffset - rollXOffset) * elytraWingOffset;
//        float leftY = initYOffset + rollHeightOffset;
//        float leftZ = (initZOffset + pitchZOffset - rollZOffset) * elytraWingOffset;
//        float rightX = (-initXOffset + pitchXOffset + rollXOffset) * elytraWingOffset;
//        float rightY = initYOffset - rollHeightOffset;
//        float rightZ = (-initZOffset + pitchZOffset + rollZOffset) * elytraWingOffset;
//
//        //values must be lerped to heavily reduce rendering artifacts
//        //FIXME - weird rendering artifacts when flying straight up
//        double lerpedPlayerX = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevX, player.getX());
//        double lerpedPlayerY = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevY, player.getY());
//        double lerpedPlayerZ = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), player.prevZ, player.getZ());
//
//        Vec3d left = new Vec3d(lerpedPlayerX + leftX, lerpedPlayerY + leftY, lerpedPlayerZ + leftZ);
//        Vec3d right = new Vec3d(lerpedPlayerX + rightX, lerpedPlayerY + rightY, lerpedPlayerZ + rightZ);
//        return new ContrailPos(left, right);
    }

    public static double getPlayerRoll(AbstractClientPlayerEntity player, float tickDelta) {
        MinecraftClient.getInstance().getTickDelta();

        double roll = 0;

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

        return roll;
    }
}
