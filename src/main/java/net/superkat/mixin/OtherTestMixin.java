package net.superkat.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.superkat.JetLagMain.LOGGER;

@Environment(EnvType.CLIENT)
@Mixin(FireworkRocketItem.class)
public abstract class OtherTestMixin {

    @Shadow public abstract TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);

    @Inject(method = "use", at = @At("TAIL"))
    public void init(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        LOGGER.info("OtherTestMixin activated!");
    }
}
