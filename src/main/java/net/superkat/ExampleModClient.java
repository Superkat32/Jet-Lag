package net.superkat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.superkat.particles.SpeedlinesParticle;

public class ExampleModClient implements ClientModInitializer {

//    SpeedlineRenderer speedlineRenderer;

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ExampleMod.MY_PARTICLE, SpeedlinesParticle.Factory::new);
//        HudRenderCallback.EVENT.register(((matrixStack, tickDelta) -> speedlineRenderer.renderTestItem(matrixStack)));
    }
}
