package com.mohistmc.banner.mixin.world.inventory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventoryMerchant;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventoryView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantMenu.class)
public abstract class MixinMerchantMenu extends AbstractContainerMenu {

    // @formatter:off
    @Shadow @Final private Merchant trader;
    @Shadow @Final private MerchantContainer tradeContainer;
    // @formatter:on

    private CraftInventoryView bukkitEntity = null;
    private Inventory playerInventory;

    protected MixinMerchantMenu(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/trading/Merchant;)V", at = @At("RETURN"))
    public void banner$init(int id, Inventory playerInventoryIn, Merchant merchantIn, CallbackInfo ci) {
        this.playerInventory = playerInventoryIn;
    }

    @Inject(method = "playTradeSound", cancellable = true, at = @At("HEAD"))
    public void banner$returnIfFail(CallbackInfo ci) {
        if (!(this.trader instanceof Entity)) {
            ci.cancel();
        }
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity == null) {
            bukkitEntity = new CraftInventoryView(this.playerInventory.player.getBukkitEntity(), new CraftInventoryMerchant(this.trader, this.tradeContainer), (AbstractContainerMenu) (Object) this);
        }
        return bukkitEntity;
    }
}
