package com.vomiter.spidersshootwebs.registry;

import com.vomiter.spidersshootwebs.Helpers;
import com.vomiter.spidersshootwebs.SpidersShootWebs;
import com.vomiter.spidersshootwebs.entity.WebProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<@NotNull EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SpidersShootWebs.MOD_ID);

    public static final DeferredHolder<@NotNull EntityType<?>, @NotNull EntityType<@NotNull WebProjectile>> WEB_PROJECTILE =
            ENTITIES.register("web_projectile", () ->
                    EntityType.Builder.<WebProjectile>of(WebProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)          // hitbox
                            .clientTrackingRange(64)
                            .updateInterval(10)
                            .build(
                                    ResourceKey.create(
                                            BuiltInRegistries.ENTITY_TYPE.key(),
                                            Helpers.id(SpidersShootWebs.MOD_ID, "web_projectile")
                                    )
                            )
            );
}