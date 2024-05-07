package net.superkat.jetlag.config;


import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.ColorController;
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.JetLagMain;

import java.awt.*;
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
    @SerialEntry public double contrailWidthAddition = 0.04;
    @SerialEntry public float contrailOpacityAdjustment = 0.25f;
    @SerialEntry public boolean velocityBasedOpacityAdjust = true;
    @SerialEntry public int contrailCurvePoints = 3;
    @SerialEntry public int ticksPerPoint = 1;
    @SerialEntry public int contrailDeletionDelay = 1;
    @SerialEntry public int pointsDeletedPerDelay = 1;
    @SerialEntry public Color contrailColor = new Color(220, 255, 255, 118);

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
                    .customController(opt -> new IntegerSliderController(opt, 1, 36, 1))
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
                    .customController(opt -> new DoubleSliderController(opt, 0.05, 0.5, 0.01))
                    .build();

            var contrailWidthAddition = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.contrailwidthadd"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.contrailwidthadd.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailWidthAddition,
                            () -> config.contrailWidthAddition,
                            val -> config.contrailWidthAddition = val
                    )
                    .customController(opt -> new DoubleSliderController(opt, 0, 0.1, 0.01))
                    .build();

            var contrailOpacityAdjustment = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.contrailopacityadjustment"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.contrailopacityadjustment.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailOpacityAdjustment,
                            () -> config.contrailOpacityAdjustment,
                            val -> config.contrailOpacityAdjustment = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 1f, 0.05f))
                    .build();

            var velocityBasedOpacityAdjust = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.velocityopacityadjust"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.velocityopacityadjust.tooltip"))
                            .build())
                    .binding(
                            defaults.velocityBasedOpacityAdjust,
                            () -> config.velocityBasedOpacityAdjust,
                            val -> config.velocityBasedOpacityAdjust = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            var ticksPerPoint = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.ticksperpoint"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.ticksperpoint.tooltip"))
                            .build())
                    .binding(
                            defaults.ticksPerPoint,
                            () -> config.ticksPerPoint,
                            val -> config.ticksPerPoint = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 20, 1))
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

            var contrailColor = Option.<Color>createBuilder()
                    .name(Text.translatable("jetlag.contrailcolor"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.contrailcolor.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailColor,
                            () -> config.contrailColor,
                            val -> config.contrailColor = val
                    )
                    .customController(opt -> new ColorController(opt, true))
                    .build();

            var testCategory = TestCategory.createBuilder()
                    .name(Text.of("name"))
                    .tooltip(Text.of("tooltip"));

            contrailGroup.option(maxPoints);
            contrailGroup.option(fadeInPoints);
            contrailGroup.option(fadeOutPoints);
            contrailGroup.option(curvePoints);
            contrailGroup.option(contrailWidth);
            contrailGroup.option(contrailWidthAddition);
            contrailGroup.option(contrailOpacityAdjustment);
            contrailGroup.option(velocityBasedOpacityAdjust);
            contrailGroup.option(ticksPerPoint);
            contrailGroup.option(deleteDelay);
            contrailGroup.option(pointsPerDelete);
            contrailGroup.option(contrailColor);
            defaultCategoryBuilder.group(contrailGroup.build());


            return builder
                .title(Text.translatable("jetlag.title"))
                .category(defaultCategoryBuilder.build())
                    .category(testCategory.build());
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
