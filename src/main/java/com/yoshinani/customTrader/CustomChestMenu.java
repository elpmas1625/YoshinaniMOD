package com.yoshinani.customTrader;

import com.yoshinani.loadyaml.LoadYAML;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;

import static com.yoshinani.money.Money.setPlayerMoney;

public class CustomChestMenu extends ChestMenu {
    private final Container container;
    private String page = "mode";

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
//                player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1.0F, 1.0F);

                setPage("buy_page1");
                switch (page){
                    case "mode":
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setPage(String p){
//        page = p
        Map<Integer, String> items = LoadYAML.MasterYAML.get(p);

        container.clearContent();
        for (Map.Entry<Integer, String> entry : items.entrySet()) {
            int slot = entry.getKey();
            String itemID = entry.getValue();
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemID))), 1);
            container.setItem(slot, itemStack);
        }
    }

}