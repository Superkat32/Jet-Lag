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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.superkat.jetlag.JetLagMain;

import java.awt.*;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

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

    //contrails
    @SerialEntry public boolean contrailsEnabled = true;
    @SerialEntry public int maxPoints = 100;
    @SerialEntry public int fadeInPoints = 20;
    @SerialEntry public int fadeOutPoints = 30;
    @SerialEntry public int contrailCurvePoints = 3;
    @SerialEntry public Color contrailColor = new Color(220, 255, 255, 118);
    @SerialEntry public double contrailWidth = 0.1;
    @SerialEntry public double contrailWidthAddition = 0.04;
    @SerialEntry public float contrailOpacityAdjustment = 0.25f;
    @SerialEntry public boolean mirrorOpacity = false;
    @SerialEntry public boolean velocityBasedOpacityAdjust = true;
    @SerialEntry public int ticksPerPoint = 1;
    @SerialEntry public int contrailDeletionDelay = 1;
    @SerialEntry public int pointsDeletedPerDelay = 1;

    //speedlines
    @SerialEntry public boolean speedlinesEnabled = true;
    @SerialEntry public SpeedlineConfigInstance speedlineConfig = new SpeedlineConfigInstance();
    @SerialEntry public boolean rocketSpeedlinesEnabled = true;
    @SerialEntry public SpeedlineConfigInstance rocketConfig = new SpeedlineConfigInstance().rocketMode();

    //particles
    @SerialEntry public boolean windLines = true;
    @SerialEntry public Color windLinesColor = Color.white;
    @SerialEntry public boolean windLinesInFirstPerson = false;
    @SerialEntry public boolean windGusts = true;
    @SerialEntry public boolean useMinecraftWindGusts = false;
    @SerialEntry public boolean altFireworkParticles = true;
    @SerialEntry public boolean alwaysUseAltFireworkParticles = false;

    //screen VFX
    @SerialEntry public boolean showSpeedlinesTexture = true;
    @SerialEntry public boolean velocityBasedSpeedlinesOpacity = true;
    @SerialEntry public boolean rainbowSpeedlines = false;
    @SerialEntry public Color speedlinesColor = Color.white;
    @SerialEntry public boolean onlyShowSpeedlinesInFirstPerson = true;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> {

            var contrailCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.contrail"))
                    .tooltip(Text.translatable("jetlag.category.contrail.tooltip"));

            var contrailsEnabled = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.contrailsenabled"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.contrailsenabled.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailsEnabled,
                            () -> config.contrailsEnabled,
                            val -> config.contrailsEnabled = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var pointsGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.points.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.points.group.tooltip"))
                            .build());

            var maxPoints = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.maxpoints"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.maxpoint.tooltip"))
                            .text(Text.translatable("jetlag.lagwarning"))
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
                            .text(Text.translatable("jetlag.superlagwarning"))
                            .build())
                    .binding(
                            defaults.contrailCurvePoints,
                            () -> config.contrailCurvePoints,
                            val -> config.contrailCurvePoints = val
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 24, 1))
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

            var widthAndOpacityGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.widthopacity.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.widthopacity.group.tooltip"))
                            .build());

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
                            .text(Text.of(""))
                            .text(Text.translatable("jetlag.artifactwarning"))
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

            var mirrorOpacity = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.mirroropacity"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.mirroropacity.tooltip"))
                            .build())
                    .binding(
                            defaults.mirrorOpacity,
                            () -> config.mirrorOpacity,
                            val -> config.mirrorOpacity = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
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
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var spawningAndDeletingGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.spawndelete.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.spawndelete.group.tooltip"))
                            .build());

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

            contrailCategoryBuilder.option(contrailsEnabled);
            pointsGroup.option(maxPoints);
            pointsGroup.option(fadeInPoints);
            pointsGroup.option(fadeOutPoints);
            pointsGroup.option(curvePoints);
            pointsGroup.option(contrailColor);
            widthAndOpacityGroup.option(contrailWidth);
            widthAndOpacityGroup.option(contrailWidthAddition);
            widthAndOpacityGroup.option(contrailOpacityAdjustment);
            widthAndOpacityGroup.option(mirrorOpacity);
            widthAndOpacityGroup.option(velocityBasedOpacityAdjust);
            spawningAndDeletingGroup.option(ticksPerPoint);
            spawningAndDeletingGroup.option(deleteDelay);
            spawningAndDeletingGroup.option(pointsPerDelete);

            contrailCategoryBuilder.group(pointsGroup.build());
            contrailCategoryBuilder.group(widthAndOpacityGroup.build());
            contrailCategoryBuilder.group(spawningAndDeletingGroup.build());

            var speedlinesCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.speedlines"))
                    .tooltip(Text.translatable("jetlag.category.speedlines.tooltip"), Text.translatable("jetlag.category.speedlines.wipwarning"));

            var speedlinesEnabled = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedlinesenabled"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinesenabled.tooltip"))
                            .build())
                    .binding(
                            defaults.speedlinesEnabled,
                            () -> config.speedlinesEnabled,
                            val -> config.speedlinesEnabled = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var rocketSpeedlinesEnabled = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.rocketspeedlinesenabled"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.rocketspeedlinesenabled.tooltip"))
                            .build())
                    .binding(
                            defaults.rocketSpeedlinesEnabled,
                            () -> config.rocketSpeedlinesEnabled,
                            val -> config.rocketSpeedlinesEnabled = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var speedlinePresetsGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.speedlinepresets.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinepresets.group.tooltip"))
                            .build());

            var speedlinesGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.speedlines.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlines.group.tooltip"))
                            .build());

            SpeedlineConfigOptionContainer speedlineOptions = new SpeedlineConfigOptionContainer(speedlinesGroup, defaults.speedlineConfig, config.speedlineConfig, Formatting.WHITE);
            speedlineOptions.buildOptions();

            var rocketSpeedlinesGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.rocketspeedlines.group").formatted(Formatting.AQUA))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.rocketspeedlines.group.tooltip"))
                            .build());

            SpeedlineConfigOptionContainer rocketSpeedlineOptions = new SpeedlineConfigOptionContainer(rocketSpeedlinesGroup, defaults.rocketConfig, config.rocketConfig, Formatting.AQUA);
            rocketSpeedlineOptions.buildOptions();

            var subtlePreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedlinepreset.subtle"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().subtleAndSweet());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_subtleAndSweet());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                Text.translatable("jetlag.title"),
                                Text.translatable("jetlag.speedlinepreset.previewing").append(buttonOption.name())
                            )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedlinepreset.preview"))
                    .build();

            var boldPreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedlinepreset.bold"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().niceAndNoticeable());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_niceAndNoticeable());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                        Text.translatable("jetlag.title"),
                                        Text.translatable("jetlag.speedlinepreset.previewing").append(buttonOption.name())
                                )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedlinepreset.preview"))
                    .build();

            var animePreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedlinepreset.anime"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().animeStyled());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_animeStyled());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                        Text.translatable("jetlag.title"),
                                        Text.translatable("jetlag.speedlinepreset.previewing").append(buttonOption.name())
                                )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedlinepreset.preview"))
                    .build();

            speedlinePresetsGroup.option(subtlePreset);
            speedlinePresetsGroup.option(boldPreset);
            speedlinePresetsGroup.option(animePreset);

            speedlinesCategoryBuilder.option(speedlinesEnabled);
            speedlinesCategoryBuilder.option(rocketSpeedlinesEnabled);
            speedlinesCategoryBuilder.group(speedlinePresetsGroup.build());
            speedlinesCategoryBuilder.group(speedlinesGroup.build());
            speedlinesCategoryBuilder.group(rocketSpeedlinesGroup.build());



            var particlesCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.particles"))
                    .tooltip(Text.translatable("jetlag.category.particles.tooltip"));

            var windGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.wind.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.wind.group.tooltip"))
                            .build());

            var windLines = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.windlines"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.windlines.tooltip"))
                            .build())
                    .binding(
                            defaults.windLines,
                            () -> config.windLines,
                            val -> config.windLines = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var windLinesColor = Option.<Color>createBuilder()
                    .name(Text.translatable("jetlag.windlinescolor"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.windlinescolor.tooltip"))
                            .build())
                    .binding(
                            defaults.windLinesColor,
                            () -> config.windLinesColor,
                            val -> config.windLinesColor = val
                    )
                    .customController(opt -> new ColorController(opt, false))
                    .build();

            var firstPersonWindLines = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.firstpersonwindlines"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.firstpersonwindlines.tooltip"))
                            .build())
                    .binding(
                            defaults.windLinesInFirstPerson,
                            () -> config.windLinesInFirstPerson,
                            val -> config.windLinesInFirstPerson = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            var windGusts = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.windgusts"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.windgusts.tooltip"))
                            .build())
                    .binding(
                            defaults.windGusts,
                            () -> config.windGusts,
                            val -> config.windGusts = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var useMCWindGusts = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.mcwindgusts"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.mcwindgusts.tooltip"))
                            .build())
                    .binding(
                            defaults.useMinecraftWindGusts,
                            () -> config.useMinecraftWindGusts,
                            val -> config.useMinecraftWindGusts = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            var fireworkGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.firework.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.firework.group.tooltip"))
                            .build());

            var altFireworkParticles = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.altfirework"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.altfirework.tooltip"))
                            .build())
                    .binding(
                            defaults.altFireworkParticles,
                            () -> config.altFireworkParticles,
                            val -> config.altFireworkParticles = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var alwaysUseAltFireworkParticles = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.alwaysaltfirework"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.alwaysaltfirework.tooltip"))
                            .build())
                    .binding(
                            defaults.alwaysUseAltFireworkParticles,
                            () -> config.alwaysUseAltFireworkParticles,
                            val -> config.alwaysUseAltFireworkParticles = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            windGroup.option(windLines);
            windGroup.option(windLinesColor);
            windGroup.option(firstPersonWindLines);
            windGroup.option(LabelOption.create(Text.empty()));
            windGroup.option(windGusts);
            windGroup.option(useMCWindGusts);
            fireworkGroup.option(altFireworkParticles);
            fireworkGroup.option(alwaysUseAltFireworkParticles);

            particlesCategoryBuilder.group(windGroup.build());
            particlesCategoryBuilder.group(fireworkGroup.build());

            var screenEffectsCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.screen"))
                    .tooltip(Text.translatable("jetlag.category.screen.tooltip"));

            var oldSpeedlinesGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.oldspeedlines.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.oldspeedlines.group.tooltip"))
                            .build());

            var showSpeedlines = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.showspeedlines"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.showspeedlines.tooltip"))
                            .build())
                    .binding(
                            defaults.showSpeedlinesTexture,
                            () -> config.showSpeedlinesTexture,
                            val -> config.showSpeedlinesTexture = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var velBasedSpeedlinesOpacity = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.velspeedlinesopacity"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.velspeedlinesopacity.tooltip"))
                            .build())
                    .binding(
                            defaults.velocityBasedSpeedlinesOpacity,
                            () -> config.velocityBasedSpeedlinesOpacity,
                            val -> config.velocityBasedSpeedlinesOpacity = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var speedlinesColor = Option.<Color>createBuilder()
                    .name(Text.translatable("jetlag.speedlinescolor"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinescolor.tooltip"))
                            .build())
                    .binding(
                            defaults.speedlinesColor,
                            () -> config.speedlinesColor,
                            val -> config.speedlinesColor = val
                    )
                    .customController(opt -> new ColorController(opt, true))
                    .build();

            var rainbowSpeedlines = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.rainbowspeedlines"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.rainbowspeedlines.tooltip"))
                            .build())
                    .binding(
                            defaults.rainbowSpeedlines,
                            () -> config.rainbowSpeedlines,
                            val -> config.rainbowSpeedlines = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var onlyFirstPersonSpeedlines = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.firstpersonspeedlines"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.firstpersonspeedlines.tooltip"))
                            .build())
                    .binding(
                            defaults.onlyShowSpeedlinesInFirstPerson,
                            () -> config.onlyShowSpeedlinesInFirstPerson,
                            val -> config.onlyShowSpeedlinesInFirstPerson = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
                    .build();

            oldSpeedlinesGroup.option(showSpeedlines);
            oldSpeedlinesGroup.option(velBasedSpeedlinesOpacity);
            oldSpeedlinesGroup.option(speedlinesColor);
            oldSpeedlinesGroup.option(rainbowSpeedlines);
            oldSpeedlinesGroup.option(onlyFirstPersonSpeedlines);

            screenEffectsCategoryBuilder.group(oldSpeedlinesGroup.build());

            return builder
                .title(Text.translatable("jetlag.title"))
                .category(contrailCategoryBuilder.build())
                    .category(speedlinesCategoryBuilder.build())
                    .category(particlesCategoryBuilder.build())
                    .category(screenEffectsCategoryBuilder.build());
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

//    private static void buildSpeedlineConfigInstance(OptionGroup.Builder group, SpeedlineConfigInstance defaults, SpeedlineConfigInstance config, Formatting titleFormatting) {
//        var width = Option.<Float>createBuilder()
//            .name(Text.translatable("jetlag.speedlinewidth"))
//            .description(OptionDescription.createBuilder()
//                    .text(Text.translatable("jetlag.speedlinewidth.tooltip"))
//                    .build())
//            .binding(
//                    defaults.width,
//                    () -> config.width,
//                    val -> config.width = val
//            )
//            .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
//            .build();
//
//        var length = Option.<Float>createBuilder()
//                .name(Text.translatable("jetlag.speedlinelength"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinelength.tooltip"))
//                        .build())
//                .binding(
//                        defaults.length,
//                        () -> config.length,
//                        val -> config.length = val
//                )
//                .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
//                .build();
//
//        var spawnRadius = Option.<Float>createBuilder()
//                .name(Text.translatable("jetlag.speedlinespawnradius"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinespawnradius.tooltip"))
//                        .build())
//                .binding(
//                        defaults.spawnRadius,
//                        () -> config.spawnRadius,
//                        val -> config.spawnRadius = val
//                )
//                .customController(opt -> new FloatSliderController(opt, 0, 20f, 0.1f))
//                .build();
//
//        var velBasedSpawnRadius = Option.<Boolean>createBuilder()
//                .name(Text.translatable("jetlag.speedlinevelbasedspawnradius"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinevelbasedspawnradius.tooltip"))
//                        .build())
//                .binding(
//                        defaults.velBasedSpawnRadius,
//                        () -> config.velBasedSpawnRadius,
//                        val -> config.velBasedSpawnRadius = val
//                )
//                .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
//                .build();
//
//        var speed = Option.<Float>createBuilder()
//                .name(Text.translatable("jetlag.speedlinespeed"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinespeed.tooltip"))
//                        .build())
//                .binding(
//                        defaults.speed,
//                        () -> config.speed,
//                        val -> config.speed = val
//                )
//                .customController(opt -> new FloatSliderController(opt, 0, 2f, 0.05f))
//                .build();
//
//        var velMultiplier = Option.<Float>createBuilder()
//                .name(Text.translatable("jetlag.speedlinevelmultipler"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinevelmultipler.tooltip"))
//                        .build())
//                .binding(
//                        defaults.velMultiplier,
//                        () -> config.velMultiplier,
//                        val -> config.velMultiplier = val
//                )
//                .customController(opt -> new FloatSliderController(opt, 0, 2f, 0.05f))
//                .build();
//
//        var color = Option.<Color>createBuilder()
//                .name(Text.translatable("jetlag.speedlinecolor"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinecolor.tooltip"))
//                        .build())
//                .binding(
//                        defaults.color,
//                        () -> config.color,
//                        val -> config.color = val
//                )
//                .customController(opt -> new ColorController(opt, true))
//                .build();
//
//        var velBasedOpacity = Option.<Boolean>createBuilder()
//                .name(Text.translatable("jetlag.speedlinevelbasedopacity"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinevelbasedopacity.tooltip"))
//                        .build())
//                .binding(
//                        defaults.velBasedOpacity,
//                        () -> config.velBasedOpacity,
//                        val -> config.velBasedOpacity = val
//                )
//                .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
//                .build();
//
//        var fadeIn = Option.<Boolean>createBuilder()
//                .name(Text.translatable("jetlag.speedlinefadein"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinefadein.tooltip"))
//                        .build())
//                .binding(
//                        defaults.fadeIn,
//                        () -> config.fadeIn,
//                        val -> config.fadeIn = val
//                )
//                .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
//                .build();
//
//        var rainbowMode = Option.<Boolean>createBuilder()
//                .name(Text.translatable("jetlag.speedlinerainbowmode"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinerainbowmode.tooltip"))
//                        .build())
//                .binding(
//                        defaults.rainbowMode,
//                        () -> config.rainbowMode,
//                        val -> config.rainbowMode = val
//                )
//                .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
//                .build();
//
//        //pure magic this works but really happy with it
//        AtomicInteger pendingMaxAge = new AtomicInteger(config.maxAge);
//        AtomicInteger pendingMinAge = new AtomicInteger(config.minAge);
//
//        var maxAge = Option.<Integer>createBuilder()
//                .name(Text.translatable("jetlag.speedlinemaxage"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlinemaxage.tooltip"))
//                        .text(Text.translatable("jetlag.speedlineagenotice"))
//                        .build())
//                .binding(
//                        defaults.maxAge,
//                        () -> config.maxAge,
//                        val -> {
//                            config.maxAge = val;
//                            pendingMaxAge.set(config.maxAge);
//                        }
//                )
//                .customController(opt -> new IntegerSliderController(opt, 1, 100, 1))
//                .listener((integerOption, integer) -> {
//                    pendingMaxAge.set(integer);
//                    if(integer < pendingMinAge.get()) {
//                        integerOption.requestSet(pendingMinAge.get());
//                    }
//                })
//                .build();
//
//        var minAge = Option.<Integer>createBuilder()
//                .name(Text.translatable("jetlag.speedlineminage"))
//                .description(OptionDescription.createBuilder()
//                        .text(Text.translatable("jetlag.speedlineminage.tooltip"))
//                        .text(Text.translatable("jetlag.speedlineagenotice"))
//                        .build())
//                .binding(
//                        defaults.minAge,
//                        () -> config.minAge,
//                        val -> {
//                            config.minAge = val;
//                            pendingMinAge.set(config.minAge);
//                        }
//                )
//                .customController(opt -> new IntegerSliderController(opt, 1, 100, 1))
//                .listener((integerOption, integer) -> {
//                    pendingMinAge.set(integer);
//                    if(integer > pendingMaxAge.get()) {
//                        integerOption.requestSet(pendingMaxAge.get());
//                    }
//                })
//                .build();
//
//        group.option(LabelOption.create(Text.translatable("jetlag.speedlinescale").formatted(titleFormatting)));
//        group.option(width);
//        group.option(length);
//        group.option(LabelOption.create(Text.translatable("jetlag.speedlineplacementandvel").formatted(titleFormatting)));
//        group.option(spawnRadius);
//        group.option(velBasedSpawnRadius);
//        group.option(speed);
//        group.option(velMultiplier);
//        group.option(LabelOption.create(Text.translatable("jetlag.speedlinecolorandopacity").formatted(titleFormatting)));
//        group.option(color);
//        group.option(velBasedOpacity);
//        group.option(fadeIn);
//        group.option(rainbowMode);
//        group.option(LabelOption.create(Text.translatable("jetlag.speedlinelifetime").formatted(titleFormatting)));
//        group.option(maxAge);
//        group.option(minAge);
//    }

    private static class SpeedlineConfigOptionContainer {
        public OptionGroup.Builder group;
        public SpeedlineConfigInstance defaults;
        public SpeedlineConfigInstance config;
        public Formatting formatting;

        public Option<Float> width;
        public Option<Float> length;
        public Option<Float> spawnRadius;
        public Option<Float> randomSpawnRadius;
        public Option<Boolean> velBasedSpawnRadius;
        public Option<Float> speed;
        public Option<Float> velMultiplier;
        public Option<Color> color;
        public Option<Boolean> velBasedOpacity;
        public Option<Boolean> fadeIn;
        public Option<Boolean> rainbowMode;
        public Option<Integer> maxAge;
        public Option<Integer> minAge;

        public SpeedlineConfigOptionContainer(OptionGroup.Builder group, SpeedlineConfigInstance defaults, SpeedlineConfigInstance config, Formatting titleFormatting) {
            this.group = group;
            this.defaults = defaults;
            this.config = config;
            this.formatting = titleFormatting;
        }

        public void applySpeedlineConfig(SpeedlineConfigInstance instance) {
            this.width.requestSet(instance.width);
            this.length.requestSet(instance.length);
            this.spawnRadius.requestSet(instance.spawnRadius);
            this.randomSpawnRadius.requestSet(instance.randomSpawnRadius);
            this.velBasedSpawnRadius.requestSet(instance.velBasedSpawnRadius);
            this.speed.requestSet(instance.speed);
            this.velMultiplier.requestSet(instance.velMultiplier);
            this.color.requestSet(instance.color);
            this.velBasedOpacity.requestSet(instance.velBasedOpacity);
            this.fadeIn.requestSet(instance.fadeIn);
            this.rainbowMode.requestSet(instance.rainbowMode);
            this.maxAge.requestSet(instance.maxAge);
            this.minAge.requestSet(instance.minAge);
        }

        public void buildOptions() {
            width = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedlinewidth"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinewidth.tooltip"))
                            .build())
                    .binding(
                            defaults.width,
                            () -> config.width,
                            val -> config.width = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
                    .build();

            length = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedlinelength"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinelength.tooltip"))
                            .build())
                    .binding(
                            defaults.length,
                            () -> config.length,
                            val -> config.length = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
                    .build();

            spawnRadius = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedlinespawnradius"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinespawnradius.tooltip"))
                            .build())
                    .binding(
                            defaults.spawnRadius,
                            () -> config.spawnRadius,
                            val -> config.spawnRadius = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 30f, 0.1f))
                    .build();

            randomSpawnRadius = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedlinerandomspawnradius"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinerandomspawnradius.tooltip"))
                            .build())
                    .binding(
                            defaults.randomSpawnRadius,
                            () -> config.randomSpawnRadius,
                            val -> config.randomSpawnRadius = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
                    .build();

            velBasedSpawnRadius = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedlinevelbasedspawnradius"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinevelbasedspawnradius.tooltip"))
                            .build())
                    .binding(
                            defaults.velBasedSpawnRadius,
                            () -> config.velBasedSpawnRadius,
                            val -> config.velBasedSpawnRadius = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            speed = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedlinespeed"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinespeed.tooltip"))
                            .build())
                    .binding(
                            defaults.speed,
                            () -> config.speed,
                            val -> config.speed = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 2f, 0.05f))
                    .build();

            velMultiplier = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedlinevelmultipler"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinevelmultipler.tooltip"))
                            .build())
                    .binding(
                            defaults.velMultiplier,
                            () -> config.velMultiplier,
                            val -> config.velMultiplier = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 2f, 0.05f))
                    .build();

            color = Option.<Color>createBuilder()
                    .name(Text.translatable("jetlag.speedlinecolor"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinecolor.tooltip"))
                            .build())
                    .binding(
                            defaults.color,
                            () -> config.color,
                            val -> config.color = val
                    )
                    .customController(opt -> new ColorController(opt, true))
                    .build();

            velBasedOpacity = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedlinevelbasedopacity"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinevelbasedopacity.tooltip"))
                            .build())
                    .binding(
                            defaults.velBasedOpacity,
                            () -> config.velBasedOpacity,
                            val -> config.velBasedOpacity = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            fadeIn = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedlinefadein"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinefadein.tooltip"))
                            .build())
                    .binding(
                            defaults.fadeIn,
                            () -> config.fadeIn,
                            val -> config.fadeIn = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            rainbowMode = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedlinerainbowmode"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinerainbowmode.tooltip"))
                            .build())
                    .binding(
                            defaults.rainbowMode,
                            () -> config.rainbowMode,
                            val -> config.rainbowMode = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
                    .build();

            //pure magic this works but really happy with it
            AtomicInteger pendingMaxAge = new AtomicInteger(config.maxAge);
            AtomicInteger pendingMinAge = new AtomicInteger(config.minAge);

            maxAge = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedlinemaxage"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinemaxage.tooltip"))
                            .text(Text.translatable("jetlag.speedlineagenotice"))
                            .build())
                    .binding(
                            defaults.maxAge,
                            () -> config.maxAge,
                            val -> {
                                config.maxAge = val;
                                pendingMaxAge.set(config.maxAge);
                            }
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 100, 1))
                    .listener((integerOption, integer) -> {
                        pendingMaxAge.set(integer);
                        if(integer < pendingMinAge.get()) {
                            integerOption.requestSet(pendingMinAge.get());
                        }
                    })
                    .build();

            minAge = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedlineminage"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlineminage.tooltip"))
                            .text(Text.translatable("jetlag.speedlineagenotice"))
                            .build())
                    .binding(
                            defaults.minAge,
                            () -> config.minAge,
                            val -> {
                                config.minAge = val;
                                pendingMinAge.set(config.minAge);
                            }
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 100, 1))
                    .listener((integerOption, integer) -> {
                        pendingMinAge.set(integer);
                        if(integer > pendingMaxAge.get()) {
                            integerOption.requestSet(pendingMaxAge.get());
                        }
                    })
                    .build();

            this.group.option(LabelOption.create(Text.translatable("jetlag.speedlinescale").formatted(formatting)));
            this.group.option(width);
            this.group.option(length);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedlineplacementandvel").formatted(formatting)));
            this.group.option(spawnRadius);
            this.group.option(velBasedSpawnRadius);
            this.group.option(randomSpawnRadius);
            this.group.option(speed);
            this.group.option(velMultiplier);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedlinecolorandopacity").formatted(formatting)));
            this.group.option(color);
            this.group.option(velBasedOpacity);
            this.group.option(fadeIn);
            this.group.option(rainbowMode);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedlinelifetime").formatted(formatting)));
            this.group.option(maxAge);
            this.group.option(minAge);
        }
    }

}
