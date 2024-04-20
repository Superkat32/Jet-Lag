package net.superkat.jetlag.config;


import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.JetLagMain;

import java.nio.file.Path;

public class JetLagConfig {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("jetlag.json");

    public static final ConfigClassHandler<JetLagConfig> INSTANCE = ConfigClassHandler.<JetLagConfig>createBuilder(JetLagConfig.class)
            .id(new Identifier(JetLagMain.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .build())
            .build();

    public static void load() {
        INSTANCE.load();
    }

    public static void save() {
        INSTANCE.save();
    }

    public static JetLagConfig getInstance() {
        return INSTANCE.instance();
    }



    @SerialEntry public int maxPoints = 100;
    @SerialEntry public int fadeInPoints = 10;
    @SerialEntry public int fadeOutPoints = 30;
    @SerialEntry public float airStreakWidth = 0.5f;
    @SerialEntry public float airStreakHeight = 0.1f;
    @SerialEntry public int airStreakCurvePoints = 3;

//    @SerialEntry
//    public boolean showSpeedlines = true;
//    @SerialEntry public boolean altSpeedlineTextures = false;
//    @SerialEntry public boolean windGusts = true;
//    @SerialEntry
//    public boolean altFireworkParticles = true;
//    @SerialEntry public boolean alwaysUseAltFireworkParticles = false;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> {

            var defaultCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.default"))
                    .tooltip(Text.translatable("jetlag.category.default.tooltip"));

            var airStreakGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.airstreak.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.airstreak.group.tooltip"))
                            .build());

            var maxPoints = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.maxpoints"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.maxpoint.tooltip"))
                            .build())
                    .binding(
                            defaults.maxPoints,
                            () -> config.maxPoints,
                            val -> config.maxPoints = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 200, 1))
                    .build();

            var fadeInPoints = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.fadeinpoints"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.fadeinpoints.tooltip"))
                            .build())
                    .binding(
                            defaults.fadeInPoints,
                            () -> config.fadeInPoints,
                            val -> config.fadeInPoints = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, maxPoints.pendingValue(), 1))
                    .build();

            var fadeOutPoints = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.fadeoutpoints"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.fadeoutpoints.tooltip"))
                            .build())
                    .binding(
                            defaults.fadeOutPoints,
                            () -> config.fadeOutPoints,
                            val -> config.fadeOutPoints = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, maxPoints.pendingValue(), 1))
                    .build();

            var curvePoints = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.curvepoints"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.curvepoints.tooltip"))
                            .build())
                    .binding(
                            defaults.airStreakCurvePoints,
                            () -> config.airStreakCurvePoints,
                            val -> config.airStreakCurvePoints = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 48, 1))
                    .build();

            airStreakGroup.option(maxPoints);
            airStreakGroup.option(fadeInPoints);
            airStreakGroup.option(fadeOutPoints);
            airStreakGroup.option(curvePoints);
            defaultCategoryBuilder.group(airStreakGroup.build());

            return builder
                .title(Text.translatable("jetlag.title"))
                .category(defaultCategoryBuilder.build());
//
//            var screenEffectsGroup = OptionGroup.createBuilder()
//                    .name(Text.translatable("jetlag.screeneffects.group"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.screeneffects.group.tooltip"))
//                            .build());
//            var showSpeedlines = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.speedlines"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.speedlines.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.showSpeedlines,
//                            () -> config.showSpeedlines,
//                            val -> config.showSpeedlines = val
//                    )
//                    .customController(booleanOption -> new BooleanController(booleanOption, true))
//                    .build();
//            var altSpeedlines = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.speedlines.alt"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.speedlines.alt.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.altSpeedlineTextures,
//                            () -> config.altSpeedlineTextures,
//                            val -> config.altSpeedlineTextures = val
//                    )
//                    .customController(booleanOption -> new BooleanController(booleanOption, true))
//                    .build();
//            screenEffectsGroup.option(showSpeedlines);
//            screenEffectsGroup.option(altSpeedlines);
//            defaultCategoryBuilder.group(screenEffectsGroup.build());
//
//            var particlesGroup = OptionGroup.createBuilder()
//                    .name(Text.translatable("jetlag.particles.group"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.particles.group.tooltip"))
//                            .build());
//            var showWindGusts = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.wind"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.wind.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.windGusts,
//                            () -> config.windGusts,
//                            val -> config.windGusts = val
//                    )
//                    .customController(booleanOption -> new BooleanController(booleanOption, true))
//                    .build();
//            var altFireworkParticles = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.altfirework"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.altfirework.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.altFireworkParticles,
//                            () -> config.altFireworkParticles,
//                            val -> config.altFireworkParticles = val
//                    )
//                    .customController(booleanOption -> new BooleanController(booleanOption, true))
//                    .build();
//            var alwaysUseAltFireworkParticles = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.altfireworkeverywhere"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.altfireworkeverywhere.tooltip"))
//                            .build())
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.altfireworkeverywhere.tooltip.examples"))
//                            .build())
//                    .binding(
//                            defaults.alwaysUseAltFireworkParticles,
//                            () -> config.alwaysUseAltFireworkParticles,
//                            val -> config.alwaysUseAltFireworkParticles = val
//                    )
//                    .customController(booleanOption -> new BooleanController(booleanOption, true))
//                    .build();
//            particlesGroup.option(showWindGusts);
//            particlesGroup.option(altFireworkParticles);
//            particlesGroup.option(alwaysUseAltFireworkParticles);
//            defaultCategoryBuilder.group(particlesGroup.build());

//            return builder
//                    .title(Text.translatable("jetlag.title"))
//                    .category(defaultCategoryBuilder.build());
        }).generateScreen(parent);
    }

}
