package com.vomiter.spidersshootwebs.entity.ai;

import com.vomiter.neurolib.common.entity.movement.IMovementCache;
import com.vomiter.neurolib.common.entity.generic.ICooldownGoal;
import com.vomiter.neurolib.common.entity.movement.MovementCacheHelper;
import com.vomiter.spidersshootwebs.Config;
import com.vomiter.spidersshootwebs.SpidersShootWebs;
import com.vomiter.spidersshootwebs.entity.WebProjectile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Spider;

import java.util.EnumSet;

public final class SpiderShootWebGoal extends Goal implements ICooldownGoal {
    private final Spider spider;

    private int holdTicks;
    private int ticksBeforeShoot;

    // ===== 基準參數（固定）=====
    private final double baseMaxDist;
    private final double baseMinTargetHorizontalSpeed;
    private final int baseTicksBeforeShootTotal;
    private final int baseHoldAfterShotTicks;
    private final int baseCooldownAfterShotTicks;

    // ===== 本次施放快照（動態算）=====
    private double curMaxDistSqr;
    private double curMinTargetSpeedSqr;
    private int curTicksBeforeShootTotal;
    private int curHoldAfterShotTicks;
    private int curCooldownAfterShotTicks;
    private long nextAllowedTick;


    public SpiderShootWebGoal(
            Spider spider,
            double maxDist,
            double minTargetHorizontalSpeed,
            int ticksBeforeShootTotal,
            int holdAfterShotTicks,
            int cooldownAfterShotTicks
    ) {
        this.spider = spider;

        this.baseMaxDist = maxDist;
        this.baseMinTargetHorizontalSpeed = minTargetHorizontalSpeed;
        this.baseTicksBeforeShootTotal = ticksBeforeShootTotal;
        this.baseHoldAfterShotTicks = holdAfterShotTicks;
        this.baseCooldownAfterShotTicks = cooldownAfterShotTicks;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        // 給個預設，避免第一次還沒 start 就被用到
        applyDifficultySnapshot();
    }

    private void applyDifficultySnapshot() {
        // 只在伺服端有效；客戶端就用基準值
        if (!(spider.level() instanceof ServerLevel sl)) {
            setSnapshotFromScales(1.0f, 0.0f);
            return;
        }

        Difficulty worldDiff = sl.getDifficulty();
        if (worldDiff == Difficulty.PEACEFUL) {
            setSnapshotFromScales(0.0f, 0.0f);
            return;
        }

        DifficultyInstance di = sl.getCurrentDifficultyAt(spider.blockPosition());

        //  0.0 ~ 6.75（wiki 的 Local Difficulty）
        float local = di.getEffectiveDifficulty();
        float local01 = Mth.clamp(local / 6.75f, 0.0f, 1.0f);

        float worldScale = switch (worldDiff) {
            case EASY -> 0.85f;
            case NORMAL -> 1.00f;
            case HARD -> 1.15f;
            default -> 1.00f;
        };

        setSnapshotFromScales(worldScale, local01);
    }

    private void setSnapshotFromScales(float worldScale, float local01) {
        // 難度越高越兇：射程略增、啟動門檻下降、蓄力更短、CD 更短
        double maxDist = baseMaxDist * (1.0 + 0.20 * worldScale + 0.35 * local01);
        double minSpeed = baseMinTargetHorizontalSpeed * (1.0 - 0.15 * worldScale - 0.40 * local01);

        int tBefore = Math.round(baseTicksBeforeShootTotal * (1.0f - 0.10f * worldScale - 0.45f * local01));
        int hold = Math.round(baseHoldAfterShotTicks * (1.0f + 0.05f * worldScale + 0.10f * local01));
        int cd = Math.round(baseCooldownAfterShotTicks * (1.0f - 0.10f * worldScale - 0.35f * local01));

        maxDist = Mth.clamp(maxDist, 4.0, 64.0);
        minSpeed = Mth.clamp(minSpeed, 0.00, baseMinTargetHorizontalSpeed); // 最低可到 0：代表慢慢走也會被射
        tBefore = Mth.clamp(tBefore, 5, baseTicksBeforeShootTotal);
        hold = Mth.clamp(hold, 0, 200);
        cd = Mth.clamp(cd, 10, baseCooldownAfterShotTicks);

        this.curMaxDistSqr = maxDist * maxDist;
        this.curMinTargetSpeedSqr = minSpeed;
        this.curTicksBeforeShootTotal = tBefore;
        this.curHoldAfterShotTicks = hold;
        this.curCooldownAfterShotTicks = cd;
    }

