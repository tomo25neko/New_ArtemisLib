package org.tomo25neko.new_artemis.compatibilities;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.tomo25neko.new_artemis.Reference;
import org.tomo25neko.new_artemis.compatibilities.sizeCap.ISizeCap;
import org.tomo25neko.new_artemis.compatibilities.sizeCap.SizeCapPro;
import org.tomo25neko.new_artemis.compatibilities.sizeCap.SizeDefaultCap;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class CapabilitiesHandler {

    // Capabilityの追加
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAddCapabilities(AttachCapabilitiesEvent<Entity> event) {
        // EntityがLivingEntityであり、サイズキャパビリティが存在しない場合に追加
        if (event.getObject() instanceof LivingEntity entity && !entity.getCapability(SizeCapPro.SIZE_CAPABILITY).isPresent()) {
            final boolean transformed = false;
            final float defaultWidth = entity.getBbWidth();
            final float defaultHeight = entity.getBbHeight();
            final ISizeCap cap = new SizeDefaultCap(transformed, defaultWidth, defaultHeight);
            event.addCapability(new ResourceLocation(Reference.MODID, "Capability"), new SizeCapPro(cap));
        }
    }
}
