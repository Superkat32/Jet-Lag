package net.superkat.jetlag.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
    @Override
    public List<Contrail> jetlag$getContrails() {
        return contrails;
    }

    @Override
    public void jetlag$createContrail() {
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
        //renders air streaks if any exists
        jetlag$renderContrailSets();

        //checks if the player is flying with an elytra
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if(player.isFallFlying()) {
            if(currentContrail != null) {
                //adds new points to the newest air streak
                MinecraftClient client = MinecraftClient.getInstance();
                if(!client.isPaused() && (client.world == null || client.world.getTickManager().shouldTick())) {
                    if(jetlag$hasContrails()) {
                        currentContrail.addPoint();
                    }
                }
            } else {
                //creates and sets the current air streak
                jetlag$createContrail();
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


//    public Contrail playerAirStreaks = null;
//    @Override
//    public Contrail jetLag$getPlayerAirStreaks() {
//        return playerAirStreaks;
//    }
//
//    @Override
//    public void jetLag$setAirStreak(Contrail airStreak) {
//        this.playerAirStreaks = airStreak;
//    }
//
//    @Override
//    public void jetLag$updateAirStreakPoint() {
//        playerAirStreaks.addPoint();
//    }
//
//    @Override
//    public void addPoint(MatrixStack matrixStack, ClientPlayerEntity entity) {
//        if(playerAirStreaks != null) {
//            playerAirStreaks.addPoint(matrixStack, entity);
//        }
//    }
//
//    @Override
//    public void addPoint(Vec3d pointLoc) {
//        if(playerAirStreaks != null) {
//            playerAirStreaks.addPoint(pointLoc);
//        }
//    }
//
//    @Inject(method = "tick", at = @At("TAIL"))
//    public void jetLag$updateAirStreaks(CallbackInfo ci) {
//        if(playerAirStreaks != null) {
//            ContrailHandler.updatePlayerAirStreaks((ClientPlayerEntity) (Object) this);
//        }
//    }
}
