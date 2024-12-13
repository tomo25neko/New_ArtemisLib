package org.new_artemis.Entity;

import org.new_artemis.network.NetworkHandler;

import dev.necauqua.mods.cm.SidedHandler;
import org.new_artemis.Entity.ISizedEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.new_artemis.network.NetworkHandler.setSizeOnClient;

public final class EntitySizeManager {

    public static final float LOWER_LIMIT = 0.000244140625f; // = 1/16/16/16 = 1/4096
    public static final float UPPER_LIMIT = 16.0f;

    private static final Int2ObjectMap<SetSize> spawnSetSizeQueue = new Int2ObjectOpenHashMap<>();

    private EntitySizeManager() {}

    // サイズ取得メソッド
    public static double getSize(Entity entity) {
        return ((ISizedEntity) entity).getEntitySize();
    }

    // サイズ設定メソッド
    public static void setSize(Entity entity, double size, boolean interpolate) {
        if (size < LOWER_LIMIT || size > UPPER_LIMIT) {
            return;
        }
        // 乗り物から降りる
        entity.dismountTo(entity.getX(), entity.getY(), entity.getZ());
        // 乗客を全て降ろす
        entity.clearPassengers();

        // 子エンティティ（乗り物）に対してサイズを設定
        Entity ridingEntity = entity.getVehicle();
        if (ridingEntity != null) {
            setSize(ridingEntity, size, interpolate);
        }

        ((ISizedEntity) entity).setEntitySize(size, interpolate);
    }

    // サイズ設定＆同期
    public static void setSizeAndSync(Entity entity, double size, boolean interpolate) {
        setSize(entity, size, interpolate);
        setSizeOnTrackingClients(entity, size, interpolate);
    }

    // クライアント側のサイズ設定
    public static void setClientSize(int entityId, double size, boolean interp) {
        Entity entity = null;
        if (entityId == -1) {
            entity = SidedHandler.instance.getClientPlayer();
        } else {
            Level world = SidedHandler.instance.getClientWorld();
            if (world != null) {
                entity = world.getEntity(entityId);
            }
        }

        if (entity != null) {
            setSize(entity, size, interp);
        } else {
            spawnSetSizeQueue.put(entityId, new SetSize(size, interp));
        }
    }

    // トラッキングしているプレイヤーにサイズを同期
    public static void setSizeOnTrackingClients(Entity entity, double size, boolean interpolate) {
        if (entity.level.isClientSide) {
            return;
        }
        if (entity instanceof ServerPlayer) {
            NetworkHandler.setSizeOfClient((ServerPlayer) entity, size, interpolate);
        }
        for (ServerPlayer player : ((ServerLevel) entity.level).getPlayers()) {
            setSizeOnClient((ServerPlayer) player, entity.getId(), size, interpolate);
        }
    }

    // サイズ設定キューから取得
    private static void trySetSizeFromQueue(int id, Entity entity) {
        SetSize size = spawnSetSizeQueue.remove(id);
        if (size != null) {
            setSize(entity, size.size, size.interp);
        }
    }

    // エンティティがワールドに参加した時のイベント
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
        Entity entity = e.getEntity();
        if (entity.level.isClientSide) {
            if (entity instanceof ServerPlayer) {
                trySetSizeFromQueue(-1, entity);
            }
            trySetSizeFromQueue(entity.getId(), entity);
        } else {
            double size = getSize(entity);
            if (size != 1.0) {
                setSizeOnTrackingClients(entity, size, false);
            }
        }
    }

    // プレイヤーがエンティティを追跡し始めた時
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        Entity entity = e.getTarget();
        double size = getSize(entity);
        if (size != 1.0) {
            setSizeOnClient((ServerPlayer) e.getEntityPlayer(), entity.getId(), size, false);
        }
    }

    // プレイヤーがクローンされた時
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone e) {
        if (!e.isWasDeath()) {
            setSizeAndSync(e.getOriginal(), getSize(e.getEntity()), false);
        }
    }

    // サイズ情報を保持するための内部クラス
    private static class SetSize {
        private final double size;
        private final boolean interp;

        private SetSize(double size, boolean interp) {
            this.size = size;
            this.interp = interp;
        }
    }
}
