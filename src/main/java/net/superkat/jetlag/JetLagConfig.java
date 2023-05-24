package net.superkat.jetlag;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import dev.isxander.yacl.gui.controllers.BooleanController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Path;

public class JetLagConfig {

//    public static final ConfigInstance<JetLagConfig> INSTANCE = new GsonConfigInstance<>(JetLagConfig.class, Path.of("./config/jetlag.json"));
    public static final GsonConfigInstance<JetLagConfig> INSTANCE = GsonConfigInstance.<JetLagConfig>createBuilder(JetLagConfig.class).setPath(Path.of("./config/jetlag.json")).build();

    @ConfigEntry public boolean showSpeedlines = true;
    @ConfigEntry public boolean altSpeedlineTextures = false;
    @ConfigEntry public boolean windGusts = true;
    @ConfigEntry public boolean altFireworkParticles = true;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> {
            var defaultCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.default"))
                    .tooltip(Text.translatable("jetlag.category.default.tooltip"));

            var screenEffectsGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.screeneffects.group"))
                    .tooltip(Text.translatable("jetlag.screeneffects.group.tooltip"));
            var showSpeedlines = Option.createBuilder(boolean.class)
                    .name(Text.translatable("jetlag.speedlines"))
                    .tooltip(Text.translatable("jetlag.speedlines.tooltip"))
                    .binding(
                            defaults.showSpeedlines,
                            () -> config.showSpeedlines,
                            val -> config.showSpeedlines = val
                    )
                    .controller(booleanOption -> new BooleanController(booleanOption, true))
                    .build();
            var altSpeedlines = Option.createBuilder(boolean.class)
                    .name(Text.translatable("jetlag.speedlines.alt"))
                    .tooltip(Text.translatable("jetlag.speedlines.alt.tooltip"))
                    .binding(
                            defaults.altSpeedlineTextures,
                            () -> config.altSpeedlineTextures,
                            val -> config.altSpeedlineTextures = val
                    )
                    .controller(booleanOption -> new BooleanController(booleanOption, true))
                    .build();
            screenEffectsGroup.option(showSpeedlines);
            screenEffectsGroup.option(altSpeedlines);
            defaultCategoryBuilder.group(screenEffectsGroup.build());

            var particlesGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.particles.group"))
                    .tooltip(Text.translatable("jetlag.particles.group.tooltip"));
            var showWindGusts = Option.createBuilder(boolean.class)
                    .name(Text.translatable("jetlag.wind"))
                    .tooltip(Text.translatable("jetlag.wind.tooltip"))
                    .binding(
                            defaults.windGusts,
                            () -> config.windGusts,
                            val -> config.windGusts = val
                    )
                    .controller(booleanOption -> new BooleanController(booleanOption, true))
                    .build();
            var altFireworkParticles = Option.createBuilder(boolean.class)
                    .name(Text.translatable("jetlag.altfirework"))
                    .tooltip(Text.translatable("jetlag.altfirework.tooltip"))
                    .binding(
                            defaults.altFireworkParticles,
                            () -> config.altFireworkParticles,
                            val -> config.altFireworkParticles = val
                    )
                    .controller(booleanOption -> new BooleanController(booleanOption, true))
                    .build();
            particlesGroup.option(showWindGusts);
            particlesGroup.option(altFireworkParticles);
            defaultCategoryBuilder.group(particlesGroup.build());

            return builder
                    .title(Text.translatable("jetlag.title"))
                    .category(defaultCategoryBuilder.build());
        }).generateScreen(parent);
    }

}
