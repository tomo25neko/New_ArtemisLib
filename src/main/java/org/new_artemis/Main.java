package org.new_artemis;

import org.new_artemis.compatibilities.Capabilities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("newartemis")
public class Main {

    // ロガーインスタンス
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public Main() {
        // MODのイベントバスを取得
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Capabilitiesの登録
        Capabilities.init(modEventBus);

        // サーバーおよび共通セットアップ登録
        modEventBus.addListener(this::commonSetup);

        // クライアントセットアップ登録
        modEventBus.addListener(this::clientSetup);

        // Forgeイベントバスに登録
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("ArtemisLib loaded!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup initialized.");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Client setup initialized.");
    }
}
