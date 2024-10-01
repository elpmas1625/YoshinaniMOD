package com.yoshinani.shopButton;

import com.robertx22.mine_and_slash.saveclasses.item_classes.GearItemData;
import com.robertx22.mine_and_slash.uncommon.datasaving.StackSaving;
import com.yoshinani.customTrader.CustomChestMenu;
import com.yoshinani.money.Money;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SellButton implements ShopButton {
    private static final Map<String, Integer> RARITY_PRICE_MAP = new HashMap<String, Integer>() {
        {
            put("common", 100);
            put("uncommon", 200);
            put("rare", 300);
            put("epic", 400);
            put("legendary", 500);
            put("mythic", 600);
            put("unique", 700);
            put("runeword", 800);
        }
    };
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
            GearItemData data = StackSaving.GEARS.loadFrom(stack);
            if (!stack.isEmpty() && data != null) {
                sellingPrice += stack.getCount() * RARITY_PRICE_MAP.get(data.rar);;
                parent.container.setItem(i, ItemStack.EMPTY);
            }
        }
        if (sellingPrice != 0) {
            Money.setPlayerMoney(parent.player, Money.getPlayerMoney(parent.player) + (long) sellingPrice);
            parent.updateMoney();
        }
    }
}
