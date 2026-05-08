package com.vomiter.spidersshootwebs.registry;

import com.vomiter.spidersshootwebs.SpidersShootWebs;
import com.vomiter.spidersshootwebs.block.TemporaryCobweb;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, SpidersShootWebs.MOD_ID);

    public static final DeferredHolder<Block, Block> TEMP_WEB
            = BLOCKS.register("temporary_web", () -> new TemporaryCobweb(BlockBehaviour.Properties.ofFullCopy(Blocks.COBWEB)));
}
