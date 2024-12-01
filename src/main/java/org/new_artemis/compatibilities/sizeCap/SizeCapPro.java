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

    private final ISizeCap capabilitySize;
    private final LazyOptional<ISizeCap> lazyInstance;

    // Capabilityトークンの宣言
    public static final Capability<ISizeCap> SIZE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    // 引数付きコンストラクタ
    public SizeCapPro(ISizeCap capability) {
        this.capabilitySize = (capability != null) ? capability : new SizeDefaultCap(); // nullを避ける
        this.lazyInstance = LazyOptional.of(() -> this.capabilitySize);
    }

    // getCapabilityメソッドのオーバーライド
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == SIZE_CAPABILITY ? lazyInstance.cast() : LazyOptional.empty();
    }

    // CapabilityのNBTシリアライズ
    public CompoundTag serializeNBT() {
        return capabilitySize.saveToNBT();
    }

    // CapabilityのNBTデシリアライズ
    public void deserializeNBT(CompoundTag nbt) {
        capabilitySize.loadFromNBT(nbt);
    }

    // LazyOptionalの解放
    public void invalidate() {
        lazyInstance.invalidate();
    }
}
