package org.new_artemis.network;

import org.new_artemis.Reference;
import org.new_artemis.Entity.EntitySizeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import java.util.function.Supplier;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1.0";

    // チャンネルの初期化
    public final static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MODID),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int ID = 0;

    private static int nextId() {
        return ID++;
    }

    // 初期化メソッド
    public static void init() {
        INSTANCE.registerMessage(nextId(), SetSizePacket.class, SetSizePacket::encode, SetSizePacket::decode, SetSizePacket::handle);
        INSTANCE.registerMessage(nextId(), SetSizeOfClientPacket.class, SetSizeOfClientPacket::encode, SetSizeOfClientPacket::decode, SetSizeOfClientPacket::handle);
    }

    // SetSizePacket（エンティティのサイズを設定するパケット）
    public static class SetSizePacket {
        private final int entityId;
        private final double size;
        private final boolean interpolate;

        public SetSizePacket(int entityId, double size, boolean interpolate) {
            this.entityId = entityId;
            this.size = size;
            this.interpolate = interpolate;
        }

        // パケットのエンコード
        public static void encode(SetSizePacket packet, FriendlyByteBuf buffer) {
            buffer.writeInt(packet.entityId);
            buffer.writeDouble(packet.size);
            buffer.writeBoolean(packet.interpolate);
        }

        // パケットのデコード
        public static SetSizePacket decode(FriendlyByteBuf buffer) {
            return new SetSizePacket(buffer.readInt(), buffer.readDouble(), buffer.readBoolean());
        }

        // パケットの処理
        public static void handle(SetSizePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                EntitySizeManager.setClientSize(packet.entityId, packet.size, packet.interpolate);
            });
            context.get().setPacketHandled(true);
        }
    }

    // SetSizeOfClientPacket（クライアントのサイズを設定するパケット）
    public static class SetSizeOfClientPacket {
        private final double size;
        private final boolean interpolate;

        public SetSizeOfClientPacket(double size, boolean interpolate) {
            this.size = size;
            this.interpolate = interpolate;
        }

        // パケットのエンコード
        public static void encode(SetSizeOfClientPacket packet, FriendlyByteBuf buffer) {
            buffer.writeDouble(packet.size);
            buffer.writeBoolean(packet.interpolate);
        }

        // パケットのデコード
        public static SetSizeOfClientPacket decode(FriendlyByteBuf buffer) {
            return new SetSizeOfClientPacket(buffer.readDouble(), buffer.readBoolean());
        }

        // パケットの処理
        public static void handle(SetSizeOfClientPacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                ServerPlayer player = context.get().getSender();
                if (player != null) {
                    EntitySizeManager.setSizeOfClient(player, packet.size, packet.interpolate);
                }
            });
            context.get().setPacketHandled(true);
        }
    }

    // サーバーからクライアントへのサイズ変更パケット送信
    public static void setSizeOnClient(ServerPlayer client, int entityId, double size, boolean interpolate) {
        INSTANCE.sendTo(new SetSizePacket(entityId, size, interpolate), client);
    }

    // クライアントのサイズ設定
    public static void setSizeOfClient(ServerPlayer client, double size, boolean interpolate) {
        INSTANCE.sendTo(new SetSizeOfClientPacket(size, interpolate), client);
    }
}
