package net.superkat.jetlag.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.JetLagMain;
import net.superkat.jetlag.WindLineHandler;
import net.superkat.jetlag.config.JetLagConfig;
import net.superkat.jetlag.contrail.Contrail;
import net.superkat.jetlag.contrail.JetLagPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements JetLagPlayer {
    List<Contrail> contrails = Lists.newArrayList();
    @Nullable
    Contrail currentContrail = null;
    public int ticksUntilPoint = 0;
    public int ticksUntilWindLine = 20;
    @Override
    public List<Contrail> jetlag$getContrails() {
        return contrails;
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

    @Override
    public void jetlag$onElytraEnd() {
        //DELETEME - perhaps easier without this?
    }

    @Override
    public void jetlag$tick() {
        //renders contrails if any exists
        jetlag$renderContrailSets();

        //checks if the player is flying with an elytra
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if(player.isFallFlying()) {
            if(currentContrail != null) {
                //adds new points to the newest contrail
                //config check here to ensure that if contrails are disabled mid-flight, new points don't keep spawning
                if(JetLagConfig.getInstance().contrailsEnabled && JetLagMain.canTick()) {
                    if(jetlag$hasContrails()) {
                        if(ticksUntilPoint <= 0) {
                            currentContrail.addPoint();
                            ticksUntilPoint = JetLagConfig.getInstance().ticksPerPoint;
                        }
                        ticksUntilPoint--;
                    }
                }
            } else {
                //creates and sets the current contrail
                jetlag$createContrail();
            }

            //wind lines
            if(JetLagConfig.getInstance().windLines && JetLagMain.canTick()) {
                ticksUntilWindLine--;
                if(ticksUntilWindLine <= 0) {
                    WindLineHandler.spawnWindLineParticles(player);
                    ticksUntilWindLine = player.getWorld().random.nextBetween(3, 13);
                }
            }

        } else if (currentContrail != null) {
            currentContrail.startDeletingPoints();
            currentContrail = null;
        }
    }

    @Override
    public void jetlag$renderContrailSets() {
        for(Contrail contrail : contrails) {
            contrail.render();
        }
    }

    @Override
    public boolean jetlag$hasContrails() {
        return !contrails.isEmpty();
    }
}
