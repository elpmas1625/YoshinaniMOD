package com.yoshinani.customTrader;

import com.yoshinani.loadyaml.LoadYAML;
import com.yoshinani.shopButton.ShopButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;

public class CustomChestMenu extends ChestMenu {
    private final Container container;
    private Map<Integer, ShopButton> items;
    public StringBuilder page;
    public String selectedItemId = "minecraft:error";
    public int selectedItemAmount = -1;
    public int selectedItemPrice = -1;
    public ItemStack selectedItemStack;
    public ServerPlayer player;

    public CustomChestMenu(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, int pRows, StringBuilder pPage) {
        super(pType, pContainerId, pPlayerInventory, pContainer, pRows);

        container = pContainer;
        page = pPage;
        setPage(page.toString());
    }

    @Override
    public void clicked(int slotId, int buttonId, ClickType clickType, Player player) {
        this.player = (ServerPlayer) player;
        if (slotId >= 0 && slotId < this.slots.size()) {
            ItemStack clickedStack = this.getSlot(slotId).getItem();

            if ((Objects.equals(page.toString(), "sell") && (slotId < 45 || 53 < slotId))) {
                super.clicked(slotId, buttonId, clickType, player);
                return;
            }

            if (!clickedStack.isEmpty()) {
                player.playNotifySound(SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                if (items.containsKey(slotId)) {
                    items.get(slotId).clicked(this);
                }
            }
        }
    }

    public void setPage(String p) {
        page.setLength(0);
        page.append(p);
        items = LoadYAML.MasterYAML.get(page.toString());

        container.clearContent();
        for (Map.Entry<Integer, ShopButton> entry : items.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue().createItemStack();
            container.setItem(slot, itemStack);
        }

        // buyページの場合、選択中のアイテムを表示
        if (page.toString().equals("buy")) {
            selectedItemStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(selectedItemId))), selectedItemAmount);
            selectedItemStack.setHoverName(Component.nullToEmpty(selectedItemPrice + " x " + selectedItemAmount + ": " + selectedItemPrice * selectedItemAmount));
            container.setItem(13, selectedItemStack);
        }
    }
}
