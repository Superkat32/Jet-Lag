package net.superkat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetLagMain implements ModInitializer {
    public static final String MOD_ID = "jetlag";
    public static final Logger LOGGER = LoggerFactory.getLogger("jetlag");
    public static final DefaultParticleType FIREWORKPARTICLE = FabricParticleTypes.simple();

    @Override
    public void onInitialize() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "fireworkparticle"), FIREWORKPARTICLE);
    }
}