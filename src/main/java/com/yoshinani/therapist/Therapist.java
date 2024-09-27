package com.yoshinani.therapist;

import com.yoshinani.money.Money;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.*;

public class Therapist {
    private static final Map<UUID, ArrayList<ItemStack>> itemStore = new HashMap<>();

    public static void AddInsuredTooltip(ItemTooltipEvent event) {
        List<Component> tooltipList = event.getToolTip();
        CompoundTag itemTag = event.getItemStack().getTag();
        if (itemTag != null && itemTag.contains("insured")) {
            tooltipList.add(Component.empty().append("Insured").withStyle(ChatFormatting.GOLD));
        }
    }

    public static void AddInsuredItem(ServerPlayer player) {
        long count = 0;
        for (ItemStack item: player.inventoryMenu.getItems()) {
            CompoundTag itemTag = item.getTag();
            if (itemTag == null || !itemTag.contains("insured")) {
                item.addTagElement("insured", StringTag.valueOf(player.getUUID().toString()));
                count += 1;
            }
        }
        Money.setPlayerMoney(player, Money.getClientMoney() - 100L * count);
    }

    public static void InsureItemStore(ServerPlayer player) {
        NonNullList<ItemStack> items = player.inventoryMenu.getItems();
        for (int i = items.size() - 1; 0 <= i; i--) {
            ItemStack item = items.get(i);
            CompoundTag itemTag = item.getTag();
            if (itemTag != null && itemTag.contains("insured")) {
                item.removeTagKey("insured");
                itemStore.compute(player.getUUID(), (k, v) -> {
                    if (v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(item.copy());
                    return v;
                });
                player.inventoryMenu.setItem(i, 0, ItemStack.EMPTY);
            }
        }
    }

    public static void GiveInsuredItem(ServerPlayer player) {
        for (ItemStack item: itemStore.getOrDefault(player.getUUID(), new ArrayList<>())) {
            player.getInventory().add(item);
        }
        itemStore.put(player.getUUID(), new ArrayList<>());
    }
}
