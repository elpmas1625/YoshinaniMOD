package com.yoshinani.shopButton;

import com.yoshinani.customTrader.CustomChestMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class SelectButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;
    public int price;

    public SelectButton(String pItemId, int pSlotId, String pDisplayName, int pPrice) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
        price = pPrice;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public int getSlotId() {
        return slotId;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId))), 1);
        if (Objects.equals(displayName, "null")) {
            displayName = itemStack.getDisplayName().getString();
        }
        itemStack.setHoverName(Component.nullToEmpty(displayName + ": " + price + "円"));
        return itemStack;
    }

    @Override
    public void clicked(CustomChestMenu parent) {
        parent.selectedItemId = itemId;
        parent.selectedItemAmount = 1;
        parent.selectedItemPrice = price;
        parent.setPage("buy");
    }
}
