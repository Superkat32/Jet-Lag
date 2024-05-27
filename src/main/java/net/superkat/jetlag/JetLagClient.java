package net.superkat.jetlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.particles.FireworkParticle;
import net.superkat.jetlag.particles.WindLineParticle;
import net.superkat.jetlag.particles.WindParticle;
import net.superkat.jetlag.rendering.ContrailRenderer;
import net.superkat.jetlag.rendering.SpeedlineRenderer;

public class JetLagClient implements ClientModInitializer {
    public static ShaderProgram rainbowShader;
    @Override
    public void onInitializeClient() {
//        JetLagConfig.INSTANCE.load();

        ParticleFactoryRegistry.getInstance().register(JetLagMain.FIREWORKPARTICLE, FireworkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND1, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND2, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND3, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagMain.WIND_LINE, WindLineParticle.Factory::new);

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            Identifier id = new Identifier(JetLagMain.MOD_ID, "rainbow");
            context.register(id, VertexFormats.POSITION, program -> rainbowShader = program);
        });

        WorldRenderEvents.END.register(ContrailRenderer::airStreakWorldRendering);
        HudRenderCallback.EVENT.register(SpeedlineRenderer::speedlineRendering);
    }
}
