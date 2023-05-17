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

    @ConfigEntry public static boolean myBoolean = true;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> {
            var defaultCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("jetlag.category.default"));

            var testGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("jetlag.test.group"))
                    .tooltip(Text.translatable("jetlag.test.group.tooltip"));
            var myBoolean = Option.createBuilder(boolean.class)
                    .name(Text.translatable("jetlag.myboolean"))
                    .tooltip(Text.translatable("jetlag.myboolean.tooltip"))
                    .binding(
                            defaults.myBoolean,
                            () -> config.myBoolean,
                            val -> config.myBoolean = val
                    )
                    .controller(booleanOption -> new BooleanController(booleanOption, true))
                    .build();
            testGroup.option(myBoolean);
            defaultCategoryBuilder.group(testGroup.build());

            return builder
                    .title(Text.translatable("jetlag.title"))
                    .category(defaultCategoryBuilder.build());
        }).generateScreen(parent);
    }

}
