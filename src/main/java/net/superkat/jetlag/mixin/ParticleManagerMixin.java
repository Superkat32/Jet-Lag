package net.superkat.jetlag.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.superkat.jetlag.speedline.FancyParticleTextureSheet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    //I can't believe this worked!

    @Mutable @Shadow @Final
    private static List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS;

    static {
        PARTICLE_TEXTURE_SHEETS = new ImmutableList.Builder<ParticleTextureSheet>()
                .addAll(PARTICLE_TEXTURE_SHEETS).add(FancyParticleTextureSheet.JETLAG_FANCY_RAINBOW_TRANSLUCENT, FancyParticleTextureSheet.JETLAG_FANCY_RAINBOW_TRANSLUCENT_ROCKET).build();
    }

}
