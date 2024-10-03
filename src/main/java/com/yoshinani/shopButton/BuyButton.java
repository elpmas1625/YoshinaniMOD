package com.yoshinani.shopButton;

import com.robertx22.mine_and_slash.capability.entity.EntityData;
import com.robertx22.mine_and_slash.database.data.rarities.GearRarity;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.loot.LootInfo;
import com.robertx22.mine_and_slash.loot.blueprints.MapBlueprint;
import com.robertx22.mine_and_slash.loot.blueprints.bases.GearRarityPart;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.PlayerUtils;
import com.robertx22.mine_and_slash.vanilla_mc.commands.giveitems.GiveMap;
import com.yoshinani.customTrader.CustomChestMenu;
import com.yoshinani.money.Money;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import com.robertx22.mine_and_slash.mmorpg.registers.common.SlashItemTags;


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

        // mapのランダム生成
        if (Objects.equals(parent.selectedItemId, "mmorpg:map")){
            Money.setPlayerMoney(parent.player, playerMoney - (long) parent.selectedItemPrice * parent.selectedItemAmount);

            for (int i = 0; i < parent.selectedItemAmount; ++i) {
                MapBlueprint blueprint = new MapBlueprint(LootInfo.ofLevel(Load.Unit(parent.player).getLevel()));
                blueprint.level.set(Load.Unit(parent.player).getLevel());
                PlayerUtils.giveItem(blueprint.createStack(), parent.player);
            }
        }else {
            // お金を減らしアイテムをプレイヤーに追加
            Money.setPlayerMoney(parent.player, playerMoney - (long) parent.selectedItemPrice * parent.selectedItemAmount);
            parent.player.addItem(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(parent.selectedItemId))), parent.selectedItemAmount));
        }
        parent.setPage("buy_page1");
    }
}
