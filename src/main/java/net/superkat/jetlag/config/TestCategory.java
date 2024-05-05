package net.superkat.jetlag.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.PlaceholderCategory;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface TestCategory extends ConfigCategory {
    static Builder createBuilder() {
        return new TestCategoryImpl.BuilderImpl();
    }

    interface Builder {
        /**
         * Sets name of the category
         *
         * @see ConfigCategory#name()
         */
        Builder name(@NotNull Text name);

        /**
         * Sets the tooltip to be used by the category.
         * Can be invoked twice to append more lines.
         * No need to wrap the Component yourself, the gui does this itself.
         *
         * @param tooltips Component lines - merged with a new-line on {@link dev.isxander.yacl3.api.PlaceholderCategory.Builder#build()}.
         */
        Builder tooltip(@NotNull Text... tooltips);

        /**
         * Screen to open upon selecting this category
         *
         * @see PlaceholderCategory#screen()
         */
        Builder screen(@NotNull BiFunction<MinecraftClient, YACLScreen, Screen> screenFunction);

        TestCategory build();
    }
}
