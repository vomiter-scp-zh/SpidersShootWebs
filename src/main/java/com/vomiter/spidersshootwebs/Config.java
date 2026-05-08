package com.vomiter.spidersshootwebs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SpidersShootWebs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // ===== Spec-backed values =====
    private static final ForgeConfigSpec.BooleanValue CAVE_SPIDER_CAN_SHOOT_WEB_SPEC;
    private static final ForgeConfigSpec.BooleanValue NORMAL_SPIDER_CAN_SHOOT_WEB_SPEC;
    private static final ForgeConfigSpec.BooleanValue APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE_SPEC;
    private static final ForgeConfigSpec.BooleanValue TEMPORARY_WEB_BLOCKS_SPEC;

    public static boolean CAVE_SPIDER_CAN_SHOOT_WEB = true;
    public static boolean NORMAL_SPIDER_CAN_SHOOT_WEB = true;
    public static boolean APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE = false;
    public static boolean TEMPORARY_WEB_BLOCKS = false;

    static {
        BUILDER.push("general");

        CAVE_SPIDER_CAN_SHOOT_WEB_SPEC = BUILDER
                .comment("If true, Cave Spiders can shoot cobweb projectiles.")
                .define("caveSpiderCanShootWeb", true);

        NORMAL_SPIDER_CAN_SHOOT_WEB_SPEC = BUILDER
                .comment("If true, normal Spiders can shoot cobweb projectiles.")
                .define("normalSpiderCanShootWeb", true);

        APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE_SPEC = BUILDER
                .comment("If true, targets hit by the cobweb projectile receive Slowness.")
                .define("applySlownessWhenHitByCobwebProjectile", false);

        TEMPORARY_WEB_BLOCKS_SPEC = BUILDER
                .comment("If true, cobweb blocks placed by spider projectiles do not drop string when broken, and it will disappear after a period of time.")
                .define("temporaryWebBlocks", false);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() != SPEC) return;
        bake();
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != SPEC) return;
        bake();
    }

    private static void bake() {
        CAVE_SPIDER_CAN_SHOOT_WEB = CAVE_SPIDER_CAN_SHOOT_WEB_SPEC.get();
        NORMAL_SPIDER_CAN_SHOOT_WEB = NORMAL_SPIDER_CAN_SHOOT_WEB_SPEC.get();
        APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE = APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE_SPEC.get();
        TEMPORARY_WEB_BLOCKS = TEMPORARY_WEB_BLOCKS_SPEC.get();
    }
}