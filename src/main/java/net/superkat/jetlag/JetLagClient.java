package net.superkat.jetlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.contrail.ContrailHandler;
import net.superkat.jetlag.contrail.JetLagPlayer;
import net.superkat.jetlag.particles.*;
import net.superkat.jetlag.rendering.ContrailRenderer;
import net.superkat.jetlag.rendering.SpeedlineRenderer;
import net.superkat.jetlag.speedline.SpeedlineHandler;

public class JetLagClient implements ClientModInitializer {
    public static ShaderProgram rainbowShader;
    public static ShaderProgram rainbowParticle;

    @Override
    public void onInitializeClient() {
//        JetLagConfig.INSTANCE.load();

        ParticleFactoryRegistry.getInstance().register(JetLagParticles.FIREWORKPARTICLE, FireworkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.WIND1, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.WIND2, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.WIND3, WindParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.WIND_LINE, WindLineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.CAMERA_TEST, CameraParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.SPEEDLINE, SpeedlineParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JetLagParticles.ROCKET_SPEEDLINE, RocketSpeedlineParticle.Factory::new);

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            Identifier rainbowId =
                    //? if (>=1.21) {
                    Identifier.of(JetLagMain.MOD_ID, "rainbow");
                    //?} else {
//                    new Identifier(JetLagMain.MOD_ID, "rainbow");
                    //?}
            context.register(rainbowId, VertexFormats.POSITION, program -> rainbowShader = program);

            Identifier fancyId =
                    //? if (>=1.21) {
                    Identifier.of(JetLagMain.MOD_ID, "fancyparticle");
                    //?} else {
//                    new Identifier(JetLagMain.MOD_ID, "fancyparticle");
                    //?}
            context.register(fancyId, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT, program -> rainbowParticle = program);
        });

        //BEFORE_DEBUG_RENDER is called as closely to the particles being rendered as I can get with Fabric API.
        //In theory, no matrix stack changes should be made between the debug render and the particles render
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(ContrailRenderer::contrailWorldRendering);

        ClientTickEvents.END_CLIENT_TICK.register(SpeedlineHandler::tickSpeedlines);
        ClientTickEvents.END_WORLD_TICK.register(ContrailHandler::tickContrails);
        HudRenderCallback.EVENT.register(SpeedlineRenderer::speedlineRendering);

//        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> );

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if(entity instanceof AbstractClientPlayerEntity player) {
                JetLagPlayer jetLagPlayer = (JetLagPlayer) player;
                jetLagPlayer.jetlag$endCurrentContrail();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register(ContrailHandler::onDisconnect);
    }

    public static boolean DABRLoaded() {
        return FabricLoader.getInstance().isModLoaded("do_a_barrel_roll");
    }
}
