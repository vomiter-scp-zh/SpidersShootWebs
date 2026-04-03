package com.vomiter.spidersshootwebs;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = SpidersShootWebs.MOD_ID)
public final class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ===== Spec-backed values =====
    private static final ModConfigSpec.BooleanValue CAVE_SPIDER_CAN_SHOOT_WEB_SPEC;
    private static final ModConfigSpec.BooleanValue NORMAL_SPIDER_CAN_SHOOT_WEB_SPEC;
    private static final ModConfigSpec.BooleanValue APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE_SPEC;

    public static boolean CAVE_SPIDER_CAN_SHOOT_WEB = true;
    public static boolean NORMAL_SPIDER_CAN_SHOOT_WEB = true;
    public static boolean APPLY_SLOWNESS_WHEN_HIT_BY_COBWEB_PROJECTILE = false;

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

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

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
    }
}