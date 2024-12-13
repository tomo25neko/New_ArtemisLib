package org.new_artemis.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.new_artemis.compatibilities.sizeCap.SizeCapPro;
import org.new_artemis.util.attributes.ArtemisLibAttributes;

@Mod.EventBusSubscriber(modid = "artemislib", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttachAttributes {

    @SubscribeEvent
    public static void attachAttributes(EntityAttributeCreationEvent event) {
        // AttributeSupplierを使用して属性を登録
        event.put(EntityType.PLAYER, AttributeSupplier.builder()
                .add(ArtemisLibAttributes.ENTITY_HEIGHT.get())
                .add(ArtemisLibAttributes.ENTITY_WIDTH.get())
                .build());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        player.getCapability(SizeCapPro.SIZE_CAPABILITY).ifPresent(cap -> {
            double heightAttr = player.getAttributeValue(ArtemisLibAttributes.ENTITY_HEIGHT.get());
            double widthAttr = player.getAttributeValue(ArtemisLibAttributes.ENTITY_WIDTH.get());

            float defaultHeight = cap.getDefaultHeight();
            float defaultWidth = cap.getDefaultWidth();

            if (!cap.isTransformed()) {
                cap.setDefaultHeight(player.getBbHeight());
                cap.setDefaultWidth(player.getBbWidth());
                cap.setTransformed(true);
            }

            float newHeight = Mth.clamp(defaultHeight * (float) heightAttr, 0.25F, 4.0F);
            float newWidth = Mth.clamp(defaultWidth * (float) widthAttr, 0.15F, 4.0F);

            if (cap.isTransformed()) {
                AABB newBoundingBox = new AABB(
                        player.getX() - newWidth / 2.0,
                        player.getY(),
                        player.getZ() - newWidth / 2.0,
                        player.getX() + newWidth / 2.0,
                        player.getY() + newHeight,
                        player.getZ() + newWidth / 2.0
                );
                player.setBoundingBox(newBoundingBox);
            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();

        entity.getCapability(SizeCapPro.SIZE_CAPABILITY).ifPresent(cap -> {
            if (cap.isTransformed()) {
                float scaleHeight = entity.getBbHeight() / cap.getDefaultHeight();
                float scaleWidth = entity.getBbWidth() / cap.getDefaultWidth();

                poseStack.pushPose();
                poseStack.scale(scaleWidth, scaleHeight, scaleWidth);
            }
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        event.getPoseStack().popPose();
    }
}
