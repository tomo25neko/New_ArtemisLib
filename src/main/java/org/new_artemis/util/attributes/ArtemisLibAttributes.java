package org.new_artemis.util.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ArtemisLibAttributes {

    // プレイヤーやエンティティの高さを設定する属性
    public static final Attribute ENTITY_HEIGHT = new RangedAttribute("entityHeight", 1.0F, Float.MIN_VALUE, Float.MAX_VALUE);

    // プレイヤーやエンティティの幅を設定する属性
    public static final Attribute ENTITY_WIDTH = new RangedAttribute("entityWidth", 1.0F, Float.MIN_VALUE, Float.MAX_VALUE);
}
