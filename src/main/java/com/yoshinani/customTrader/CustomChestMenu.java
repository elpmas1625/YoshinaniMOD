package com.yoshinani.customTrader;

import com.yoshinani.loadyaml.LoadYAML;
import com.yoshinani.shopButton.ShopButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    public String page = "mode";
    public String selectedItemId = "minecraft:error";
    public int selectedItemAmount = 1;

    public CustomChestMenu(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, int pRows) {
        super(pType, pContainerId, pPlayerInventory, pContainer, pRows);

        container = pContainer;
        setPage("mode");
    }

    @Override
    public void clicked(int slotId, int buttonId, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < this.slots.size()) {
            ItemStack clickedStack = this.getSlot(slotId).getItem();
            if (!clickedStack.isEmpty()) {
                player.sendSystemMessage(Component.literal("選択したアイテム: " + clickedStack.getHoverName().getString()));
                player.playNotifySound(SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                if (items.containsKey(slotId)) {
                    items.get(slotId).clicked(this);
                }
            }
        }
    }

    public void setPage(String p){
        page = p;
        items = LoadYAML.MasterYAML.get(page);

        container.clearContent();
        for (Map.Entry<Integer, ShopButton> entry : items.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue().createItemStack();
            container.setItem(slot, itemStack);
        }

        // buyページの場合、選択中のアイテムを表示
        if (page.equals("buy")) {
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(selectedItemId))), selectedItemAmount);
            container.setItem(13, itemStack);
        }
    }
}
