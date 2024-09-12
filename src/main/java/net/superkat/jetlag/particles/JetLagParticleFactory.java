package net.superkat.jetlag.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;


import org.jetbrains.annotations.Nullable;

//? if (<=1.20.4) {
//import net.minecraft.particle.DefaultParticleType;
//?} else {
import net.minecraft.particle.SimpleParticleType;
//?}

/**
 * Really over-engineered solution to not wanting to use Stonecutter's variable function.
 *
 * @see IJetLagParticleFactory
 */
public abstract class JetLagParticleFactory implements IJetLagParticleFactory {

    @Override
    public @Nullable Particle createParticle(
            //? if (<=1.20.4) {
            //DefaultParticleType parameters,
            //?} else {
            SimpleParticleType parameters,
            //?}
            ClientWorld world, double x, double y, double z, double velX, double velY, double velZ) {
        return createParticle(world, x, y, z, velX, velY, velZ);
    }

}
