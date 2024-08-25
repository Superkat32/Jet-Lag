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
    @SerialEntry public boolean mirrorContrailOffset = true;
    @SerialEntry public double contrailLeftOffsetWidth = 1.45;
    @SerialEntry public double contrailLeftOffsetHeight = 0.65;
    @SerialEntry public double contrailLeftOffsetLength = 0.45;
    @SerialEntry public double contrailRightOffsetWidth = 1.45;
    @SerialEntry public double contrailRightOffsetHeight = 0.65;
    @SerialEntry public double contrailRightOffsetLength = 0.45;
    @SerialEntry public int ticksPerPoint = 1;
    @SerialEntry public int contrailDeletionDelay = 1;
    @SerialEntry public int pointsDeletedPerDelay = 1;

    //speedlines
    @SerialEntry public boolean speedlinesEnabled = true;
    @SerialEntry public SpeedlineConfigInstance speedlineConfig = new SpeedlineConfigInstance();
    @SerialEntry public boolean rocketSpeedlinesEnabled = true;
    @SerialEntry public SpeedlineConfigInstance rocketConfig = new SpeedlineConfigInstance().rocketMode();
    @SerialEntry public boolean riptideMakesRocket = true;
    @SerialEntry public boolean thirdPersonSpeedlines = false;
    @SerialEntry public boolean hideSpeedlinesInF1 = true;
    @SerialEntry public boolean speedlinesIgnoreFov = true;

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
                            .text(Text.of(""))
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
                            .text(Text.of(""))
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

            var positionGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.position.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.position.group.tooltip"))
                            .build());

            var leftOffsetWidth = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.widthoffset"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.widthoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailLeftOffsetWidth,
                            () -> config.contrailLeftOffsetWidth,
                            val -> config.contrailLeftOffsetWidth = val
                    )
                    .customController(opt -> new DoubleSliderController(opt, 0, 2, 0.01))
                    .build();

            var leftOffsetHeight = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.heightoffset"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.heightoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailLeftOffsetHeight,
                            () -> config.contrailLeftOffsetHeight,
                            val -> config.contrailLeftOffsetHeight = val
                    )
                    .customController(opt -> new DoubleSliderController(opt, 0, 2, 0.01))
                    .build();

            var leftOffsetLength = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.lengthoffset"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.lengthoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailLeftOffsetLength,
                            () -> config.contrailLeftOffsetLength,
                            val -> config.contrailLeftOffsetLength = val
                    )
                    .customController(opt -> new DoubleSliderController(opt, 0, 2, 0.01))
                    .build();

            var rightOffsetWidth = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.widthoffset")
                            .append(Text.translatable("jetlag.offsetright"))
                    )
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.widthoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailRightOffsetWidth,
                            () -> config.contrailRightOffsetWidth,
                            val -> config.contrailRightOffsetWidth = val
                    )
                    .available(!config.mirrorContrailOffset)
                    .customController(opt -> new DoubleSliderController(opt, 0, 2, 0.01))
                    .build();

            var rightOffsetHeight = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.heightoffset")
                            .append(Text.translatable("jetlag.offsetright"))
                    )
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.heightoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailRightOffsetHeight,
                            () -> config.contrailRightOffsetHeight,
                            val -> config.contrailRightOffsetHeight = val
                    )
                    .available(!config.mirrorContrailOffset)
                    .customController(opt -> new DoubleSliderController(opt, 0, 2, 0.01))
                    .build();

            var rightOffsetLength = Option.<Double>createBuilder()
                    .name(Text.translatable("jetlag.lengthoffset")
                            .append(Text.translatable("jetlag.offsetright"))
                    )
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.lengthoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.contrailRightOffsetLength,
                            () -> config.contrailRightOffsetLength,
                            val -> config.contrailRightOffsetLength = val
                    )
                    .available(!config.mirrorContrailOffset)
                    .customController(opt -> new DoubleSliderController(opt, 0, 2, 0.01))
                    .build();

            var mirrorContrailOffset = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.mirrorcontrailoffset"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.mirrorcontrailoffset.tooltip"))
                            .build())
                    .binding(
                            defaults.mirrorContrailOffset,
                            () -> config.mirrorContrailOffset,
                            val -> config.mirrorContrailOffset = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
                    .listener((opt, value) -> {
                        rightOffsetWidth.setAvailable(!value);
                        rightOffsetLength.setAvailable(!value);
                        rightOffsetHeight.setAvailable(!value);
                    })
                    .build();

            positionGroup.option(mirrorContrailOffset);
            positionGroup.option(leftOffsetWidth);
            positionGroup.option(leftOffsetHeight);
            positionGroup.option(leftOffsetLength);
            positionGroup.option(rightOffsetWidth);
            positionGroup.option(rightOffsetHeight);
            positionGroup.option(rightOffsetLength);

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
            contrailCategoryBuilder.group(positionGroup.build());
            contrailCategoryBuilder.group(spawningAndDeletingGroup.build());

            var speedlinesCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.speedlines"))
                    .tooltip(Text.translatable("jetlag.category.speedlines.tooltip"));

            var speedlinesGeneralGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.speedlinesgeneral.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinesgeneral.group.tooltip"))
                            .build());

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

            var riptideEnablesRocket = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.riptidemakesrocket"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.riptidemakesrocket.tooltip"))
                            .build())
                    .binding(
                            defaults.riptideMakesRocket,
                            () -> config.riptideMakesRocket,
                            val -> config.riptideMakesRocket = val
                    )
                    .available(config.rocketSpeedlinesEnabled)
                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
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
                    .listener((opt, value) -> {
                        riptideEnablesRocket.setAvailable(value);
                    })
                    .build();

            var thirdPersonSpeedlines = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.thirdpersonspeedlines"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.thirdpersonspeedlines.tooltip"))
                            .build())
                    .binding(
                            defaults.thirdPersonSpeedlines,
                            () -> config.thirdPersonSpeedlines,
                            val -> config.thirdPersonSpeedlines = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var hideSpeedlinesInF1 = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.hidespeedlinesfone"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.hidespeedlinesfone.tooltip"))
                            .build())
                    .binding(
                            defaults.hideSpeedlinesInF1,
                            () -> config.hideSpeedlinesInF1,
                            val -> config.hideSpeedlinesInF1 = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
                    .build();

            var speedlinesIgnoreFov = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedlinesignorefov"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedlinesignorefov.tooltip"))
                            .build())
                    .binding(
                            defaults.speedlinesIgnoreFov,
                            () -> config.speedlinesIgnoreFov,
                            val -> config.speedlinesIgnoreFov = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            speedlinesGeneralGroup.option(speedlinesEnabled);
            speedlinesGeneralGroup.option(rocketSpeedlinesEnabled);
            speedlinesGeneralGroup.option(riptideEnablesRocket);
            speedlinesGeneralGroup.option(LabelOption.create(Text.of("")));
            speedlinesGeneralGroup.option(thirdPersonSpeedlines);
            speedlinesGeneralGroup.option(hideSpeedlinesInF1);
            speedlinesGeneralGroup.option(speedlinesIgnoreFov);

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
            rocketSpeedlineOptions.setAvailable(config.rocketSpeedlinesEnabled);

            rocketSpeedlinesEnabled.addListener((opt, value) -> {
                rocketSpeedlineOptions.setAvailable(value);
            });

            var subtlePreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedline.preset.subtle"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().subtleAndSweet());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_subtleAndSweet());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                Text.translatable("jetlag.title"),
                                Text.translatable("jetlag.speedline.preset.previewing").append(buttonOption.name())
                            )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedline.preset.preview"))
                    .build();

            var boldPreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedline.preset.bold"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().niceAndNoticeable());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_niceAndNoticeable());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                        Text.translatable("jetlag.title"),
                                        Text.translatable("jetlag.speedline.preset.previewing").append(buttonOption.name())
                                )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedline.preset.preview"))
                    .build();

            var animePreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedline.preset.anime"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().animeStyled());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_animeStyled());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                        Text.translatable("jetlag.title"),
                                        Text.translatable("jetlag.speedline.preset.previewing").append(buttonOption.name())
                                )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedline.preset.preview"))
                    .build();

            var hyperdrivePreset = ButtonOption.createBuilder()
                    .name(Text.translatable("jetlag.speedline.preset.hyperdrive"))
                    .action(((yaclScreen, buttonOption) -> {
                        speedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().HYPERDRIVE());
                        rocketSpeedlineOptions.applySpeedlineConfig(new SpeedlineConfigInstance().rocket_HYPERDRIVE());
                        MinecraftClient.getInstance().getToastManager().add(
                                new SystemToast(new SystemToast.Type(3500),
                                        Text.translatable("jetlag.title"),
                                        Text.translatable("jetlag.speedline.preset.previewing").append(buttonOption.name())
                                )
                        );
                    }))
                    .text(Text.translatable("jetlag.speedline.preset.preview"))
                    .build();

            speedlinePresetsGroup.option(subtlePreset);
            speedlinePresetsGroup.option(boldPreset);
            speedlinePresetsGroup.option(animePreset);
            speedlinePresetsGroup.option(hyperdrivePreset);

            speedlinesCategoryBuilder.group(speedlinesGeneralGroup.build());
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
                    .available(config.windLines)
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
                    .available(config.windLines)
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            windLines.addListener((opt, value) -> {
                windLinesColor.setAvailable(value);
                firstPersonWindLines.setAvailable(value);
            });

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
                    .available(config.windGusts)
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
                    .listener((opt, value) -> useMCWindGusts.setAvailable(value))
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            var fireworkGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.firework.group"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.firework.group.tooltip"))
                            .build());

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
                    .available(config.altFireworkParticles)
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

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
                    .listener((opt, value) -> alwaysUseAltFireworkParticles.setAvailable(value))
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
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

