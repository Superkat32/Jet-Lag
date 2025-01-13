package net.superkat.jetlag.speedline;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;

public interface FancyParticleTextureSheet {
    //putting jetlag in the name incase it cases any crashes, should be easily noticeable at that point if this is included in the name
    //otherwise, anyone reading my code will be able to tell it is custom-made :)
    ParticleTextureSheet JETLAG_FANCY_RAINBOW_TRANSLUCENT = new ParticleTextureSheet() {
        @Override
        public BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
            return PARTICLE_SHEET_TRANSLUCENT.begin(tessellator, textureManager);
        }

        public String toString() {
            return "JETLAG_FANCY_RAINBOW_TRANSLUCENT";
        }
    };

    ParticleTextureSheet JETLAG_FANCY_RAINBOW_TRANSLUCENT_ROCKET = new ParticleTextureSheet() {
        @Override
        public BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
            return PARTICLE_SHEET_TRANSLUCENT.begin(tessellator, textureManager);
        }

        public String toString() {
            return "JETLAG_FANCY_RAINBOW_TRANSLUCENT_ROCKET";
        }
    };
}
