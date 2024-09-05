package net.superkat.jetlag.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
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

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin implements JetLagPlayer {
    @Nullable @Unique
    public Contrail currentContrail = null;
    @Unique public int pointTicks = 0;
    @Unique public int windLineTicks = 20;
    @Unique public boolean rocketBoosted = false;
    @Unique public float fakeElytraRoll = 0f;

    @Override
    public boolean jetlag$rocketBoosting() {
        return rocketBoosted;
    }

    @Override
    public void jetlag$setRocketBoosting(boolean boosting) {
        this.rocketBoosted = boosting;
    }

    @Override
    public float jetlag$playerFakeElytraRoll() {
        return fakeElytraRoll;
    }

    @Override
    public void jetlag$setPlayerFakeElytraRoll(float roll) {
        this.fakeElytraRoll = roll;
    }

    @Override
    public List<Contrail> jetlag$getContrails() {
        return ContrailHandler.getPlayerContrail((AbstractClientPlayerEntity) (Object) this);
    }

    @Override
    public Contrail jetlag$getCurrentContrail() {
        return currentContrail;
    }

    @Override
    public void jetlag$createContrail() {
        if(!JetLagConfig.getInstance().contrailsEnabled) return;
        AbstractClientPlayerEntity self = (AbstractClientPlayerEntity) (Object) this;
        currentContrail = new Contrail(self);
        ContrailHandler.addPlayerContrail(self, currentContrail);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void jetlag$tick(CallbackInfo ci) {
        jetlag$tickElytraRoll();
        jetlag$tickContrails();
        jetlag$tickWindLines();
    }

    @Unique
    private void jetlag$tickElytraRoll() {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        this.fakeElytraRoll = ContrailHandler.calculateElytraRoll(player);
    }

    @Unique
    public void jetlag$tickContrails() {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        Contrail currentContrail = jetlag$getCurrentContrail();

        //if player is flying with an elytra
        if(player.isFallFlying()) {
            //tick current contrail
            if(currentContrail != null) {
                //adds new points to the current contrail
                if (JetLagConfig.getInstance().contrailsEnabled && ContrailHandler.shouldTick()) {
                    pointTicks--;
                    if(pointTicks <= 0) {
                        currentContrail.addPoint();
                        pointTicks = JetLagConfig.getInstance().ticksPerPoint;
                    }
                }
            } else {
                //no current contrail exists yet
                jetlag$createContrail();
            }
        } else if(currentContrail != null) {
            //player has just landed
            jetlag$endCurrentContrail();
        }
    }

    @Unique
    public void jetlag$tickWindLines() {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        if(player.isFallFlying()) {
            windLineTicks--;
            if(windLineTicks <= 0) {
                WindLineHandler.spawnWindLineParticles(player);
                int max = (int) MathHelper.clamp(8 - (player.getVelocity().lengthSquared()), 3, 7);
                windLineTicks = player.getRandom().nextBetween(2, max);
            }
        }
    }

    @Override
    public void jetlag$endCurrentContrail() {
        if(this.currentContrail != null) {
            this.currentContrail.startDeletingPoints();
            this.currentContrail = null;
        }
    }
}
