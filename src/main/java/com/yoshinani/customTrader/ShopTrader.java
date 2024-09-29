package com.yoshinani.customTrader;

import com.yoshinani.loadyaml.LoadYAML;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class ShopTrader extends CustomTrader {
    public ShopTrader(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.level().isClientSide()) {
            SimpleContainer container = new SimpleContainer(54);

            Map<Integer, String> items = LoadYAML.MasterYAML.get("buy_page1");
            for (Map.Entry<Integer, String> entry : items.entrySet()) {
                int slot = entry.getKey();
                String itemID = entry.getValue();

                ItemStack itemStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemID))), 1);
                container.setItem(slot, itemStack);
            }
            MenuProvider menuProvider = new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> new ChestMenu(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6),
                    Component.literal("Large Chest")
            );
            NetworkHooks.openScreen((ServerPlayer) player, menuProvider);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
