package org.new_artemis.compatibilities.sizeCap;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SizeCapPro implements ICapabilityProvider {
    private  ISizeCap capabilitySize;
    private final LazyOptional<ISizeCap> lazyInstance = LazyOptional.of(() -> this.capabilitySize);

    public static final Capability<ISizeCap> SIZE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    // デフォルトコンストラクタで確実に初期化
    public SizeCapPro() {
        this.capabilitySize = new SizeDefaultCap(); // 必ず初期化
    }

    // 引数付きコンストラクタ
    public SizeCapPro(ISizeCap capability) {
        // capability が null でないことを確認
        this.capabilitySize = (capability != null) ? capability : new SizeDefaultCap(); // nullを避ける
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == SIZE_CAPABILITY ? lazyInstance.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return capabilitySize.saveToNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        capabilitySize.loadFromNBT(nbt);
    }
}
