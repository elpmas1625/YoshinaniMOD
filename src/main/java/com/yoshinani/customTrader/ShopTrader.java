package com.yoshinani.customTrader;

import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.NotNull;

public class ShopTrader extends CustomTrader {
    public ShopTrader(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.level().isClientSide()) {
            SimpleContainer container = new SimpleContainer(54);

            // ここでダイヤモンドを最初のスロットに追加
            container.setItem(0, new ItemStack(Items.DIAMOND, 1));

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
