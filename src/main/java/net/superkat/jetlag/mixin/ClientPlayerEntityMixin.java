package net.superkat.jetlag.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.network.ClientPlayerEntity;
import net.superkat.jetlag.airstreak.AirStreak;
import net.superkat.jetlag.airstreak.JetLagPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements JetLagPlayer {
    List<AirStreak> airStreaks = Lists.newArrayList();
    @Nullable
    AirStreak currentAirStreak = null;
    @Override
    public List<AirStreak> jetlag$getAirStreaks() {
        return airStreaks;
    }

    @Override
    public void jetlag$createAirStreak() {
        currentAirStreak = new AirStreak((ClientPlayerEntity) (Object) this);
        airStreaks.add(currentAirStreak);
    }

    @Override
    public void jetlag$removeAllAirStreaks() {
        airStreaks = Lists.newArrayList();
    }

    @Override
    public void jetlag$onElytraEnd() {

    }

    @Override
    public void jetlag$tick() {
        //renders air streaks if any exists
        if(jetlag$hasAirStreaks()) {
            jetlag$renderAirStreakSets();
        }

        //checks if the player is flying with an elytra
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if(player.isFallFlying()) {
            if(currentAirStreak != null) {
                //adds new points to the newest air streak
                currentAirStreak.addPoint();
            } else {
                //creates and sets the current air streak
                jetlag$createAirStreak();
            }
        }
    }

    @Override
    public void jetlag$renderAirStreakSets() {
        for(AirStreak airStreak : airStreaks) {
            airStreak.render();
        }
    }

    @Override
    public boolean jetlag$hasAirStreaks() {
        return !airStreaks.isEmpty();
    }


//    public AirStreak playerAirStreaks = null;
//    @Override
//    public AirStreak jetLag$getPlayerAirStreaks() {
//        return playerAirStreaks;
//    }
//
//    @Override
//    public void jetLag$setAirStreak(AirStreak airStreak) {
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
//            AirStreakHandler.updatePlayerAirStreaks((ClientPlayerEntity) (Object) this);
//        }
//    }
}
