package com.vomiter.spidersshootwebs.entity;

import com.vomiter.spidersshootwebs.Config;
import com.vomiter.spidersshootwebs.registry.ModBlocks;
import com.vomiter.spidersshootwebs.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class WebProjectile extends ThrowableProjectile implements ItemSupplier {

    public WebProjectile(EntityType<? extends WebProjectile> type, Level level) {
        super(type, level);
    }

    public WebProjectile(Level level, LivingEntity owner) {
        super(ModEntities.WEB_PROJECTILE.get(), owner, level);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide()) {
            if(Config.APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE){
                if(hit.getEntity() instanceof LivingEntity living){
                    living.addEffect(
                            new MobEffectInstance(
                                    MobEffects.MOVEMENT_SLOWDOWN,
                                    40, 1,
                                    false, false, true)
                            , getOwner());
                }
            }
            placeCobwebNearImpact(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, null);
            discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hit) {
        super.onHitBlock(hit);
        if (!level().isClientSide()) {
            // 以命中的方塊外側面為優先落點（比較像「黏在表面」）
            Direction face = hit.getDirection();
            BlockPos pos = hit.getBlockPos().relative(face);
            placeCobwebAtOrNearby(pos);
            discard();
        }
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
    }

    private void placeCobwebNearImpact(double x, double y, double z, BlockPos preferred) {
        if (level().isClientSide()) return;

        //  mobGriefing rule
        /*
        if (getOwner() instanceof LivingEntity) {
            boolean mobGriefing = server.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            if (!mobGriefing) return;
        }
         */

        if (preferred != null) {
            placeCobwebAtOrNearby(preferred);
            return;
        }

        BlockPos center = BlockPos.containing(x, y, z);
        placeCobwebAtOrNearby(center);
    }

    private void placeCobwebAtOrNearby(BlockPos center) {
        if (!(level() instanceof ServerLevel server)) return;

        BlockState cobweb = getWebBlock();

        // 優先：中心；其次：附近 6 面（避免命中在實體內/方塊內完全放不下）
        if (canPlaceCobweb(center)) {
            server.setBlock(center, cobweb, 3);
            return;
        }

        for (Direction d : Direction.values()) {
            BlockPos p = center.relative(d);
            if (canPlaceCobweb(p)) {
                server.setBlock(p, cobweb, 3);
                return;
            }
        }
    }

    private boolean canPlaceCobweb(BlockPos pos) {
        // 只在空氣/可替換方塊上放，避免把別人的方塊硬換掉
        BlockState state = level().getBlockState(pos);
        return state.isAir() || state.canBeReplaced();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemStack(Blocks.COBWEB);
    }

    public @NotNull BlockState getWebBlock(){
        if(Config.TEMPORARY_WEB_BLOCKS) return ModBlocks.TEMP_WEB.get().defaultBlockState();
        return Blocks.COBWEB.defaultBlockState();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        
    }
}