//            var screenEffectsCategoryBuilder = ConfigCategory.createBuilder()
//                    .name(Text.translatable("jetlag.category.screen"))
//                    .tooltip(Text.translatable("jetlag.category.screen.tooltip"));
//
//            var oldSpeedlinesGroup = OptionGroup.createBuilder()
//                    .name(Text.translatable("jetlag.oldspeedlines.group"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.oldspeedlines.group.tooltip"))
//                            .build());
//
//            var showSpeedlines = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.showspeedlines"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.showspeedlines.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.showSpeedlinesTexture,
//                            () -> config.showSpeedlinesTexture,
//                            val -> config.showSpeedlinesTexture = val
//                    )
//                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
//                    .build();
//
//            var velBasedSpeedlinesOpacity = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.velspeedlinesopacity"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.velspeedlinesopacity.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.velocityBasedSpeedlinesOpacity,
//                            () -> config.velocityBasedSpeedlinesOpacity,
//                            val -> config.velocityBasedSpeedlinesOpacity = val
//                    )
//                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
//                    .build();
//
//            var speedlinesColor = Option.<Color>createBuilder()
//                    .name(Text.translatable("jetlag.speedlinescolor"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.speedlinescolor.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.speedlinesColor,
//                            () -> config.speedlinesColor,
//                            val -> config.speedlinesColor = val
//                    )
//                    .customController(opt -> new ColorController(opt, true))
//                    .build();
//
//            var rainbowSpeedlines = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.rainbowspeedlines"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.rainbowspeedlines.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.rainbowSpeedlines,
//                            () -> config.rainbowSpeedlines,
//                            val -> config.rainbowSpeedlines = val
//                    )
//                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
//                    .build();
//
//            var onlyFirstPersonSpeedlines = Option.<Boolean>createBuilder()
//                    .name(Text.translatable("jetlag.firstpersonspeedlines"))
//                    .description(OptionDescription.createBuilder()
//                            .text(Text.translatable("jetlag.firstpersonspeedlines.tooltip"))
//                            .build())
//                    .binding(
//                            defaults.onlyShowSpeedlinesInFirstPerson,
//                            () -> config.onlyShowSpeedlinesInFirstPerson,
//                            val -> config.onlyShowSpeedlinesInFirstPerson = val
//                    )
//                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
//                    .build();
//
//            oldSpeedlinesGroup.option(showSpeedlines);
//            oldSpeedlinesGroup.option(velBasedSpeedlinesOpacity);
//            oldSpeedlinesGroup.option(speedlinesColor);
//            oldSpeedlinesGroup.option(rainbowSpeedlines);
//            oldSpeedlinesGroup.option(onlyFirstPersonSpeedlines);
//
//            screenEffectsCategoryBuilder.group(oldSpeedlinesGroup.build());

            return builder
                .title(Text.translatable("jetlag.title"))
                .category(contrailCategoryBuilder.build())
                    .category(speedlinesCategoryBuilder.build())
                    .category(particlesCategoryBuilder.build());
