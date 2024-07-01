package net.superkat.jetlag.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.WindLineHandler;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.contrail.Contrail;
import net.superkat.jetlag.contrail.ContrailHandler;
import net.superkat.jetlag.contrail.JetLagPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements JetLagPlayer {
    public List<Contrail> contrails = Lists.newArrayList();
    @Nullable
    public Contrail currentContrail = null;
    @Unique
    public int pointTicks = 0;
    @Unique
    public int windLineTicks = 20;

    @Override
    public List<Contrail> jetlag$getContrails() {
        return contrails;
    }

    @Override
    public Contrail jetlag$getCurrentContrail() {
        return currentContrail;
    }

    @Override
    public void jetlag$createContrail() {
        if(!JetLagConfig.getInstance().contrailsEnabled) return;
        currentContrail = new Contrail((ClientPlayerEntity) (Object) this);
        contrails.add(currentContrail);
    }

    @Override
    public void jetlag$removeAllContrails() {
        contrails = Lists.newArrayList();
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void jetlag$tick(CallbackInfo ci) {
        //bring code handling outside the mixin
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        ContrailHandler.tickJetlagPlayer(self);
        WindLineHandler.tickJetlagPlayer(self);
    }

    @Override
    public void jetlag$endCurrentContrail() {
        if(this.currentContrail != null) {
            this.currentContrail.startDeletingPoints();
            this.currentContrail = null;
        }
    }

    @Override
    public boolean jetlag$hasContrails() {
        return !contrails.isEmpty();
    }

    @Override
    public int jetlag$pointTicks() {
        return pointTicks;
    }

    @Override
    public void jetlag$setPointTicks(int ticks) {
        this.pointTicks = ticks;
    }

    @Override
    public int jetlag$windLineTicks() {
        return windLineTicks;
    }

    @Override
    public void jetlag$setWindLineTicks(int ticks) {
        this.windLineTicks = ticks;
    }
}
