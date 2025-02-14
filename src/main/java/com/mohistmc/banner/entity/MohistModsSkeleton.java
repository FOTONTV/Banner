package com.mohistmc.banner.entity;

import com.mohistmc.banner.api.EntityAPI;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftAbstractSkeleton;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.jetbrains.annotations.NotNull;

public class MohistModsSkeleton extends CraftAbstractSkeleton {

    public String entityName;

    public MohistModsSkeleton(CraftServer server, net.minecraft.world.entity.monster.AbstractSkeleton entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    public @NotNull Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.MOD;
    }

    @NotNull
    @Override
    public EntityType getType() {
        return EntityAPI.entityType(entityName);
    }

    @Override
    public String toString() {
        return "MohistModsSkeleton{" + entityName + '}';
    }
}