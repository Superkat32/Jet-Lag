package net.superkat.jetlag.airstreak;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.rendering.AirStreakRenderer;

import java.util.List;

public class AirStreakHandler {
    //This is really just a utils class which can be hot swapped

    public static void determineAirStreakRendering(WorldRenderContext context) {
        List<AbstractClientPlayerEntity> players = context.world().getPlayers();
        for(AbstractClientPlayerEntity abstractPlayer : players) {
            if(abstractPlayer instanceof ClientPlayerEntity player) {
                JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
                if(player.isFallFlying()) {
                    if(jetLagPlayer.jetLag$getPlayerAirStreaks() == null) {
                        jetLagPlayer.jetLag$setAirStreak(new AirStreak(player));
                    }
                } if (jetLagPlayer.jetLag$getPlayerAirStreaks() != null) {
                    AirStreakRenderer.renderAirStreaks(context, player);
                }
            }
        }
    }

    public static void updatePlayerAirStreaks(ClientPlayerEntity player) {
        JetLagClientPlayerEntity jetLagPlayer = (JetLagClientPlayerEntity) player;
        AirStreak playerAirStreak = jetLagPlayer.jetLag$getPlayerAirStreaks();
        playerAirStreak.tick();
    }

    public static void changeElytraRotation(ModelPart leftWing, ModelPart rightWing) {
        //this was used for testing purposes
//        System.out.println(leftWing.roll);
        //-1.5707958 is the absolute max roll the left wing can have
//        rightWing.roll = 1.5f;
    }
}
