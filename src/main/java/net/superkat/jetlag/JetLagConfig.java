package net.superkat.jetlag;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.ConfigInstance;
import dev.isxander.yacl.config.GsonConfigInstance;
import dev.isxander.yacl.gui.controllers.BooleanController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Path;

public class JetLagConfig {

    public static final ConfigInstance<JetLagConfig> INSTANCE = new GsonConfigInstance<>(JetLagConfig.class, Path.of("./config/jetlag.json"));

    @ConfigEntry public static boolean showSpeedlines = true;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> {
            var defaultCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.default"));

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
            screenEffectsGroup.option(showSpeedlines);
            defaultCategoryBuilder.group(screenEffectsGroup.build());

            return builder
                    .title(Text.translatable("jetlag.title"))
                    .category(defaultCategoryBuilder.build());
        }).generateScreen(parent);
    }

}
