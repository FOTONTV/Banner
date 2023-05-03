package com.mohistmc.banner.mixin.world.entity.animal.horse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_19_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class MixinAbstractHorse extends Animal {

    public int maxDomestication;

    protected MixinAbstractHorse(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void banner$init(EntityType<? extends AbstractHorse> type, Level worldIn, CallbackInfo ci) {
        this.maxDomestication = 100;
    }

    @Redirect(method = "createInventory", at = @At(value = "NEW", target = "net/minecraft/world/SimpleContainer"))
    private SimpleContainer banner$createInv(int slots) {
        SimpleContainer inventory = new SimpleContainer(slots);
        inventory.setOwner((InventoryHolder) this.getBukkitEntity());
        return inventory;
    }

    @Inject(method = "handleEating", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;heal(F)V"))
    private void banner$healByEating(Player player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        pushHealReason(EntityRegainHealthEvent.RegainReason.EATING);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;heal(F)V"))
    private void banner$healByRegen(CallbackInfo ci) {
        pushHealReason(EntityRegainHealthEvent.RegainReason.REGEN);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void banner$writeTemper(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("Bukkit.MaxDomestication", this.maxDomestication);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void banner$readTemper(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("Bukkit.MaxDomestication")) {
            this.maxDomestication = compound.getInt("Bukkit.MaxDomestication");
        }
    }

    @Inject(method = "handleStartJump", cancellable = true, at = @At("HEAD"))
    private void banner$horseJump(int i, CallbackInfo ci) {
        float power;
        if (i >= 90) {
            power = 1.0F;
        } else {
            power = 0.4F + 0.4F * (float) i / 90.0F;
        }
        HorseJumpEvent event = CraftEventFactory.callHorseJumpEvent((AbstractHorse) (Object) this, power);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    /**
     * @author wdog5
     * @reason
     */
    @Overwrite
    public int getMaxTemper() {
        return maxDomestication;
    }
}
