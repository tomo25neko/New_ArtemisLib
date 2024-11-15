package org.tomo25neko.new_artemis.compatibilities.sizeCap;

import net.minecraft.nbt.CompoundTag;

public interface ISizeCap {

    /**
     * Gets whether the entity is transformed.
     *
     * @return true if the entity is transformed, false otherwise.
     */
    boolean isTransformed();

    /**
     * Sets whether the entity is transformed.
     *
     * @param transformed true to mark the entity as transformed, false otherwise.
     */
    void setTransformed(boolean transformed);

    /**
     * Gets the default width of the entity.
     *
     * @return the default width.
     */
    float getDefaultWidth();

    /**
     * Sets the default width of the entity.
     *
     * @param width the default width to set.
     */
    void setDefaultWidth(float width);

    /**
     * Gets the default height of the entity.
     *
     * @return the default height.
     */
    float getDefaultHeight();

    /**
     * Sets the default height of the entity.
     *
     * @param height the default height to set.
     */
    void setDefaultHeight(float height);

    /**
     * Saves the size capabilities to an NBT tag.
     *
     * @return a CompoundTag containing the size capabilities.
     */
    CompoundTag saveToNBT();

    /**
     * Loads the size capabilities from an NBT tag.
     *
     * @param compound the CompoundTag to load from.
     */
    void loadFromNBT(CompoundTag compound);
}
