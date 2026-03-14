package com.vomiter.spidersshootwebs;

import net.minecraft.resources.ResourceLocation;

public class Helpers {
    public static ResourceLocation id(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation id(String path){
        return id(SpidersShootWebs.MOD_ID, path);
    }
    public static ResourceLocation minecraftId(String path){
        return id("minecraft", path);
    }

}
