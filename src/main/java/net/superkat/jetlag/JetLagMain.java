package net.superkat.jetlag;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.particles.WindLineParticleEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class JetLagMain implements ModInitializer {
    public static final String MOD_ID = "jetlag";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final DefaultParticleType FIREWORKPARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType WIND1 = FabricParticleTypes.simple();
    public static final DefaultParticleType WIND2 = FabricParticleTypes.simple();
    public static final DefaultParticleType WIND3 = FabricParticleTypes.simple();
    public static final List<DefaultParticleType> windParticles = Arrays.asList(WIND1, WIND2, WIND3);
    public static final ParticleType<WindLineParticleEffect> WIND_LINE = FabricParticleTypes.complex(WindLineParticleEffect.PARAMETER_FACTORY);

    @Override
    public void onInitialize() {
        JetLagConfig.load();

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "fireworkparticle"), FIREWORKPARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "wind1"), WIND1);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "wind2"), WIND2);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "wind3"), WIND3);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "windline"), WIND_LINE);
    }

    public static boolean canTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        return !client.isPaused() && (client.world == null || client.world.getTickManager().shouldTick());
    }
}