package org.new_artemis.compatibilities.sizeCap;

import net.minecraft.nbt.CompoundTag;

public class SizeDefaultCap implements ISizeCap {

    private boolean transformed;
    private float defaultWidth;
    private float defaultHeight;

    public SizeDefaultCap() {}

    public SizeDefaultCap(boolean transformed, float defaultWidth, float defaultHeight) {
        this.transformed = transformed;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
    }

    @Override
    public boolean isTransformed() {
        return this.transformed;
    }

    @Override
    public void setTransformed(boolean transformed) {
        this.transformed = transformed;
    }

    @Override
    public float getDefaultWidth() {
        return this.defaultWidth;
    }

    @Override
    public void setDefaultWidth(float defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    @Override
    public float getDefaultHeight() {
        return this.defaultHeight;
    }

    @Override
    public void setDefaultHeight(float defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    @Override
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("transformed", this.transformed);
        tag.putFloat("defaultWidth", this.defaultWidth);
        tag.putFloat("defaultHeight", this.defaultHeight);
        return tag;
    }


    @Override
    public void loadFromNBT(CompoundTag compound) {
        if (compound.contains("transformed")) {
            setTransformed(compound.getBoolean("transformed"));
        }
        if (compound.contains("defaultWidth")) {
            setDefaultWidth(compound.getFloat("defaultWidth"));
        }
        if (compound.contains("defaultHeight")) {
            setDefaultHeight(compound.getFloat("defaultHeight"));
        }
    }
}
