package net.superkat.jetlag.config;


import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController;
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
    @SerialEntry public int fadeInPoints = 20;
    @SerialEntry public int fadeOutPoints = 30;
    @SerialEntry public double contrailWidth = 0.1;
    @SerialEntry public float contrailHeight = 0.1f;
    @SerialEntry public int contrailCurvePoints = 3;
    @SerialEntry public int contrailDeletionDelay = 1;
    @SerialEntry public int pointsDeletedPerDelay = 1;

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

            var contrailGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.contrail.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.contrail.group.tooltip"))
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
                    .customController(opt -> new IntegerSliderController(opt, 0, 200, 1))
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
                    .customController(opt -> new IntegerSliderController(opt, 0, 50, 1))
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
                    .customController(opt -> new IntegerSliderController(opt, 0, 50, 1))
                    .build();

            var curvePoints = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.curvepoints"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.curvepoints.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailCurvePoints,
                            () -> config.contrailCurvePoints,
                            val -> config.contrailCurvePoints = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 48, 1))
                    .build();

            var contrailWidth = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.contrailwidth"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.contrailwidth.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailWidth,
                            () -> config.contrailWidth,
                            val -> config.contrailWidth = val
                    )
                    .customController(opt -> new DoubleSliderController(opt, 0.05, 0.5, 0.05))
                    .build();

            var deleteDelay = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.deletedelay"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.deletedelay.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailDeletionDelay,
                            () -> config.contrailDeletionDelay,
                            val -> config.contrailDeletionDelay = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 10, 1))
                    .build();

            var pointsPerDelete = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.pointsperdelete"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.pointsperdelete.tooltip"))
                            .build())
                    .binding(
                            defaults.pointsDeletedPerDelay,
                            () -> config.pointsDeletedPerDelay,
                            val -> config.pointsDeletedPerDelay = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 10, 1))
                    .build();

            contrailGroup.option(maxPoints);
            contrailGroup.option(fadeInPoints);
            contrailGroup.option(fadeOutPoints);
            contrailGroup.option(curvePoints);
            contrailGroup.option(contrailWidth);
            contrailGroup.option(deleteDelay);
            contrailGroup.option(pointsPerDelete);
            defaultCategoryBuilder.group(contrailGroup.build());

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
