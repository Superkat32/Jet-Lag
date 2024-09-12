package net.superkat.jetlag.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.superkat.jetlag.JetLagParticles;

//? if (<=1.20.4) {
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.registry.Registries;
//import java.util.Locale;
//?} else {
import com.mojang.serialization.Codec;
//?}


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

    //? if (<=1.20.4) {
//    @SuppressWarnings("deprecation")
//    public static final ParticleEffect.Factory<WindLineParticleEffect> PARAMETER_FACTORY = new ParticleEffect.Factory<>() {
//        @Override
//        public WindLineParticleEffect read(ParticleType<WindLineParticleEffect> type, StringReader reader) throws CommandSyntaxException {
//            reader.expect(' ');
//            float pitch = reader.readFloat();
//            reader.expect(' ');
//            float yaw = reader.readFloat();
//            return new WindLineParticleEffect(pitch, yaw);
//        }
//
//        @Override
//        public WindLineParticleEffect read(ParticleType<WindLineParticleEffect> type, PacketByteBuf buf) {
//            float pitch = buf.readFloat();
//            float yaw = buf.readFloat();
//            return new WindLineParticleEffect(pitch, yaw);
//        }
//    };
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeFloat(this.pitch);
//        buf.writeFloat(this.yaw);
//    }
//
//    @Override
//    public String asString() {
//        return String.format(Locale.ROOT, "%s %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.pitch, this.yaw);
//    }

    //?} else {
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
    //?}
}