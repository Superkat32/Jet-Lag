package net.superkat.jetlag.contrail;

import net.minecraft.util.math.random.Random;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class GuiContrail {
    public List<Contrail.ContrailPos> contrailPoints = Lists.newArrayList();
    public List<Float> contrailOpacityAdjustments = Lists.newArrayList();
    public int maxPoints;
    public boolean startDeletingPoints = false;
    public int ticksUntilNextDelete = 10;
    public final Random random = Random.create();
    public GuiContrail() {
        super();
    }
}
