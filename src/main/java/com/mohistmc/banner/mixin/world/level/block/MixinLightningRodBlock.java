package com.mohistmc.banner.mixin.world.level.block;

import com.mohistmc.banner.bukkit.DistValidate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class MixinLightningRodBlock {

    @Inject(method = "onLightningStrike", cancellable = true, at = @At("HEAD"))
    private void banner$redstoneChange(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        boolean powered = state.getValue(LightningRodBlock.POWERED);
        int old = (powered) ? 15 : 0;
        int current = (!powered) ? 15 : 0;

        BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(CraftBlock.at(level, pos), old, current);
        Bukkit.getPluginManager().callEvent(eventRedstone);

        if (eventRedstone.getNewCurrent() <= 0) {
            ci.cancel();
        }
    }

    @Redirect(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean banner$strikeReason(Level level, Entity entity) {
        if (!DistValidate.isValid(level)) return level.addFreshEntity(entity);
        ((ServerLevel) level).strikeLightning((LightningBolt) entity, LightningStrikeEvent.Cause.TRIDENT);
        return true;
    }
}
