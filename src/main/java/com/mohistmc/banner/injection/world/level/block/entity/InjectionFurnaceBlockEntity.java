package com.mohistmc.banner.injection.world.level.block.entity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

import java.util.List;

public interface InjectionFurnaceBlockEntity {

    default List<ItemStack> getContents() {
        return null;
    }

    default void onOpen(CraftHumanEntity who) {
    }

    default void onClose(CraftHumanEntity who) {
    }

    default Object2IntOpenHashMap<ResourceLocation> getRecipesUsed() {
        return null;
    }

    default List<HumanEntity> getViewers() {
        return null;
    }

    default void setMaxStackSize(int size) {

    }

    default void awardUsedRecipesAndPopExperience(ServerPlayer entityplayer, ItemStack itemstack, int amount) { // CraftBukkit
    }

    default List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel worldserver, Vec3 vec3d, BlockPos blockposition, ServerPlayer entityplayer, ItemStack itemstack, int amount) {
        return null;
    }
}
