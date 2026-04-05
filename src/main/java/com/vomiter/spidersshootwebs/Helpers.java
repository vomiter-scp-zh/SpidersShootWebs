package com.vomiter.spidersshootwebs;

import net.minecraft.resources.Identifier;

public class Helpers {
    public static Identifier id(String namespace, String path){
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier id(String path){
        return id(SpidersShootWebs.MOD_ID, path);
    }
    public static Identifier minecraftId(String path){
        return id("minecraft", path);
    }

}
