package org.new_artemis.util.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.new_artemis.Reference;

public class ArtemisLibAttributes {

    // DeferredRegisterを使用して属性を管理
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Reference.MODID);

    // プレイヤーやエンティティの高さを設定する属性
    public static final RegistryObject<Attribute> ENTITY_HEIGHT = ATTRIBUTES.register("entity_height",
            () -> new RangedAttribute("attribute.new_artemis.entity_height", 1.0D, 0.1D, 1024.0D).setSyncable(true));

    // プレイヤーやエンティティの幅を設定する属性
    public static final RegistryObject<Attribute> ENTITY_WIDTH = ATTRIBUTES.register("entity_width",
            () -> new RangedAttribute("attribute.new_artemis.entity_width", 1.0D, 0.1D, 1024.0D).setSyncable(true));
}
