package net.superkat.jetlag.config;

import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.tab.TabExt;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TestTab implements TabExt {
    private final YACLScreen screen;
    private final ScreenRect tabArea;
    private final ContrailConfigWidget contrailConfigWidget;

    public TestTab(YACLScreen screen, ScreenRect tabArea) {
        this.screen = screen;
        this.tabArea = tabArea;

        contrailConfigWidget = new ContrailConfigWidget(tabArea.getLeft(), tabArea.getRight(), tabArea.width(), tabArea.height(), Text.of("test"));
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
        consumer.accept(contrailConfigWidget);
    }

    @Override
    public void refreshGrid(ScreenRect tabArea) {

    }

    @Override
    public void renderBackground(DrawContext graphics) {

    }
}
