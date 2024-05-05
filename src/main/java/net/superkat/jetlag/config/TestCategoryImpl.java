package net.superkat.jetlag.config;

import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.CustomTabProvider;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class TestCategoryImpl implements TestCategory, CustomTabProvider {
    private final Text name;
    private final BiFunction<MinecraftClient, YACLScreen, Screen> screen;
    private final Text tooltip;
    public TestCategoryImpl(Text name, BiFunction<MinecraftClient, YACLScreen, Screen> screen, Text tooltip) {
        this.name = name;
        this.screen = screen;
        this.tooltip = tooltip;
    }

    @Override
    public @NotNull Text name() {
        return Text.of("plswork");
    }

    @Override
    public @NotNull ImmutableList<OptionGroup> groups() {
        return ImmutableList.of();
    }

    @Override
    public @NotNull Text tooltip() {
        return Text.of("pls");
    }

    @Override
    public Tab createTab(YACLScreen screen, ScreenRect tabArea) {
        return new TestTab(screen, tabArea);
    }

    @ApiStatus.Internal
    public static final class BuilderImpl implements Builder {
        private Text name;

        private final List<Text> tooltipLines = new ArrayList<>();

        private BiFunction<MinecraftClient, YACLScreen, Screen> screenFunction;

        @Override
        public Builder name(@NotNull Text name) {
            Validate.notNull(name, "`name` cannot be null");

            this.name = name;
            return this;
        }

        @Override
        public Builder tooltip(@NotNull Text... tooltips) {
            Validate.notEmpty(tooltips, "`tooltips` cannot be empty");

            tooltipLines.addAll(List.of(tooltips));
            return this;
        }

        @Override
        public Builder screen(@NotNull BiFunction<MinecraftClient, YACLScreen, Screen> screenFunction) {
            Validate.notNull(screenFunction, "`screenFunction` cannot be null");

            this.screenFunction = screenFunction;
            return this;
        }

        @Override
        public TestCategory build() {
            Validate.notNull(name, "`name` must not be null to build `ConfigCategory`");

            MutableText concatenatedTooltip = Text.empty();
            boolean first = true;
            for (Text line : tooltipLines) {
                if (!first) concatenatedTooltip.append("\n");
                first = false;

                concatenatedTooltip.append(line);
            }

            return new TestCategoryImpl(name, screenFunction, concatenatedTooltip);
        }
    }
}
