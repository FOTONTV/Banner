package com.mohistmc.banner.mixin.world.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.craftbukkit.v1_19_R3.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
public class MixinBee_GrowCropGoal {

    @SuppressWarnings("target") @Shadow(aliases = {"field_20373"}, remap = false)
    private Bee outerThis;

    @Inject(method = "tick", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    private void banner$entityChangeBlock(CallbackInfo ci, int i, BlockPos blockPos, BlockState blockState, Block block, boolean flag, IntegerProperty property) {
        if (CraftEventFactory.callEntityChangeBlockEvent(outerThis, blockPos, blockState.setValue(property, blockState.getValue(property) + 1)).isCancelled()) {
            ci.cancel();
        }
    }
}
