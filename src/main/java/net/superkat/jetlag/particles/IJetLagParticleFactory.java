package net.superkat.jetlag.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;

//? if (<=1.20.4) {
/*import net.minecraft.particle.DefaultParticleType;
*///?} else {
import net.minecraft.particle.SimpleParticleType;
//?}


/**
 * This is a really over-engineered solution to not wanting to use Stonecutter's variable thing because I think this makes it slightly more readable.
 * <br> <br>
 * If you are browsing this particle code wondering if something like this is required for your own project,
 * something like it is only needed if you are using Stonecutter. Otherwise, your static Factory class can implement ParticleFactory.
 */
public interface IJetLagParticleFactory
    //? if (<=1.20.4) {
        /*extends ParticleFactory<DefaultParticleType> {
    *///?} else {
    extends ParticleFactory<SimpleParticleType> {
    //?}

    //create particle method with no DefaultParticleType or SimpleParticleType
    Particle createParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ);
}
