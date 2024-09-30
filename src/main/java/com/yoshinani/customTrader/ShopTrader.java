package com.yoshinani.customTrader;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ShopTrader extends CustomTrader {
    public ShopTrader(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.level().isClientSide()) {
            StringBuilder page = new StringBuilder("mode");
            SimpleContainer container = new SimpleContainer(54){
                @Override
                public void stopOpen(@NotNull Player player) {
                    super.stopOpen(player);
                    if(Objects.equals(page.toString(), "sell")){
                        if (player instanceof ServerPlayer serverPlayer) {
                            for (int i = 0; i < 45; i++) {
                                ItemStack stack = this.getItem(i);
                                if (!stack.isEmpty()) {
                                    Containers.dropItemStack(serverPlayer.level(), serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), stack);
                                }
                            }
                        }
                    }
                }
            };

            MenuProvider menuProvider = new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> new CustomChestMenu(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6, page, (ServerPlayer) player),
                    Component.literal("よしなちゃんのショップ")
            );

            NetworkHooks.openScreen((ServerPlayer) player, menuProvider);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}