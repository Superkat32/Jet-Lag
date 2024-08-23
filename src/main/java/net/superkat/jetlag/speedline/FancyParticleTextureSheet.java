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
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            PARTICLE_SHEET_TRANSLUCENT.begin(builder, textureManager);
//            RenderSystem.depthMask(true);
//            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
//            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        @Override
        public void draw(Tessellator tessellator) {
//            tessellator.draw();
            PARTICLE_SHEET_TRANSLUCENT.draw(tessellator);
        }

        public String toString() {
            return "JETLAG_FANCY_RAINBOW_TRANSLUCENT";
        }
    };

    ParticleTextureSheet JETLAG_FANCY_RAINBOW_TRANSLUCENT_ROCKET = new ParticleTextureSheet() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            //surely this won't cause any issues, right?
            PARTICLE_SHEET_TRANSLUCENT.begin(builder, textureManager);
//            RenderSystem.depthMask(true);
//            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
//            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        @Override
        public void draw(Tessellator tessellator) {
//            tessellator.draw();
            PARTICLE_SHEET_TRANSLUCENT.draw(tessellator);
        }

        public String toString() {
            return "JETLAG_FANCY_RAINBOW_TRANSLUCENT_ROCKET";
        }
    };
}
