package com.vomiter.spidersshootwebs.registry;

import com.vomiter.spidersshootwebs.SpidersShootWebs;
import com.vomiter.spidersshootwebs.block.TemporaryCobweb;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SpidersShootWebs.MOD_ID);

    public static final RegistryObject<Block> TEMP_WEB
            = BLOCKS.register("temporary_web", () -> new TemporaryCobweb(BlockBehaviour.Properties.copy(Blocks.COBWEB)));
}
