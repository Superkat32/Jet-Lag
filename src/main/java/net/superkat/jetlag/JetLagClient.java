package net.superkat.jetlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.airstreak.JetLagClientPlayerEntity;
import net.superkat.jetlag.particles.FireworkParticle;
import net.superkat.jetlag.particles.WindParticle;
import net.superkat.jetlag.rendering.AirStreak;
import net.superkat.jetlag.rendering.AirStreakRenderer;

import java.util.List;

public class JetLagClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
//        JetLagConfig.INSTANCE.load();

        ParticleFactoryRegistry.getInstance().register(JetLagMain.FIREWORKPARTICLE, FireworkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND1, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND2, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND3, WindParticle.Factory::new);

        WorldRenderEvents.END.register(context -> {
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
        });
    }
}
