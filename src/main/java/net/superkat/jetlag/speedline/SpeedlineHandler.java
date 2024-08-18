package net.superkat.jetlag.speedline;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.contrail.ContrailHandler;
import net.superkat.jetlag.contrail.JetLagPlayer;

public class SpeedlineHandler {
    public static int speedlineTicks = 20;

    public static void tickSpeedlines(MinecraftClient client) {
        if(client.world != null && client.player != null && ContrailHandler.shouldTick()) {
            ClientPlayerEntity player = client.player;
            if(player.isFallFlying()) {
                speedlineTicks--;
            }
            if(speedlineTicks <= 0) {
                if(!JetLagConfig.getInstance().speedlinesEnabled) return;

                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                int count = player.getRandom().nextBetween(1, 3);
                for (int i = 0; i < count; i++) {
                    addSpeedline(player, jetLagPlayer.jetlag$rocketBoosting());
                }
                speedlineTicks = player.getRandom().nextBetween(1, 3);
            }
        }
    }

    public static void addSpeedline(ClientPlayerEntity player, boolean rocketBoosted) {
        if(JetLagConfig.getInstance().rocketSpeedlinesEnabled && rocketBoosted) {
            addRocketSpeedline(player);
        } else {
            addSpeedline(player);
        }
    }

    private static void addSpeedline(ClientPlayerEntity player) {
        player.getWorld().addParticle(JetLagMain.SPEEDLINE, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
    }

    private static void addRocketSpeedline(ClientPlayerEntity player) {
        player.getWorld().addParticle(JetLagMain.ROCKET_SPEEDLINE, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
    }
}
