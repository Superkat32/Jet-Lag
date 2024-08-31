package net.superkat.jetlag.contrail;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.JetLagClient;
import net.superkat.jetlag.compat.DABRCompat;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.contrail.Contrail.ContrailPos;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class ContrailHandler {
    private static final float maxElytraRoll = 1.5707958f; //can probably be modified by other mods... hopefully shouldn't though
    public static float fakePlayerElytraRoll = 0f;

//    public static void tickJetlagPlayer(ClientPlayerEntity player) {
//        //TODO - move most of this back into ClientPlayerEntityMixin because I don't like the way this looks
//        JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
//        Contrail currentContrail = jetLagPlayer.jetlag$getCurrentContrail();
//
//        //if player is flying with an elytra
//        if(player.isFallFlying()) {
//            //tick current contrail
//            if(currentContrail != null) {
//                //adds new points to the current contrail
//                if (JetLagConfig.getInstance().contrailsEnabled && shouldTick()) {
//                    int pointTicks = jetLagPlayer.jetlag$pointTicks();
//                    jetLagPlayer.jetlag$setPointTicks(pointTicks--);
//                    if(pointTicks <= 0) {
//                        currentContrail.addPoint();
//                        jetLagPlayer.jetlag$setPointTicks(JetLagConfig.getInstance().ticksPerPoint);
//                    }
//                }
//            } else {
//                //no current contrail exists yet
//                jetLagPlayer.jetlag$createContrail();
//            }
//        } else if(currentContrail != null) {
//            //player has just landed
//            jetLagPlayer.jetlag$endCurrentContrail();
//        }
//
//        for (Contrail contrail : jetLagPlayer.jetlag$getContrails()) {
//            contrail.tick();
//        }
//    }

    /**
     * @return If the contrails tick(add/delete points)
     */
    public static boolean shouldTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        return !client.isPaused() && (client.world == null || client.world.getTickManager().shouldTick());
    }

    /**
     * Find a ContrailPos based on a player's position and rotation, in theory at the edge of both elytra wings. Opacity is NOT applied here.
     *
     * @param player The player whom the contrail is coming from - used for position and rotation
     * @return A new ContrailPos, in theory at the edge of both elytra wings
     *
     * @see Contrail#addPoint()
     */
    public static ContrailPos getContrailPos(AbstractClientPlayerEntity player) {
        double yaw = player.getYaw(MinecraftClient.getInstance().getTickDelta());
        double pitch = player.getPitch(MinecraftClient.getInstance().getTickDelta());
        double yawRadians = Math.toRadians(yaw);
        double pitchRadians = Math.toRadians(pitch);
        double rollRadians = getPlayerRoll(player, MinecraftClient.getInstance().getTickDelta());
        float delta = MinecraftClient.getInstance().getTickDelta();

        double elytraWingOffset = -getElytraRoll(player) / maxElytraRoll;

        JetLagConfig config = JetLagConfig.getInstance();
        double leftWidth = config.contrailLeftOffsetWidth;
        double leftLength = config.contrailLeftOffsetLength;
        double leftHeight = config.contrailLeftOffsetHeight;
        double rightWidth = config.mirrorContrailOffset ? leftWidth : config.contrailRightOffsetWidth;
        double rightLength = config.mirrorContrailOffset ? leftLength : config.contrailRightOffsetLength;
        double rightHeight = config.mirrorContrailOffset ? leftHeight : config.contrailRightOffsetHeight;

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
        Vector3d leftRot = new Vector3d(leftWidth, leftLength, leftHeight).rotate(rotation).mul(elytraWingOffset);
        Vec3d left = new Vec3d(leftRot.x, leftRot.z, leftRot.y).add(playerPos);
        Vector3d rightRot = new Vector3d(-rightWidth, rightLength, rightHeight).rotate(rotation).mul(elytraWingOffset);
        Vec3d right = new Vec3d(rightRot.x, rightRot.z, rightRot.y).add(playerPos);

//        MinecraftClient.getInstance().world.addParticle(ParticleTypes.END_ROD, left.getX(), left.getY(), left.getZ(), 0, 0, 0);
//        MinecraftClient.getInstance().world.addParticle(ParticleTypes.CHERRY_LEAVES, right.getX(), right.getY(), right.getZ(), 0, 0, 0);
        return new ContrailPos(left, right);
    }

    /**
     * Get a player's roll in radians using the same math that the PlayerModel would use. Compat for Do A Barrel Roll is also in this method.
     *
     * @param player The player whose roll should be returned.
     * @param tickDelta Minecraft tick delta.
     * @return The player's roll in radians.
     *
     * @see net.minecraft.client.render.entity.PlayerEntityRenderer#setupTransforms(AbstractClientPlayerEntity, MatrixStack, float, float, float)
     * @see DABRCompat
     */
    public static double getPlayerRoll(AbstractClientPlayerEntity player, float tickDelta) {
        MinecraftClient.getInstance().getTickDelta();
        boolean DABRLoaded = JetLagClient.DABRLoaded();
        double roll = 0;

        //DABR doesn't edit this, so this takes priority
        if(player.isUsingRiptide()) {
            return getRiptideRoll(player);
        }

        //DABR elytra
        if(DABRLoaded) {
            if(DABRCompat.DABREnabled()) {
                return Math.toRadians(DABRCompat.getPlayerRoll(player, tickDelta));
            }
        }

        //normal elytra
        Vec3d rotationVec = player.getRotationVec(tickDelta);
        Vec3d lerpVelocity = player.lerpVelocity(tickDelta);
        double d = rotationVec.horizontalLengthSquared();
        double e = lerpVelocity.horizontalLengthSquared();
        if(d > 0 && e > 0) {
            double m = (lerpVelocity.x * rotationVec.x + lerpVelocity.z * rotationVec.z) / Math.sqrt(d * e);
            double n = lerpVelocity.x * rotationVec.z - lerpVelocity.z * rotationVec.x;
            roll = Math.signum(n) * Math.acos(m);
        }

        //fixes flickering while flying without moving(player model still flickers)
        if(Double.isNaN(roll)) {
            roll = 0d;
        }

        return roll;
    }

    private static double getRiptideRoll(AbstractClientPlayerEntity player) {
        //not sure why putting this in degrees work but it does
        return -(player.age) % 360;
    }

    /**
     * Get the player's elytra WING roll, not the player's actual roll!! The AbstractClientPlayerEntity's elytraRoll field does not get updated in first person, so it is manually calculated here if needed.
     *
     * @param player The player who's elytra wing roll should be gotten
     * @return The player's elytra wing roll
     */
    public static float getElytraRoll(AbstractClientPlayerEntity player) {
        //checks if the player is the client's player and then checks if manual calculation should be done
//        if(player instanceof ClientPlayerEntity && MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
        boolean self = player instanceof ClientPlayerEntity;
        if(self) {
            if(!MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
                return player.elytraRoll;
            }
        }
        //TODO - move contrails from player to common list maybe in here
        //TODO - add config option for custom calculation of elytra roll if player is visible
        return ((JetLagPlayer) player).jetlag$playerFakeElytraRoll();
//        return player.elytraRoll - (self ? 0f : MinecraftClient.getInstance().getTickDelta());
//        return -maxElytraRoll;
//        return ((JetLagPlayer) player).jetlag$playerFakeElytraRoll();
//            float l = (float) (-Math.PI / 12);
//            float o = 1.0F;
//            Vec3d vec3d = player.getVelocity();
//            if (vec3d.y < 0.0) {
//                Vec3d vec3d2 = vec3d.normalize();
//                o = 1.0F - (float)Math.pow(-vec3d2.y, 1.5);
//            }
//            l = o * (float) (-Math.PI / 2) + (1.0F - o) * l;
//            JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
//            float fakeElytraRoll = jetLagPlayer.jetlag$playerFakeElytraRoll();
//            fakeElytraRoll += (l - fakeElytraRoll) * 0.1F;
//            jetLagPlayer.jetlag$setPlayerFakeElytraRoll(fakeElytraRoll);
//            return player.elytraRoll;
//        } else {
//            return player.elytraRoll;
//        }
    }

    public static float calculateElytraRoll(AbstractClientPlayerEntity player) {
        float l = (float) (-Math.PI / 12);
        if(player.isFallFlying()) {
            float o = 1.0F;
            Vec3d vec3d = player.lerpVelocity(MinecraftClient.getInstance().getTickDelta());
            if (vec3d.y < 0.0) {
                Vec3d vec3d2 = vec3d.normalize();
                o = 1.0F - (float)Math.pow(-vec3d2.y, 1.5);
            }
            l = o * (float) (-Math.PI / 2) + (1.0F - o) * l;
        }
        JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
        float fakeElytraRoll = jetLagPlayer.jetlag$playerFakeElytraRoll();
        fakeElytraRoll += (l - fakeElytraRoll) * 0.1f;
        return fakeElytraRoll;
    }
}
