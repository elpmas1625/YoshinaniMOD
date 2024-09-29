package com.yoshinani.shopButton;

import com.yoshinani.customTrader.CustomChestMenu;
import net.minecraft.world.item.ItemStack;

public interface ShopButton {
    public String getItemId();
    public int getSlotId();
    public String getDisplayName();

    public ItemStack createItemStack();
    public void clicked(CustomChestMenu parent);
}
