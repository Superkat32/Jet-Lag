package net.superkat.jetlag;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.particles.WindLineParticleEffect;

import java.util.Arrays;
import java.util.List;

import static net.superkat.jetlag.JetLagMain.MOD_ID;

public class JetLagParticles {

    //? if (<=1.20.4) {
//    public static final DefaultParticleType FIREWORKPARTICLE = FabricParticleTypes.simple();
//    public static final DefaultParticleType WIND1 = FabricParticleTypes.simple();
//    public static final DefaultParticleType WIND2 = FabricParticleTypes.simple();
//    public static final DefaultParticleType WIND3 = FabricParticleTypes.simple();
//    public static final List<DefaultParticleType> windParticles = Arrays.asList(WIND1, WIND2, WIND3);
//    public static final ParticleType<WindLineParticleEffect> WIND_LINE = FabricParticleTypes.complex(WindLineParticleEffect.PARAMETER_FACTORY);
//    public static final DefaultParticleType CAMERA_TEST = FabricParticleTypes.simple();
//    public static final DefaultParticleType SPEEDLINE = FabricParticleTypes.simple();
//    public static final DefaultParticleType ROCKET_SPEEDLINE = FabricParticleTypes.simple();
    //?} else {
    public static final SimpleParticleType FIREWORKPARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType WIND1 = FabricParticleTypes.simple();
    public static final SimpleParticleType WIND2 = FabricParticleTypes.simple();
    public static final SimpleParticleType WIND3 = FabricParticleTypes.simple();
    public static final List<ParticleEffect> windParticles = Arrays.asList(WIND1, WIND2, WIND3);
    public static final ParticleType<WindLineParticleEffect> WIND_LINE = FabricParticleTypes.complex(WindLineParticleEffect.CODEC, WindLineParticleEffect.PACKET_CODEC);
    public static final SimpleParticleType CAMERA_TEST = FabricParticleTypes.simple();
    public static final SimpleParticleType SPEEDLINE = FabricParticleTypes.simple();
    public static final SimpleParticleType ROCKET_SPEEDLINE = FabricParticleTypes.simple();
    //?}

    public static void initParticles() {
        Registry.register(Registries.PARTICLE_TYPE, id("fireworkparticle"), JetLagParticles.FIREWORKPARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, id("wind1"), JetLagParticles.WIND1);
        Registry.register(Registries.PARTICLE_TYPE, id("wind2"), JetLagParticles.WIND2);
        Registry.register(Registries.PARTICLE_TYPE, id("wind3"), JetLagParticles.WIND3);
        Registry.register(Registries.PARTICLE_TYPE, id("windline"), JetLagParticles.WIND_LINE);
        Registry.register(Registries.PARTICLE_TYPE, id("camera_test"), JetLagParticles.CAMERA_TEST);
        Registry.register(Registries.PARTICLE_TYPE, id("speedline"), JetLagParticles.SPEEDLINE);
        Registry.register(Registries.PARTICLE_TYPE, id("rocket_speedline"), JetLagParticles.ROCKET_SPEEDLINE);
    }

    private static Identifier id(String path) {
        //? if (>=1.21) {
        return Identifier.of(MOD_ID, path);
        //?} else {
//        return new Identifier(MOD_ID, path);
        //?}
    }
}
