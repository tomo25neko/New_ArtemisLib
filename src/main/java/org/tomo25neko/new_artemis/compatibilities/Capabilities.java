package org.tomo25neko.new_artemis.compatibilities;

import org.tomo25neko.new_artemis.compatibilities.sizeCap.ISizeCap;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class Capabilities {

    // Capabilitiesの初期化
    public static void init(IEventBus modEventBus) {
        // イベントバスに登録して、Capabilitesを登録
        modEventBus.addListener(Capabilities::registerCapabilities);
    }

    // Capabilityの登録
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ISizeCap.class);
    }
}
