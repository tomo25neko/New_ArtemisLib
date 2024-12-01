package org.new_artemis.network;

import org.new_artemis.Reference;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;


public class NetworkHandler {

    private static final String PROTCOL_VERSION = "1.0";

    public final static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MODID),
            () -> PROTCOL_VERSION,
            PROTCOL_VERSION::equals,
            PROTCOL_VERSION::equals
    );

    private static int ID = 0;

    private static int nextId() {
        return ID++;
    }

    public static void init() {

    }
}
