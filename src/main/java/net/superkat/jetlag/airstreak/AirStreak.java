package net.superkat.jetlag.airstreak;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class AirStreak {
    public final ClientPlayerEntity player;
    public List<Vec3d> leftPoints = Lists.newArrayList();
    public List<Vec3d> rightPoints = Lists.newArrayList();
    public int maxPoints = 30;
    public int ticksUntilNextPoint = 2;
    public int ticksUntilRemovePoint = 10;
    public AirStreak(ClientPlayerEntity player) {
        this.player = player;
        addPoint();
    }
    public void addPoint(MatrixStack matrixStack, ClientPlayerEntity player) {
        Vector3f vector3f = new Vector3f(0, 0, 0);
        Vector3f translated = matrixStack.peek().getPositionMatrix().getTranslation(vector3f);

//        PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
//        Matrix4f test = matrixStack.peek().getPositionMatrix();
//        Vector3f vector3f = test.getTranslation(player.getPos().toVector3f());
//        Vec3d offset = new Vec3d(vector3f.x, vector3f.y, vector3f.z);
//        double offsetX = Math.cos(player.getYaw() / 180f) * offset.x;
//        double offsetZ = Math.sin(player.getYaw() / 180f) * offset.z;
//        leftPoints.add(player.getPos().add(offsetX, offset.y, -offsetZ));

//        double xoffset = Math.cos(test.m20);
//        double yoffset = test.m21;
//        double zoffset = Math.cos(test.m22);
//        leftPoints.add(player.getPos().add(xoffset, yoffset, xoffset));

//        Matrix3f test = matrixStack.peek().getNormalMatrix().normal();
//        double xoffset = Math.cos(player.getYaw() / 180f) * test.m20;
//        double yoffset = test.m21;
//        double zoffset = Math.sin(player.getYaw() / 180f) * test.m20;
//        leftPoints.add(player.getPos().add(xoffset, yoffset, zoffset));

        leftPoints.add(new Vec3d(translated.x, translated.y, translated.z));
    }
    public void addPoint() {

//        double offX = 1.000E+0;
//        double offY = 2.100E+1;
//        double offZ = -1.000E+0;

        float f = MathHelper.cos(7.448451F * 0.017453292F + 3.1415927F) * player.elytraRoll;
        float x = MathHelper.cos((player.getYaw() * 0.017453292F)) * Math.abs(player.elytraRoll);
        float y = ((0.3f + f * 0.45f) * (player.elytraRoll * player.elytraPitch + 1.0f + player.elytraPitch));
        float z = MathHelper.sin((player.getYaw() * 0.017453292F)) * Math.abs(player.elytraRoll);

//        double pitchRad = Math.toRadians(player.elytraPitch);
//        double yawRad = Math.toRadians(player.getYaw());
//        double elytraRoll = Math.toRadians(player.elytraRoll);
//
//        float distance = 1.5f;
//        float x = (float) (Math.cos(pitchRad) * Math.cos(yawRad) * Math.cos(elytraRoll)) * distance;
//        float y = (float) (Math.sin(pitchRad) * Math.cos(elytraRoll)) * distance;
//        float z = (float) (Math.cos(pitchRad) * Math.sin(yawRad) * Math.cos(elytraRoll)) * distance;



//        float f = MathHelper.cos(7.448451F * 0.017453292F + 3.1415927F) * player.elytraRoll;
//
//        //closest thing I've got to it working properly
//        float x = MathHelper.cos(player.getYaw() * 0.017453292F) * player.elytraRoll;
//        float y = ((0.3f + f * 0.45f) * (player.elytraRoll * player.elytraPitch + 1.0f + player.elytraPitch));
//        float z = MathHelper.sin(player.getYaw() * 0.017453292F) * player.elytraRoll;


//        float x = MathHelper.cos(player.getYaw() * 0.017453292F) * (1.3f + 0.21f);
//        float y = (0.3f + f * 0.45f) * (1 * 0.2f + 1.0f);
//        float z = MathHelper.sin(player.getYaw() * 0.017453292F) * (1.3f + 0.21f);
//        System.out.println("yaw " + player.elytraYaw);
//        System.out.println("pitch " + player.elytraPitch);
//        System.out.println("roll " + player.elytraRoll);
        //-1.5707958 is the absolute max roll the left wing can have
//        leftPoints.add(player.getPos().add(x, y, z));
//        rightPoints.add(player.getPos());
    }

    public Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d((double)(i * j), (double)(-k), (double)(h * j));
    }

    public void removeOldestPoint() {
        leftPoints.remove(0);
        rightPoints.remove(0);
    }
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
    public boolean shouldStartDeletingPoints() {
        return rightPoints.size() >= maxPoints || leftPoints.size() >= maxPoints;
    }
    public void tick() {
        //if player is flying, start adding points
        if(player.isFallFlying()) {
            ticksUntilNextPoint--;
            if(ticksUntilNextPoint <= 0) {
                addPoint();
                ticksUntilNextPoint = 2;
            }

        }

        //if max points have been reached or the player has stopped flying
        if(shouldStartDeletingPoints() || !(player.isFallFlying())) {
            if(player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.TRIDENT) {
                ticksUntilRemovePoint--;
                if(ticksUntilRemovePoint <= 0) {
                    if(leftPoints.isEmpty() || rightPoints.isEmpty()) {
                        ((JetLagClientPlayerEntity) player).jetLag$setAirStreak(null);
                    } else {
                        removeOldestPoint();
                    }
                    ticksUntilRemovePoint = 2;
                }
            }
        }
    }
}
