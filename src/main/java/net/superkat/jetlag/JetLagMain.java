package net.superkat.jetlag;

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
    public static final DefaultParticleType WIND1 = FabricParticleTypes.simple();
    public static final DefaultParticleType WIND2 = FabricParticleTypes.simple();
    public static final DefaultParticleType WIND3 = FabricParticleTypes.simple();
    public static final DefaultParticleType MY_PARTICLE = FabricParticleTypes.simple();

    @Override
    public void onInitialize() {
        JetLagConfig.load();

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "fireworkparticle"), FIREWORKPARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "wind1"), WIND1);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "wind2"), WIND2);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "wind3"), WIND3);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "my_particle"), MY_PARTICLE);
    }
}