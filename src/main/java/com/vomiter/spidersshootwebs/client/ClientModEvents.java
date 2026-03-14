package com.vomiter.spidersshootwebs.client;

import com.vomiter.spidersshootwebs.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientModEvents {

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> EntityRenderers.register(ModEntities.WEB_PROJECTILE.get(), ThrownItemRenderer::new));
    }
}