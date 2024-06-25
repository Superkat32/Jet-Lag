package net.superkat.jetlag.compat;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.config.ModConfig;

public class DABRCompat {
    public static double getPlayerRoll(AbstractClientPlayerEntity player, float tickDelta) {
        //FIXME - Figure out how to import DABR's depends without using mods folder and re-adding the depends
        return ((RollEntity) player).doABarrelRoll$getRoll(tickDelta);
    }

    public static boolean DABREnabled() {
        return ModConfig.INSTANCE.getModEnabled();
    }
}
