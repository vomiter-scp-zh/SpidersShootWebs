package com.vomiter.spidersshootwebs;

import com.mojang.logging.LogUtils;
import com.vomiter.spidersshootwebs.client.ClientModEvents;
import com.vomiter.spidersshootwebs.registry.ModEntities;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SpidersShootWebs.MOD_ID)
public class SpidersShootWebs
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "spidersshootwebs";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean DEBUG_MODE = false;


    public SpidersShootWebs(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModEntities.ENTITIES.register(modBus);
        if(FMLEnvironment.dist.isClient()){
            modBus.addListener(ClientModEvents::onClientSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