    boolean isTargetInvalid() {
        LivingEntity target = spider.getTarget();
        if (target == null || !target.isAlive()) return true;

        double d = spider.distanceToSqr(target);
        int leapRange = 16;
        return d > curMaxDistSqr || d < leapRange;
    }

    @Override
    public boolean canUse() {
        var spiderType = spider.getType();
        if (spiderType.equals(EntityType.CAVE_SPIDER) && !Config.CAVE_SPIDER_CAN_SHOOT_WEB) return false;
        else if (spiderType.equals(EntityType.SPIDER) && !Config.NORMAL_SPIDER_CAN_SHOOT_WEB) return false;
        if (spider.level().getDifficulty().equals(Difficulty.PEACEFUL)) return false;
        if (isInCooldown()) return false;
        if (spider.isVehicle()) return false;

        // 只在 start() 做一次快照最穩
        if (isTargetInvalid()) return false;

        LivingEntity target = spider.getTarget();
        if (target == null) return false;

        if(SpidersShootWebs.DEBUG_MODE) SpidersShootWebs.LOGGER.debug("[SSW] MIN SPEED = {}", curMinTargetSpeedSqr);
        return ((IMovementCache) target).neuroLib$hasAnyStepSqrAbove(curMinTargetSpeedSqr);
    }

    @Override
    public void start() {
        applyDifficultySnapshot();

        ticksBeforeShoot = curTicksBeforeShootTotal;
        holdTicks = 0;

        freeze();
        LivingEntity target = spider.getTarget();
        if (target != null) lookAt(target);
    }

    @Override
    public boolean canContinueToUse() {
        if (isTargetInvalid()) return false;
        return ticksBeforeShoot > 0 || holdTicks > 0;
    }

    @Override
    public void tick() {
        freeze();

        LivingEntity target = spider.getTarget();
        if (target != null) lookAt(target);

        if (ticksBeforeShoot > 0) {
            ticksBeforeShoot--;
            if (ticksBeforeShoot == 0 && target != null) {
                shootOnce(target);
                holdTicks = curHoldAfterShotTicks;
            }
            return;
        }

        if (holdTicks > 0) {
            holdTicks--;
        }
    }

    @Override
    public void stop() {
        ticksBeforeShoot = 0;
        holdTicks = 0;
        startCooldown();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void freeze() {
        spider.getNavigation().stop();
        spider.getMoveControl().setWantedPosition(spider.getX(), spider.getY(), spider.getZ(), 0.0D);

        var dm = spider.getDeltaMovement();
        spider.setDeltaMovement(0.0D, dm.y, 0.0D);
    }

    private void lookAt(LivingEntity target) {
        spider.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    private void shootOnce(LivingEntity target) {
        if (spider.level().isClientSide) return;

        WebProjectile p = new WebProjectile(spider.level(), spider);

        double sx = spider.getX();
        double sy = spider.getEyeY() - 0.1;
        double sz = spider.getZ();
        p.setPos(sx, sy, sz);

        double tx = target.getX();
        double ty = target.getEyeY() - 0.15;
        double tz = target.getZ();

        double leadFactor = 1.0D; // 可調
        double leadX = MovementCacheHelper.getDx(target) * leadFactor;
        double leadZ = MovementCacheHelper.getDz(target) * leadFactor;

        tx += leadX;
        tz += leadZ;

        double dx = tx - sx;
        double dy = ty - sy;
        double dz = tz - sz;

        double flat = Math.sqrt(dx * dx + dz * dz);
        dy += flat * 0.02;

        float velocity = 1.4F;
        float inaccuracy = 0.5F;

        p.shoot(dx, dy, dz, velocity, inaccuracy);
        spider.level().addFreshEntity(p);
    }

    public boolean shouldPreemptAttack() {
        return canUse();
    }

    @Override
    public long getNextAllowedTick() {
        return nextAllowedTick;
    }

    @Override
    public void setNextAllowedTick(long l) {
        nextAllowedTick = l;
    }

    @Override
    public long getCooldownTicks() {
        return curCooldownAfterShotTicks;
    }
}