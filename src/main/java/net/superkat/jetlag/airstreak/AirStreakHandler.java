package net.superkat.jetlag.airstreak;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.airstreak.AirStreak.AirStreakPos;

import java.util.List;

public class AirStreakHandler {
    //This is really just a utils class which can be hot swapped

    public static AirStreakPos getAirStreakPos(ClientPlayerEntity player) {
        float yaw = player.getYaw(MinecraftClient.getInstance().getTickDelta());
        float pitch = player.getPitch(MinecraftClient.getInstance().getTickDelta());
        float roll = (float) getPlayerRoll(player, MinecraftClient.getInstance().getTickDelta());
        float yawRadians = (float) Math.toRadians(yaw);
        //start of attempt 3
        //configurable variables to change the particle pos
        float initHorizontalOffset = 1.385f;
        float initYOffset = 0.7f;

        //elytra wing offset
        //the player.elytra<Pitch,Yaw,Roll> is all for the left wing
        //Offsets the position towards the default resting position of the elytra
        float elytraWingOffset = Math.abs(player.elytraRoll + 0.2617994f);

        //begin by offsetting player pos to default elytra edge
        //could also be considered yaw offset
        //Offsets the position to the edge of the elytra if the player's pitch was 0
        float initXOffset = (float) Math.cos(yawRadians) * initHorizontalOffset;
        float initZOffset = (float) Math.sin(yawRadians) * initHorizontalOffset;

        //pitch offset
        //Offsets the position to either behind or in front of the player
        //depending on if they are looking upwards or downwards respectively
        float pitchXOffset = (float) (Math.sin(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;
        float pitchZOffset = (float) (Math.cos(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;

        //roll offset
        //Offsets the position up or down and to some degree left or right
        //depending on how far the player has turned
        float rollHeightOffset = (float) (Math.atan(roll) * (Math.toRadians(Math.abs(pitch)) + 0.5f)); //adjust for pitch as well
        float rollXOffset = (float) (Math.atan(initXOffset) * Math.abs(roll));
        float rollZOffset = (float) (Math.atan(initZOffset) * Math.abs(roll));


        //final variables
        float leftX = (initXOffset + pitchXOffset - rollXOffset) * elytraWingOffset;
        float leftY = initYOffset + rollHeightOffset;
        float leftZ = (initZOffset + pitchZOffset - rollZOffset) * elytraWingOffset;
        float rightX = (-initXOffset + pitchXOffset + rollXOffset) * elytraWingOffset;
        float rightY = initYOffset - rollHeightOffset;
        float rightZ = (-initZOffset + pitchZOffset + rollZOffset) * elytraWingOffset;

        Vec3d left = new Vec3d(player.getX() + leftX, player.getY() + leftY, player.getZ() + leftZ);
        Vec3d right = new Vec3d(player.getX() + rightX, player.getY() + rightY, player.getZ() + rightZ);
        return new AirStreakPos(left, right);
    }

    public static void spawnInParticlesAtElytraTips(AbstractClientPlayerEntity player) {
        float yaw = player.getYaw(MinecraftClient.getInstance().getTickDelta());
        float pitch = player.getPitch(MinecraftClient.getInstance().getTickDelta());
        float roll = (float) getPlayerRoll(player, MinecraftClient.getInstance().getTickDelta());
        float yawRadians = (float) Math.toRadians(yaw);
        //start of attempt 3
        //configurable variables to change the particle pos
        float initHorizontalOffset = 1.385f;
        float initYOffset = 0.7f;

        //elytra wing offset
        //the player.elytra<Pitch,Yaw,Roll> is all for the left wing
        //Offsets the position towards the default resting position of the elytra
        float elytraWingOffset = Math.abs(player.elytraRoll + 0.2617994f);

        //begin by offsetting player pos to default elytra edge
        //could also be considered yaw offset
        //Offsets the position to the edge of the elytra if the player's pitch was 0
        float initXOffset = (float) Math.cos(yawRadians) * initHorizontalOffset;
        float initZOffset = (float) Math.sin(yawRadians) * initHorizontalOffset;

        //pitch offset
        //Offsets the position to either behind or in front of the player
        //depending on if they are looking upwards or downwards respectively
        float pitchXOffset = (float) (Math.sin(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;
        float pitchZOffset = (float) (Math.cos(-yawRadians) * Math.atan(pitch) / 3f) / elytraWingOffset;

        //roll offset
        //Offsets the position up or down and to some degree left or right
        //depending on how far the player has turned
        float rollHeightOffset = (float) (Math.atan(roll) * (Math.toRadians(Math.abs(pitch)) + 0.5f)); //adjust for pitch as well
        float rollXOffset = (float) (Math.atan(initXOffset) * Math.abs(roll));
        float rollZOffset = (float) (Math.atan(initZOffset) * Math.abs(roll));


        //final variables
        float leftX = (initXOffset + pitchXOffset - rollXOffset) * elytraWingOffset;
        float leftY = initYOffset + rollHeightOffset;
        float leftZ = (initZOffset + pitchZOffset - rollZOffset) * elytraWingOffset;
        float rightX = (-initXOffset + pitchXOffset + rollXOffset) * elytraWingOffset;
        float rightY = initYOffset - rollHeightOffset;
        float rightZ = (-initZOffset + pitchZOffset + rollZOffset) * elytraWingOffset;

        //elytra offset
//        float leftX = initXOffset * elytraWingOffset;
//        float leftY = initYOffset;
//        float leftZ = initZOffset * elytraWingOffset;
//        float rightX = -initXOffset * elytraWingOffset;
//        float rightY = initYOffset;
//        float rightZ = -initZOffset * elytraWingOffset;

        //pitch adjustment
//        float leftX = initXOffset + pitchXOffset;
//        float leftY = initYOffset;
//        float leftZ = initZOffset + pitchZOffset;
//        float rightX = -initXOffset + pitchXOffset;
//        float rightY = initYOffset;
//        float rightZ = -initZOffset + pitchZOffset;

        //roll adjustment
//        float leftX = initXOffset - rollXOffset;
//        float leftY = initYOffset + rollHeightOffset;
//        float leftZ = initZOffset - rollZOffset;
//        float rightX = -initXOffset + rollXOffset;
//        float rightY = initYOffset - rollHeightOffset;
//        float rightZ = -initZOffset + rollZOffset;

        //left particle of player
        player.clientWorld.addParticle(ParticleTypes.END_ROD,
                player.getX() + leftX,
                player.getY() + leftY,
                player.getZ() + leftZ,
                0, 0, 0);

        //right particle of player
        player.clientWorld.addParticle(ParticleTypes.END_ROD,
                player.getX() + rightX,
                player.getY() + rightY,
                player.getZ() + rightZ,
                0, 0, 0);

        //end of attempt 3

        //start of attempt 2
//        //adjustment variables and cached variables
//        float defaultY = 0.65f; //Move up to the elytra wing's level
//        float yawRadians = yaw * 0.017453292F;
//        float pitchRadians = pitch * 0.017453292F;
//
//        //Variables used to determine the position
//        float horizontalOffset = 1.45f - (Math.abs(roll) / 2f) * (1 + Math.abs(pitch / 70f)); //the offset left/right from the wings
//        float forwardsOffset = pitch / 90f * 0.75f; //the offset to in front of OR behind the elytra
//
//        //Variables that determine the position
//        float xYawOffset = MathHelper.cos(yawRadians) * horizontalOffset;
//        float zYawOffset = MathHelper.sin(yawRadians) * horizontalOffset;
//        float xPitchOffset = MathHelper.sin(-yawRadians) * forwardsOffset;
//        float zPitchOffset = MathHelper.cos(-yawRadians) * forwardsOffset;
//
//        float yOffset = roll * 1.35f * (1 + Math.abs(pitch / 90f));
//
//        //Positions for each wing
//        double xLeft = xYawOffset + xPitchOffset;
//        double yLeft = defaultY + yOffset;
//        double zLeft = zYawOffset + zPitchOffset;
//
//        double xRight = xYawOffset - xPitchOffset;
//        double yRight = defaultY - yOffset;
//        double zRight = zYawOffset - zPitchOffset;
//
//        //left particle of player
//        player.clientWorld.addParticle(ParticleTypes.END_ROD,
//                player.getX() + xLeft,
//                player.getY() + yLeft,
//                player.getZ() + zLeft,
//                0.001, 0.001, 0.001);
//
//        //right particle of player
//        player.clientWorld.addParticle(ParticleTypes.END_ROD,
//                player.getX() - xRight,
//                player.getY() + yRight,
//                player.getZ() - zRight,
//                0.001, 0.001, 0.001);
//
        //end of attempt 2

//        float horizontalOffset = 1.35f; //the offset left/right from the wings
//        float verticalOffset = 0.65f - (pitch / 90f); //the offset upwards
//
//        float backwardsOffset = 1.1f; //the offset to behind the elytra when looking upwards
//        float forwardsOffset = 0.4f; //the offset to in front of the elytra when looking downwards
//
//        //Accounts for pitch to adjust the location forwards/backwards
//        float xPitchOffset = 0;
//        float zPitchOffset = 0;
//
//        //Partially adjusts for roll(more is done in the yOffset)
//        float xRollOffset = -Math.abs(roll * 1.5f) + 1;
//
//        //Fully adjusts for pitch
//        if(pitch < -60f) { //if the player is looking pretty far upwards
////            xPitchOffset = MathHelper.sin(-yaw * 0.017453292F) * -backwardsOffset * (-pitch - 45) * 0.017453292F * xRollOffset;
////            zPitchOffset = MathHelper.cos(-yaw * 0.017453292F) * -backwardsOffset * (-pitch - 45) * 0.017453292F * xRollOffset;
//            xPitchOffset = MathHelper.sin(-yaw * 0.017453292F) * -backwardsOffset * (-pitch - 45) * 0.017453292F;
//            zPitchOffset = MathHelper.cos(-yaw * 0.017453292F) * -backwardsOffset * (-pitch - 45) * 0.017453292F;
////            xPitchOffset = MathHelper.sin(-player.getYaw() * 0.017453292F) * -2;
////            zPitchOffset = MathHelper.cos(-player.getYaw() * 0.017453292F) * -2;
//        } else if (pitch > 60f) {
//            xPitchOffset = MathHelper.sin(-yaw * 0.017453292F) * forwardsOffset * (pitch + 45) * 0.017453292F;
//            zPitchOffset = MathHelper.cos(-yaw * 0.017453292F) * forwardsOffset * (pitch + 45) * 0.017453292F;
//        }
//
//        //Fully adjusts for yaw
////        float xOffset = MathHelper.cos(yaw * 0.017453292F) * (horizontalOffset) * (-Math.abs(roll * 0.8f) + 1);
////        float zOffset = MathHelper.sin(yaw * 0.017453292F) * (horizontalOffset) * (-Math.abs(roll * 0.8f) + 1);
//        float xOffset = MathHelper.cos(yaw * 0.017453292F) * (horizontalOffset);
//        float zOffset = MathHelper.sin(yaw * 0.017453292F) * (horizontalOffset);
//
//        float yOffset = (verticalOffset * roll * 2f);
//
//
//
//        //left particle of player
//        player.clientWorld.addParticle(ParticleTypes.END_ROD,
//                player.getX() + (double) xOffset + xPitchOffset,
//                player.getY() + verticalOffset + yOffset,
//                player.getZ() + (double) zOffset + zPitchOffset,
//                0.001, 0.001, 0.001);
//        //right particle of player
//        player.clientWorld.addParticle(ParticleTypes.END_ROD,
//                player.getX() - (double) xOffset + xPitchOffset,
//                player.getY() + verticalOffset - yOffset,
//                player.getZ() - (double) zOffset + zPitchOffset,
//                0.001, 0.001, 0.001);
    }

    public static double getPlayerRoll(AbstractClientPlayerEntity player, float tickDelta) {

        MinecraftClient.getInstance().getTickDelta();

        double roll = 0;
        Vec3d rotationVec = player.getRotationVec(tickDelta);
        Vec3d lerpVelocity = player.lerpVelocity(tickDelta);
        double d = rotationVec.horizontalLengthSquared();
        double e = lerpVelocity.horizontalLengthSquared();
        if(d > 0 && e > 0) {
            double m = (lerpVelocity.x * rotationVec.x + lerpVelocity.z * rotationVec.z) / Math.sqrt(d * e);
            double n = lerpVelocity.x * rotationVec.z - lerpVelocity.z * rotationVec.x;
            roll = Math.signum(n) * Math.acos(m);
        }

        return roll;
    }

//    public static Vec3d getWorldPos(MatrixStack matrixStack, ClientPlayerEntity player) {
//        Vector4f vec = matrixStack.peek().getPositionMatrix().transform(new Vector4f(0, 0, 0, 1));
////
//        Vec3d pos = player.getPos().subtract(Math.cos(player.getBodyYaw() * 0.017453292F) * (vec.z + vec.x), vec.y, Math.sin(player.getBodyYaw() * 0.017453292F) * (vec.z + vec.x));
//        return pos;
//    }

    public static void airStreakWorldRendering(WorldRenderContext context) {
        List<AbstractClientPlayerEntity> players = context.world().getPlayers();
        for(AbstractClientPlayerEntity abstractPlayer : players) {
            if(abstractPlayer instanceof ClientPlayerEntity player) {
                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                jetLagPlayer.jetlag$tick();
//                JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
//                if(player.isFallFlying() || player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.SPYGLASS) {
//                    if(jetLagPlayer.jetLag$getPlayerAirStreaks() == null) {
//                        jetLagPlayer.jetLag$setAirStreak(new AirStreak(player));
//                    }
//                } if (jetLagPlayer.jetLag$getPlayerAirStreaks() != null) {
//                    AirStreakRenderer.renderAirStreaks(context, player);
//                }
            }
        }
    }

//    public static void updatePlayerAirStreaks(ClientPlayerEntity player) {
//        JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
//        AirStreak playerAirStreak = jetLagPlayer.jetLag$getPlayerAirStreaks();
//        playerAirStreak.tick();
//    }

//    public static void changeElytraRotation(ModelPart leftWing, ModelPart rightWing) {
        //this was used for testing purposes
//        System.out.println(rightWing.roll);
        //-1.5707958 is the absolute max roll the left wing can have
//        leftWing.roll = -1.5707958f;
//        rightWing.roll = 1.5f;
//    }
}
