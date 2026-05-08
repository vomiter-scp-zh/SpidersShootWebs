package com.vomiter.spidersshootwebs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TemporaryCobweb extends WebBlock {

    public TemporaryCobweb(Properties p_58178_) {
        super(p_58178_);
    }

    public boolean isRandomlyTicking(@NotNull BlockState p_49921_) {
        return true;
    }

    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull RandomSource randomSource){
        if(randomSource.nextFloat() < 0.2){
            serverLevel.destroyBlock(pos, true);
        }
    }

}
