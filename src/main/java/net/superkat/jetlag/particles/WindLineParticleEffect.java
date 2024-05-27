package net.superkat.jetlag.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.superkat.jetlag.JetLagMain;

import java.util.Locale;

public class WindLineParticleEffect implements ParticleEffect {
    @SuppressWarnings("deprecation")
    public static final ParticleEffect.Factory<WindLineParticleEffect> PARAMETER_FACTORY = new ParticleEffect.Factory<>() {
        @Override
        public WindLineParticleEffect read(ParticleType<WindLineParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float pitch = reader.readFloat();
            reader.expect(' ');
            float yaw = reader.readFloat();
            return new WindLineParticleEffect(pitch, yaw);
        }

        @Override
        public WindLineParticleEffect read(ParticleType<WindLineParticleEffect> type, PacketByteBuf buf) {
            float pitch = buf.readFloat();
            float yaw = buf.readFloat();
            return new WindLineParticleEffect(pitch, yaw);
        }
    };

    public float pitch;
    public float yaw;
    public WindLineParticleEffect(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public ParticleType<WindLineParticleEffect> getType() {
        return JetLagMain.WIND_LINE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.pitch);
        buf.writeFloat(this.yaw);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.pitch, this.yaw);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}