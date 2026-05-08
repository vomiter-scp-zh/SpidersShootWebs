package com.vomiter.spidersshootwebs;

import com.mojang.logging.LogUtils;
import com.vomiter.spidersshootwebs.client.ClientModEvents;
import com.vomiter.spidersshootwebs.registry.ModBlocks;
import com.vomiter.spidersshootwebs.registry.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SpidersShootWebs.MOD_ID)
public class SpidersShootWebs
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "spidersshootwebs";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean DEBUG_MODE = false;


    public SpidersShootWebs(ModContainer mod, IEventBus modBus) {
        modBus.addListener(this::commonSetup);
        mod.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModEntities.ENTITIES.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        if(FMLEnvironment.getDist().isClient()){
            modBus.addListener(ClientModEvents::onClientSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
