package org.new_artemis.proxy;

import javax.annotation.Nullable;

import org.new_artemis.compatibilities.Capabilities;
import org.new_artemis.compatibilities.CapabilitiesHandler;
import org.new_artemis.network.NetworkHandler;
import org.new_artemis.util.AttachAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.thread.IThreadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.ModEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;

@Mod("newartemis")
public class CommonProxy {

    // コンストラクタでイベントリスナーを登録
    public CommonProxy() {
        IEventBus modEventBus = Minecraft.getInstance().getForgeEventBus();
        modEventBus.addListener(this::commonSetup);  // サーバー側の初期化処理
        modEventBus.addListener(this::clientSetup);  // クライアント側の初期化処理
    }

    // サーバー側の初期化処理
    public void commonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.init();   // ネットワークの初期化
        Capabilities.init();     // Capabilityの初期化
    }

    // クライアント側の初期化処理
    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        // クライアントのレンダリング処理等
        Minecraft.getInstance().getRenderManager();
    }

    // アイテムのレンダラー登録処理（必要に応じて実装）
    public void registerItemRenderer(Item item, int meta, String id) {
        // アイテムレンダラーをカスタマイズする処理をここに記述
    }

    // メッセージコンテキストからスレッドリスナーを取得
    public IThreadListener getThreadListener(final NetworkEvent.Context context) {
        if (context.getDirection().isServer()) {
            return context.getSender().getServer();
        } else {
            throw new WrongSideException("Tried to get the IThreadListener from a client-side MessageContext on the dedicated server");
        }
    }

    // メッセージコンテキストからエンティティを取得
    @Nullable
    public EntityLivingBase getEntityLivingBase(NetworkEvent.Context context, int entityID) {
        if (context.getDirection().isServer()) {
            final Entity entity = context.getSender().level.getEntity(entityID);
            return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
        }
        throw new WrongSideException("Tried to get the player from a client-side MessageContext on the dedicated server");
    }

    // サーバーとクライアントのサイドを確認する例外クラス
    class WrongSideException extends RuntimeException {
        public WrongSideException(final String message) {
            super(message);
        }

        public WrongSideException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
