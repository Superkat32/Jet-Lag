package net.superkat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.superkat.particles.FireworkParticle;

public class JetLagClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(JetLagMain.FIREWORKPARTICLE, FireworkParticle.Factory::new);
    }
}
