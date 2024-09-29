package com.yoshinani.shopButton;

import com.yoshinani.customTrader.CustomChestMenu;
import com.yoshinani.money.Money;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BuyButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;

    public BuyButton(String pItemId, int pSlotId, String pDisplayName) {
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
        itemStack.setHoverName(Component.nullToEmpty("購入する"));
        return itemStack;
    }

    @Override
    public void clicked(CustomChestMenu parent) {
        long playerMoney = Money.getPlayerMoney(parent.player);

        // お金が足りない場合は何もしない
        if (playerMoney < (long) parent.selectedItemPrice * parent.selectedItemAmount) {
            return;
        }

        // お金を減らしアイテムをプレイヤーに追加
        Money.setPlayerMoney(parent.player, playerMoney - (long) parent.selectedItemPrice * parent.selectedItemAmount);
        parent.player.addItem(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(parent.selectedItemId))), parent.selectedItemAmount));

        parent.setPage("buy_page1");
    }
}