//                    .category(screenEffectsCategoryBuilder.build());
        }).generateScreen(parent);
    }

    private static class SpeedlineConfigOptionContainer {
        public OptionGroup.Builder group;
        public SpeedlineConfigInstance defaults;
        public SpeedlineConfigInstance config;
        public Formatting formatting;

        public Option<Integer> maxSpawnTicks;
        public Option<Integer> minSpawnTicks;
        public Option<Integer> maxSpawnCount;
        public Option<Integer> minSpawnCount;
        public Option<Boolean> velBasedSpawnCount;
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
        public Option<Boolean> moveFromTurn;
        public Option<Float> moveMultiplier;
        public Option<Float> maxMoveAmountX;
        public Option<Float> maxMoveAmountY;
        public Option<Integer> maxAge;
        public Option<Integer> minAge;

        public SpeedlineConfigOptionContainer(OptionGroup.Builder group, SpeedlineConfigInstance defaults, SpeedlineConfigInstance config, Formatting titleFormatting) {
            this.group = group;
            this.defaults = defaults;
            this.config = config;
            this.formatting = titleFormatting;
        }

        public void setAvailable(boolean available) {
            this.maxSpawnTicks.setAvailable(available);
            this.minSpawnTicks.setAvailable(available);
            this.maxSpawnCount.setAvailable(available);
            this.minSpawnCount.setAvailable(available);
            this.velBasedSpawnCount.setAvailable(available);
            this.width.setAvailable(available);
            this.length.setAvailable(available);
            this.spawnRadius.setAvailable(available);
            this.randomSpawnRadius.setAvailable(available);
            this.velBasedSpawnRadius.setAvailable(available);
            this.speed.setAvailable(available);
            this.velMultiplier.setAvailable(available);
            this.color.setAvailable(available);
            this.velBasedOpacity.setAvailable(available);
            this.fadeIn.setAvailable(available);
            this.rainbowMode.setAvailable(available);
            this.moveFromTurn.setAvailable(available);
            this.moveMultiplier.setAvailable(available);
            this.maxMoveAmountX.setAvailable(available);
            this.maxMoveAmountY.setAvailable(available);
            this.maxAge.setAvailable(available);
            this.minAge.setAvailable(available);
        }

        public void applySpeedlineConfig(SpeedlineConfigInstance instance) {
            this.applySpeedlineConfig(instance, true);
        }

        public void applySpeedlineConfig(SpeedlineConfigInstance instance, boolean repeat) {
            this.maxSpawnTicks.requestSet(instance.maxSpawnTicks);
            this.minSpawnTicks.requestSet(instance.minSpawnTicks);
            this.maxSpawnCount.requestSet(instance.maxSpawnAmount);
            this.minSpawnCount.requestSet(instance.minSpawnAmount);
            this.velBasedSpawnCount.requestSet(instance.velBasedSpawnAmount);
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
            this.moveFromTurn.requestSet(instance.moveFromTurn);
            this.moveMultiplier.requestSet(instance.turnMoveMultiplier);
            this.maxMoveAmountX.requestSet(instance.maxMoveAmountX);
            this.maxMoveAmountY.requestSet(instance.maxMoveAmountY);
            this.maxAge.requestSet(instance.maxAge);
            this.minAge.requestSet(instance.minAge);

            if(repeat) {
                //repeats to ensure options with min/max will be properly set
                this.applySpeedlineConfig(instance, false);
            }
        }

        public void buildOptions() {
            AtomicInteger pendingMaxSpawnTicks = new AtomicInteger(config.maxSpawnTicks);
            AtomicInteger pendingMinSpawnTicks = new AtomicInteger(config.minSpawnTicks);

            maxSpawnTicks = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedline.maxspawnticks"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.maxspawnticks.tooltip"))
                            .build())
                    .binding(
                            defaults.maxSpawnTicks,
                            () -> config.maxSpawnTicks,
                            val -> {
                                config.maxSpawnTicks = val;
                                pendingMaxSpawnTicks.set(config.maxSpawnTicks);
                            }
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 40, 1))
                    .listener((integerOption, integer) -> {
                        pendingMaxSpawnTicks.set(integer);
                        if(integer < pendingMinSpawnTicks.get()) {
                            integerOption.requestSet(pendingMinSpawnTicks.get());
                        }
                    })
                    .build();

            minSpawnTicks = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedline.minspawnticks"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.minspawnticks.tooltip"))
                            .build())
                    .binding(
                            defaults.minSpawnTicks,
                            () -> config.minSpawnTicks,
                            val -> {
                                config.minSpawnTicks = val;
                                pendingMinSpawnTicks.set(config.minSpawnTicks);
                            }
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 40, 1))
                    .listener((integerOption, integer) -> {
                        pendingMinSpawnTicks.set(integer);
                        if(integer > pendingMaxSpawnTicks.get()) {
                            integerOption.requestSet(pendingMaxSpawnTicks.get());
                        }
                    })
                    .build();

            AtomicInteger pendingMaxSpawnCount = new AtomicInteger(config.maxSpawnAmount);
            AtomicInteger pendingMinSpawnCount = new AtomicInteger(config.minSpawnAmount);

            maxSpawnCount = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedline.maxspawncount"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.maxspawncount.tooltip"))
                            .build())
                    .binding(
                            defaults.maxSpawnAmount,
                            () -> config.maxSpawnAmount,
                            val -> {
                                config.maxSpawnAmount = val;
                                pendingMaxSpawnCount.set(config.maxSpawnAmount);
                            }
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 20, 1))
                    .listener((integerOption, integer) -> {
                        pendingMaxSpawnCount.set(integer);
                        if(integer < pendingMinSpawnCount.get()) {
                            integerOption.requestSet(pendingMinSpawnCount.get());
                        }
                    })
                    .build();

            minSpawnCount = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedline.minspawncount"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.minspawncount.tooltip"))
                            .build())
                    .binding(
                            defaults.minSpawnAmount,
                            () -> config.minSpawnAmount,
                            val -> {
                                config.minSpawnAmount = val;
                                pendingMinSpawnCount.set(config.minSpawnAmount);
                            }
                    )
                    .customController(opt -> new IntegerSliderController(opt, 1, 20, 1))
                    .listener((integerOption, integer) -> {
                        pendingMinSpawnCount.set(integer);
                        if(integer > pendingMaxSpawnCount.get()) {
                            integerOption.requestSet(pendingMaxSpawnCount.get());
                        }
                    })
                    .build();

            velBasedSpawnCount = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedline.velbasedspawncount"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.velbasedspawncount.tooltip"))
                            .build())
                    .binding(
                            defaults.velBasedSpawnAmount,
                            () -> config.velBasedSpawnAmount,
                            val -> config.velBasedSpawnAmount = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            width = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.width"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.width.tooltip"))
                            .build())
                    .binding(
                            defaults.width,
                            () -> config.width,
                            val -> config.width = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
                    .build();

            length = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.length"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.length.tooltip"))
                            .build())
                    .binding(
                            defaults.length,
                            () -> config.length,
                            val -> config.length = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
                    .build();

            spawnRadius = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.spawnradius"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.spawnradius.tooltip"))
                            .build())
                    .binding(
                            defaults.spawnRadius,
                            () -> config.spawnRadius,
                            val -> config.spawnRadius = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 30f, 0.1f))
                    .build();

            randomSpawnRadius = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.randomspawnradius"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.randomspawnradius.tooltip"))
                            .build())
                    .binding(
                            defaults.randomSpawnRadius,
                            () -> config.randomSpawnRadius,
                            val -> config.randomSpawnRadius = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 5f, 0.1f))
                    .build();

            velBasedSpawnRadius = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedline.velbasedspawnradius"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.velbasedspawnradius.tooltip"))
                            .build())
                    .binding(
                            defaults.velBasedSpawnRadius,
                            () -> config.velBasedSpawnRadius,
                            val -> config.velBasedSpawnRadius = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            speed = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.speed"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.speed.tooltip"))
                            .build())
                    .binding(
                            defaults.speed,
                            () -> config.speed,
                            val -> config.speed = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 2f, 0.05f))
                    .build();

            velMultiplier = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.velmultipler"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.velmultipler.tooltip"))
                            .build())
                    .binding(
                            defaults.velMultiplier,
                            () -> config.velMultiplier,
                            val -> config.velMultiplier = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 2f, 0.05f))
                    .build();

            color = Option.<Color>createBuilder()
                    .name(Text.translatable("jetlag.speedline.color"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.color.tooltip"))
                            .build())
                    .binding(
                            defaults.color,
                            () -> config.color,
                            val -> config.color = val
                    )
                    .customController(opt -> new ColorController(opt, true))
                    .build();

            velBasedOpacity = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedline.velbasedopacity"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.velbasedopacity.tooltip"))
                            .build())
                    .binding(
                            defaults.velBasedOpacity,
                            () -> config.velBasedOpacity,
                            val -> config.velBasedOpacity = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            fadeIn = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedline.fadein"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.fadein.tooltip"))
                            .build())
                    .binding(
                            defaults.fadeIn,
                            () -> config.fadeIn,
                            val -> config.fadeIn = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.YES_NO_FORMATTER, true))
                    .build();

            rainbowMode = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedline.rainbowmode"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.rainbowmode.tooltip"))
                            .build())
                    .binding(
                            defaults.rainbowMode,
                            () -> config.rainbowMode,
                            val -> config.rainbowMode = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.TRUE_FALSE_FORMATTER, true))
                    .build();

            moveFromTurn = Option.<Boolean>createBuilder()
                    .name(Text.translatable("jetlag.speedline.movefromturn"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.movefromturn.tooltip"))
                            .build())
                    .binding(
                            defaults.moveFromTurn,
                            () -> config.moveFromTurn,
                            val -> config.moveFromTurn = val
                    )
                    .customController(opt -> new BooleanController(opt, BooleanController.ON_OFF_FORMATTER, true))
                    .build();

            moveMultiplier = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.movemultiplier"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.movemultiplier.tooltip"))
                            .build())
                    .binding(
                            defaults.turnMoveMultiplier,
                            () -> config.turnMoveMultiplier,
                            val -> config.turnMoveMultiplier = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 20f, 0.05f))
                    .build();

            maxMoveAmountX = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.maxmoveamountx"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.maxmoveamountx.tooltip"), Text.of(""), Text.translatable("jetlag.speedline.maxmovenote"))
                            .build())
                    .binding(
                            defaults.maxMoveAmountX,
                            () -> config.maxMoveAmountX,
                            val -> config.maxMoveAmountX = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 100, 0.5f))
                    .build();

            maxMoveAmountY = Option.<Float>createBuilder()
                    .name(Text.translatable("jetlag.speedline.maxmoveamounty"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.maxmoveamounty.tooltip"), Text.of(""), Text.translatable("jetlag.speedline.maxmovenote"))
                            .build())
                    .binding(
                            defaults.maxMoveAmountY,
                            () -> config.maxMoveAmountY,
                            val -> config.maxMoveAmountY = val
                    )
                    .customController(opt -> new FloatSliderController(opt, 0, 100, 0.5f))
                    .build();

            //pure magic this works but really happy with it
            AtomicInteger pendingMaxAge = new AtomicInteger(config.maxAge);
            AtomicInteger pendingMinAge = new AtomicInteger(config.minAge);

            maxAge = Option.<Integer>createBuilder()
                    .name(Text.translatable("jetlag.speedline.maxage"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.maxage.tooltip"))
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
                    .name(Text.translatable("jetlag.speedline.minage"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("jetlag.speedline.minage.tooltip"))
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

            this.group.option(LabelOption.create(Text.translatable("jetlag.speedline.spawning").formatted(formatting)));
            this.group.option(maxSpawnTicks);
            this.group.option(minSpawnTicks);
            this.group.option(maxSpawnCount);
            this.group.option(minSpawnCount);
            this.group.option(velBasedSpawnCount);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedline.scale").formatted(formatting)));
            this.group.option(width);
            this.group.option(length);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedline.placementandvel").formatted(formatting)));
            this.group.option(spawnRadius);
            this.group.option(velBasedSpawnRadius);
            this.group.option(randomSpawnRadius);
            this.group.option(speed);
            this.group.option(velMultiplier);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedline.colorandopacity").formatted(formatting)));
            this.group.option(color);
            this.group.option(velBasedOpacity);
            this.group.option(fadeIn);
            this.group.option(rainbowMode);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedline.cameramovement").formatted(formatting)));
            this.group.option(moveFromTurn);
            this.group.option(moveMultiplier);
            this.group.option(maxMoveAmountX);
            this.group.option(maxMoveAmountY);
            this.group.option(LabelOption.create(Text.translatable("jetlag.speedline.lifetime").formatted(formatting)));
            this.group.option(maxAge);
            this.group.option(minAge);
        }
    }

}
