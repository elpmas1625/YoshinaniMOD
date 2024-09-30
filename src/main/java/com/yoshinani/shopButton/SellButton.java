package com.yoshinani.shopButton;

import com.yoshinani.customTrader.CustomChestMenu;
import com.yoshinani.money.Money;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class SellButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;

    public SellButton(String pItemId, int pSlotId, String pDisplayName) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
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
        int sellingPrice = 0;
        for (int i = 0; i < 45; i++) {
            ItemStack stack = parent.container.getItem(i);
            if (!stack.isEmpty()) {
                sellingPrice += stack.getCount() * 100;
                parent.container.setItem(i, ItemStack.EMPTY);
            }
        }
        if (sellingPrice != 0) {
            Money.setPlayerMoney(parent.player, Money.getPlayerMoney(parent.player) + (long) sellingPrice);
            parent.updateMoney();
        }
    }
}
