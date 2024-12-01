package org.new_artemis.util;

import net.minecraftforge.event.TickEvent;
import org.new_artemis.compatibilities.sizeCap.ISizeCap;
import org.new_artemis.compatibilities.sizeCap.SizeCapPro;
import org.new_artemis.util.attributes.ArtemisLibAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.event.lifecycle.FMLClientSetupEvent;
import com.mojang.blaze3d.systems.RenderSystem;

public class AttachAttributes {

    // 属性の設定を行うイベント
    @SubscribeEvent
    public void attachAttributes(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            // 属性マップに新しい属性を追加
            entity.getAttributes().put(ArtemisLibAttributes.ENTITY_HEIGHT, new AttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT));
            entity.getAttributes().put(ArtemisLibAttributes.ENTITY_WIDTH, new AttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH));
        }
    }

    // プレイヤーのサイズに影響を与える処理（トランスフォーム）
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        final PlayerEntity player = event.player;
        if (player.hasCapability(SizeCapPro.sizeCapability, null)) {
            final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);

            final AttributeInstance heightAttr = player.getAttribute(ArtemisLibAttributes.ENTITY_HEIGHT);
            final AttributeInstance widthAttr = player.getAttribute(ArtemisLibAttributes.ENTITY_WIDTH);

            final double heightValue = heightAttr.getValue();
            final double widthValue = widthAttr.getValue();

            // プレイヤーのデフォルトサイズを調整
            float height = (float) (cap.getDefaultHeight() * heightValue);
            float width = (float) (cap.getDefaultWidth() * widthValue);

            // プレイヤーがサイズ変化する場合の処理
            if (!cap.getTrans()) {
                cap.setDefaultHeight(player.getHeight());
                cap.setDefaultWidth(player.getWidth());
                cap.setTrans(true);
            }

            if (cap.getTrans()) {
                height = MathHelper.clamp(height, 0.25F, height);
                width = MathHelper.clamp(width, 0.15F, width);

                // サイズ変更の適用
                player.height = height;
                player.width = width;
                AxisAlignedBB aabb = player.getBoundingBox();
                player.setBoundingBox(new AxisAlignedBB(player.getX() - width / 2.0, aabb.minY, player.getZ() - width / 2.0,
                        player.getX() + width / 2.0, aabb.minY + height, player.getZ() + width / 2.0));
            }
        }
    }

    // 他のエンティティのアップデート時のサイズ調整
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof PlayerEntity)) {
            if (entity.hasCapability(SizeCapPro.sizeCapability, null)) {
                final ISizeCap cap = entity.getCapability(SizeCapPro.sizeCapability, null);
                // サイズ変更ロジック
                if (!cap.getTrans()) {
                    cap.setTrans(true);
                    // 初期のサイズを保存
                    cap.setDefaultHeight(entity.getHeight());
                    cap.setDefaultWidth(entity.getWidth());
                }
                if (cap.getTrans()) {
                    float height = (float) (cap.getDefaultHeight());
                    float width = (float) (cap.getDefaultWidth());

                    height = MathHelper.clamp(height, 0.08F, height);
                    width = MathHelper.clamp(width, 0.04F, width);
                    entity.height = height;
                    entity.width = width;
                    AxisAlignedBB aabb = entity.getBoundingBox();
                    entity.setBoundingBox(new AxisAlignedBB(entity.getX() - width / 2.0, aabb.minY, entity.getZ() - width / 2.0,
                            entity.getX() + width / 2.0, aabb.minY + height, entity.getZ() + width / 2.0));
                }
            }
        }
    }

    // 描画前のサイズ調整（クライアント側）
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onEntityRenderPre(RenderLivingEvent.Pre event) {
        final EntityLivingBase entity = event.getEntity();
        if (entity.hasCapability(SizeCapPro.sizeCapability, null)) {
            final ISizeCap cap = entity.getCapability(SizeCapPro.sizeCapability, null);
            if (cap.getTrans()) {
                float scaleHeight = entity.getHeight() / cap.getDefaultHeight();
                float scaleWidth = entity.getWidth() / cap.getDefaultWidth();
                event.getMatrixStack().push();
                RenderSystem.pushMatrix();  // 変更された部分: RenderSystemを使用
                event.getMatrixStack().scale(scaleWidth, scaleHeight, scaleWidth);
                event.getMatrixStack().translate(event.getX() / scaleWidth - event.getX(), event.getY() / scaleHeight - event.getY(), event.getZ() / scaleWidth - event.getZ());
            }
        }
    }

    // 描画後のサイズ調整（クライアント側）
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onLivingRenderPost(RenderLivingEvent.Post event) {
        final EntityLivingBase entity = event.getEntity();
        if (entity.hasCapability(SizeCapPro.sizeCapability, null)) {
            final ISizeCap cap = entity.getCapability(SizeCapPro.sizeCapability, null);
            if (cap.getTrans()) {
                event.getMatrixStack().pop();
                RenderSystem.popMatrix();  // 変更された部分: RenderSystemを使用
            }
        }
    }

    // クライアント側で必要な設定
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        IEventBus bus = event.getBus();
        bus.addListener(AttachAttributes::attachAttributes);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        // クライアント側の初期設定を行う場合
    }
}
