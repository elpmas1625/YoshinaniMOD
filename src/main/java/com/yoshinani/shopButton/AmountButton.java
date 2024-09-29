package com.yoshinani.shopButton;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class AmountButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;
    public String sign;
    public int amount;

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
        itemStack.setHoverName(Component.nullToEmpty(displayName));
        return itemStack;
    }
}
