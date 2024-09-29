package com.yoshinani.shopButton;

import com.yoshinani.customTrader.CustomChestMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TransitionButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;
    public String next;

    public TransitionButton(String pItemId, int pSlotId, String pDisplayName, String pNext) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
        next = pNext;
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
        itemStack.setHoverName(Component.nullToEmpty(displayName));

        return itemStack;
    }

    @Override
    public void clicked(CustomChestMenu parent) {
        parent.setPage(next);
    }
}
