package net.superkat.jetlag.speedline;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.config.SpeedlineConfigInstance;
import net.superkat.jetlag.contrail.ContrailHandler;
import net.superkat.jetlag.contrail.JetLagPlayer;

public class SpeedlineHandler {
    public static int speedlineTicks = 20;

    public static double x = 0;
    public static double y = 0;

    public static void tickSpeedlines(MinecraftClient client) {
        if(client.world != null && client.player != null && ContrailHandler.shouldTick()) {
            ClientPlayerEntity player = client.player;

            float lerpedYaw = MathHelper.lerp(client.getTickDelta(), player.lastRenderYaw, player.renderYaw);
            x = -(player.getYaw(client.getTickDelta()) - lerpedYaw);

            float lerpedPitch = MathHelper.lerp(client.getTickDelta(), player.lastRenderPitch, player.renderPitch);
            y = -(player.getPitch(client.getTickDelta()) - lerpedPitch);


            if(player.isFallFlying()) {
                speedlineTicks--;
            }
            if(speedlineTicks <= 0) {
                if(!JetLagConfig.getInstance().speedlinesEnabled) return;

                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                boolean rocketBoosted = jetLagPlayer.jetlag$rocketBoosting() || (JetLagConfig.getInstance().riptideMakesRocket && player.isUsingRiptide());
                speedlineTicks = addSpeedline(player, rocketBoosted && JetLagConfig.getInstance().rocketSpeedlinesEnabled);
            }
        }
    }

    public static int addSpeedline(ClientPlayerEntity player, boolean rocketBoosted) {
        SpeedlineConfigInstance config = rocketBoosted ? JetLagConfig.getInstance().rocketConfig : JetLagConfig.getInstance().speedlineConfig;
        int minAmount = config.minSpawnAmount;
        int maxAmount = config.maxSpawnAmount;
        float playerVelocity = (float) player.getVelocity().lengthSquared();
        if(config.velBasedSpawnAmount && playerVelocity < 1f) {
            maxAmount = (int) MathHelper.clamp(maxAmount * playerVelocity, minAmount, maxAmount);
        }

        int count = player.getRandom().nextBetween(minAmount, maxAmount);
        for (int i = 0; i < count; i++) {
            if (rocketBoosted) {
                addRocketSpeedline(player);
            } else {
                addSpeedline(player);
            }
        }

        int maxTicks = config.maxSpawnTicks;
        int minTicks = config.minSpawnTicks;
        int ticks = player.getRandom().nextBetween(minTicks, maxTicks);
        return ticks;
    }

    public static double speedlineXOffset() {
        return x;
    }

    public static double speedlineYOffset() {
        return y;
    }

    private static void addSpeedline(ClientPlayerEntity player) {
        player.getWorld().addParticle(JetLagMain.SPEEDLINE, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
    }

    private static void addRocketSpeedline(ClientPlayerEntity player) {
        player.getWorld().addParticle(JetLagMain.ROCKET_SPEEDLINE, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
    }
}
