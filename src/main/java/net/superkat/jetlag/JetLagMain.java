package net.superkat.jetlag;

import net.fabricmc.api.ModInitializer;
import net.superkat.jetlag.config.JetLagConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetLagMain implements ModInitializer {
    public static final String MOD_ID = "jetlag";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        JetLagConfig.load();

        JetLagParticles.initParticles();
    }
}