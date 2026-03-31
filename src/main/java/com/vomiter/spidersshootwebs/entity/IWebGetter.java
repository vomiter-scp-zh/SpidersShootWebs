package com.vomiter.spidersshootwebs.entity;

import net.minecraft.world.entity.Mob;

public interface IWebGetter {
    default WebProjectile getWeb(){
        if(this instanceof Mob mob){
            return new WebProjectile(mob.level(), mob);
        }
        return null;
    };
}
