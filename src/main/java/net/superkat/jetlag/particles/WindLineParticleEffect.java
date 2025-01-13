package net.superkat.jetlag.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.superkat.jetlag.JetLagParticles;

public class WindLineParticleEffect implements ParticleEffect {
    public float pitch;
    public float yaw;
    public WindLineParticleEffect(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public ParticleType<WindLineParticleEffect> getType() {
        return JetLagParticles.WIND_LINE;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public static final MapCodec<WindLineParticleEffect> CODEC;
    public static final PacketCodec<RegistryByteBuf, WindLineParticleEffect> PACKET_CODEC;

    static {
        CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(
                    Codec.FLOAT.fieldOf("pitch").forGetter(WindLineParticleEffect::getPitch),
                    Codec.FLOAT.fieldOf("yaw").forGetter(WindLineParticleEffect::getYaw)
            ).apply(instance, WindLineParticleEffect::new);
        });

        PACKET_CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, (effect) -> {
            return effect.pitch;
        }, PacketCodecs.FLOAT, (effect) -> {
            return effect.yaw;
        }, WindLineParticleEffect::new);
    }
}