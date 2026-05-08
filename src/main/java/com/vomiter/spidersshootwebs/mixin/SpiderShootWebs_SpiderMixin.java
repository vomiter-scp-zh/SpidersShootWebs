package com.vomiter.spidersshootwebs.mixin;

import com.vomiter.neurolib.common.entity.IReasonTracker;
import com.vomiter.neurolib.common.entity.NeuroLibReasons;
import com.vomiter.spidersshootwebs.entity.ISpiderShootWebGoalAccess;
import com.vomiter.spidersshootwebs.entity.IWebGetter;
import com.vomiter.spidersshootwebs.entity.ai.SpiderShootWebGoal;
import com.vomiter.spidersshootwebs.registry.ModBlocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Spider.class)
public abstract class SpiderShootWebs_SpiderMixin implements ISpiderShootWebGoalAccess, IWebGetter {

    @Unique @Nullable
    private LivingEntity PREVIOUS_TARGET;

    @Unique @Nullable
    private SpiderShootWebGoal spidersshootwebs$shootGoal;

    @Override
    public @Nullable SpiderShootWebGoal spidersshootwebs$getShootGoal() {
        return spidersshootwebs$shootGoal;
    }

    /**
     * 掛載 SpiderShootWebGoal
     *
     * 原版 priorities:
     * 1 Float
     * 3 LeapAtTarget
     * 4 SpiderAttackGoal (MeleeAttackGoal)
     * ...
     *
     * 你要求 shoot priority = 3，所以直接加在 TAIL。
     */
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void spidersshootwebs$registerShootGoal(CallbackInfo ci) {
        Spider self = (Spider) (Object) this;

        // 參數：
        // maxDist=32
        // minTargetHorizontalSpeed=0.10
        // holdAfterShot=20 ticks
        // cooldownAfterShot=200 ticks
        spidersshootwebs$shootGoal = new SpiderShootWebGoal(self, 32.0, 0.07, 20, 20, 50);
        self.goalSelector.addGoal(3, spidersshootwebs$shootGoal);
    }

    /**
     * 每 tick 遞減 cooldown
     *（GoalSelector 不保證每 tick 叫 canUse，所以不要把 cooldown 減在 canUse 裡）
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void spidersshootwebs$tickCooldown(CallbackInfo ci) {
        Spider spider = (Spider) (Object) this;
        LivingEntity target = spider.getTarget();
        if(target instanceof IReasonTracker reasonTracker){
            reasonTracker.neuroLib$refreshReason(
                    NeuroLibReasons.RECORD_MOVEMENT,
                    spider.getUUID(),
                    spider.level().getGameTime() + 5
            );
        }
    }

    @Inject(method = "makeStuckInBlock", at = @At("HEAD"), cancellable = true)
    private void spidersshootwebs$preventStuckTempWeb(BlockState state, Vec3 motionMultiplier, CallbackInfo ci){
        if(state.is(ModBlocks.TEMP_WEB.get())) ci.cancel();
    }
}