package net.superkat.jetlag;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.particles.WindLineParticleEffect;

public class WindLineHandler {

    public static void spawnWindLineParticles(LivingEntity entity) {
        JetLagConfig config = JetLagConfig.getInstance();
        if(!config.modEnabled) return;

        if(entity instanceof ClientPlayerEntity) {
            if(!config.windLinesInFirstPerson && MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) return;
        }

        int max = (int) Math.round(entity.getVelocity().lengthSquared()) + 2;
        int min = (int) Math.round(entity.getVelocity().length());
        int amount = entity.getRandom().nextBetween(min, max);

        if(amount != 0) {
            //cache these variables to hopefully make a small amount of lag decrease
            float velX = (float) -entity.getVelocity().getX() / 2f;
            float velZ = (float) -entity.getVelocity().getZ() / 2f;
            Vec3d offset = entity.getRotationVector().multiply(8);
            for (int i = 0; i < amount; i++) {
                spawnParticle(entity, offset.getX(), offset.getY(), offset.getZ(), velX, velZ);
            }
        }
    }

    public static void spawnParticle(LivingEntity entity, double offsetX, double offsetY, double offsetZ, float velX, float velZ) {
        Random random = entity.getRandom();
        float multiplier = 2.25f;
        float x = (float) (entity.getX() + offsetX + random.nextFloat() * multiplier * (random.nextBoolean() ? 1 : -1));
        float y = (float) (entity.getY() + offsetY + random.nextFloat() * multiplier * (random.nextBoolean() ? 1 : -1));
        float z = (float) (entity.getZ() + offsetZ + random.nextFloat() * multiplier * (random.nextBoolean() ? 1 : -1));
        entity.getWorld().addParticle(new WindLineParticleEffect(entity.getPitch(), entity.getBodyYaw()), x, y, z, velX, 0f, velZ);
    }
}
