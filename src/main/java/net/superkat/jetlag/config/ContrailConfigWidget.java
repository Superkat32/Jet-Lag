package net.superkat.jetlag.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.superkat.jetlag.contrail.Contrail;
import net.superkat.jetlag.rendering.ContrailRenderer;

import java.awt.*;

public class ContrailConfigWidget extends ClickableWidget {
    private int x;
    private int y;
    private int width;
    private int height;
    public ContrailConfigWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.of("why hello there!"), 50, 50, Color.white.getRGB());

        //player model rendering
        MinecraftClient client = MinecraftClient.getInstance();

        //get player skin and model
        SkinTextures playerSkinTextures = client.getSkinProvider().getSkinTextures(client.getGameProfile());
        Identifier playerSkinTexture = playerSkinTextures.texture();
        boolean slimModel = playerSkinTextures.model() == SkinTextures.Model.SLIM;
        EntityModelLayer playerModelLayer = slimModel ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER;
        PlayerEntityModel<PlayerEntity> playerModel = new PlayerEntityModel<>(client.getEntityModelLoader().getModelPart(playerModelLayer), slimModel);
        playerModel.child = false; //prevents making the player model look goofy
        playerModel.head.pitch = (float) Math.toDegrees(7);
        playerModel.hat.pitch = (float) Math.toDegrees(7);
        playerModel.head.yaw = (float) Math.toDegrees(-93);
        playerModel.hat.yaw = (float) Math.toDegrees(-93);

        //get player elytra texture and model
        Identifier playerElytraTexture = new Identifier("textures/entity/elytra.png");
        ;
        if (playerSkinTextures.elytraTexture() != null) {
            playerElytraTexture = playerSkinTextures.elytraTexture();
        } else if (playerSkinTextures.capeTexture() != null && client.options.isPlayerModelPartEnabled(PlayerModelPart.CAPE)) {
            playerElytraTexture = playerSkinTextures.capeTexture();
        }
        ElytraEntityModel<PlayerEntity> elytraModel = new ElytraEntityModel<>(client.getEntityModelLoader().getModelPart(EntityModelLayers.ELYTRA));
        elytraModel.leftWing.pitch = 0.34628776f;
        elytraModel.leftWing.roll = -1.5291312f;
        elytraModel.rightWing.pitch = 0.34628776f;
        elytraModel.rightWing.roll = 1.5291312f;


        MatrixStack matrixStack = context.getMatrices();
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        matrixStack.push();
        matrixStack.translate(100, 100, 50);
        matrixStack.scale(50f, 50f, 50f);
        //cool position for the player and elytra
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(75f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(15f));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(5f));

        //render the player and elytra models
        int light = LightmapTextureManager.pack(15, 15);

        VertexConsumer playerVertexConsumer = immediate.getBuffer(playerModel.getLayer(playerSkinTexture));
        playerModel.render(matrixStack, playerVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.translate(0.0F, -1.45f, 0.125F);
        matrixStack.scale(2f, 2f, 2f);
        VertexConsumer elytraVertexConsumer = immediate.getBuffer(playerModel.getLayer(playerElytraTexture));
        elytraModel.render(matrixStack, elytraVertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        immediate.draw();

        matrixStack.pop();

        //render fake contrail
        Contrail fakeContrail = new Contrail(null);
        fakeContrail.addPoint(new Contrail.ContrailPos(new Vec3d(50, 50, 0), new Vec3d(1, 1, 1)));
        fakeContrail.addPoint(new Contrail.ContrailPos(new Vec3d(70, 70, 0), new Vec3d(2, 1, 1)));
        fakeContrail.addPoint(new Contrail.ContrailPos(new Vec3d(50, 50, 0), new Vec3d(3, 1, 1)));
        ContrailRenderer.renderGuiContrail(context, fakeContrail);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
