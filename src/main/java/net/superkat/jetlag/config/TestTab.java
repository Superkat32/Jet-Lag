package net.superkat.jetlag.config;

import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.tab.TabExt;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TestTab implements TabExt {
    private final YACLScreen screen;

    public TestTab(YACLScreen screen, ScreenRect tabArea) {
        this.screen = screen;


    }

    @Override
    public @Nullable Tooltip getTooltip() {
        return null;
    }

    @Override
    public Text getTitle() {
        return Text.of("yes?");
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    @Override
    public void refreshGrid(ScreenRect tabArea) {

    }
}
