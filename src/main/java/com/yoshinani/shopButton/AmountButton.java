package com.yoshinani.shopButton;

import com.yoshinani.customTrader.CustomChestMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class AmountButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;
    public String sign;
    public int amount;
    public int maxStackSize;

    public AmountButton(String pItemId, int pSlotId, String pDisplayName, String pSign, int pAmount) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
        sign = pSign;
        amount = pAmount;
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
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId))), amount);
        if (Objects.equals(displayName, "null")) {
            displayName = itemStack.getDisplayName().getString();
        }
        itemStack.setHoverName(Component.nullToEmpty(displayName));
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        assert item != null;
        maxStackSize = item.getMaxStackSize(itemStack);
        return itemStack;
    }

    @Override
    public void clicked(CustomChestMenu parent) {
        if (sign.equals("plus")) {
            parent.selectedItemAmount += amount;
        } else {
            parent.selectedItemAmount -= amount;
        }
        if (parent.selectedItemAmount > maxStackSize) {
            parent.selectedItemAmount = maxStackSize;
        } else if (parent.selectedItemAmount < 1) {
            parent.selectedItemAmount = 1;
        }
        parent.selectedItemStack.setCount(parent.selectedItemAmount);
        parent.selectedItemStack.setHoverName(Component.nullToEmpty(parent.selectedItemPrice + " x " + parent.selectedItemAmount + ": " + parent.selectedItemPrice * parent.selectedItemAmount));
    }
}
