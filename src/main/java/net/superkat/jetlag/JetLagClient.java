package net.superkat.jetlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.superkat.jetlag.particles.FireworkParticle;
import net.superkat.jetlag.particles.WindParticle;
import net.superkat.jetlag.rendering.AirStreakRenderer;

public class JetLagClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
//        JetLagConfig.INSTANCE.load();

        ParticleFactoryRegistry.getInstance().register(JetLagMain.FIREWORKPARTICLE, FireworkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND1, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND2, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND3, WindParticle.Factory::new);

        WorldRenderEvents.END.register(AirStreakRenderer::airStreakWorldRendering);
    }
}
