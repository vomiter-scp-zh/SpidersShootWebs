package com.vomiter.spidersshootwebs.registry;

import com.vomiter.spidersshootwebs.SpidersShootWebs;
import com.vomiter.spidersshootwebs.block.TemporaryCobweb;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(SpidersShootWebs.MOD_ID);

    public static final DeferredBlock<@NotNull TemporaryCobweb> TEMP_WEB =
            BLOCKS.registerBlock(
                    "temporary_web",
                    TemporaryCobweb::new,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.COBWEB)
            );

    private ModBlocks() {}
}