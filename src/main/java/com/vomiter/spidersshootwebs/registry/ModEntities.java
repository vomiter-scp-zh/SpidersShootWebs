package com.vomiter.spidersshootwebs.registry;

import com.vomiter.spidersshootwebs.Helpers;
import com.vomiter.spidersshootwebs.SpidersShootWebs;
import com.vomiter.spidersshootwebs.entity.WebProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SpidersShootWebs.MOD_ID);

    public static final RegistryObject<EntityType<WebProjectile>> WEB_PROJECTILE =
            ENTITIES.register("web_projectile", () ->
                    EntityType.Builder.<WebProjectile>of(WebProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)          // hitbox
                            .clientTrackingRange(64)
                            .updateInterval(10)
                            .build(Helpers.id(SpidersShootWebs.MOD_ID, "web_projectile").toString())
            );
}