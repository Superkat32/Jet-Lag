package net.superkat.jetlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.superkat.jetlag.particles.FireworkParticle;

public class JetLagClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        JetLagConfig.INSTANCE.load();

        ParticleFactoryRegistry.getInstance().register(JetLagMain.FIREWORKPARTICLE, FireworkParticle.Factory::new);
    }
}
