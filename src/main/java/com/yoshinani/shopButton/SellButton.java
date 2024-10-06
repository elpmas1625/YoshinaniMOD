package com.yoshinani.shopButton;

import com.robertx22.mine_and_slash.saveclasses.item_classes.GearItemData;
import com.robertx22.mine_and_slash.saveclasses.jewel.JewelItemData;
import com.robertx22.mine_and_slash.saveclasses.skill_gem.SkillGemData;
import com.robertx22.mine_and_slash.uncommon.datasaving.StackSaving;
import com.yoshinani.customTrader.CustomChestMenu;
import com.yoshinani.env.CustomEnv;
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
        if (Objects.equals(displayName, "null")) {
            displayName = itemStack.getDisplayName().getString();
        }
        itemStack.setHoverName(Component.nullToEmpty(displayName));
        return itemStack;
    }

    @Override
    public void clicked(CustomChestMenu parent) {
        int sellingPrice = 0;
        for (int i = 0; i < 45; i++) {
            ItemStack stack = parent.container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            GearItemData gearData = StackSaving.GEARS.loadFrom(stack);
            if (gearData != null) {
                sellingPrice += stack.getCount() * Integer.parseInt(CustomEnv.data.get("SELL_PRICE_" + gearData.rar));
                parent.container.setItem(i, ItemStack.EMPTY);
            }
            SkillGemData gemData = StackSaving.SKILL_GEM.loadFrom(stack);
            if (gemData != null) {
                sellingPrice += stack.getCount() * Integer.parseInt(CustomEnv.data.get("SELL_PRICE_" + gemData.rar));
                parent.container.setItem(i, ItemStack.EMPTY);
            }
            JewelItemData jewelData = StackSaving.JEWEL.loadFrom(stack);
            if (jewelData != null) {
                sellingPrice += stack.getCount() * Integer.parseInt(CustomEnv.data.get("SELL_PRICE_" + jewelData.rar));
                parent.container.setItem(i, ItemStack.EMPTY);
            }
        }
        if (sellingPrice != 0) {
            Money.setPlayerMoney(parent.player, Money.getPlayerMoney(parent.player) + (long) sellingPrice);
            parent.updateMoney();
        }
    }
}
