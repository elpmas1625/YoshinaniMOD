package com.yoshinani.customTrader;

import com.yoshinani.env.CustomEnv;
import com.yoshinani.money.Money;
import com.yoshinani.therapist.Therapist;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class TheraistTrader extends CustomTrader {
    public TheraistTrader(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        this.setCustomName(Component.literal("Therapist"));
    }

    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.level().isClientSide) {
            SimpleContainer container = new SimpleContainer(54) {
                @Override
                public void stopOpen(@NotNull Player player) {
                    super.stopOpen(player);
                    if (player instanceof ServerPlayer serverPlayer) {
                        int cnt = 0;
                        for (int i = 0; i < this.getContainerSize(); i++) {
                            ItemStack stack = this.getItem(i);
                            if (!stack.isEmpty() && !Therapist.isInsured(stack) && !stack.getDescriptionId().equals("item.mmorpg.tp_back")) {
                                cnt++;
                            }
                        }

                        long totalFee = (long) cnt * Long.parseLong(CustomEnv.data.get("INSURE_FEE"));

                        if (totalFee >= Money.getPlayerMoney(serverPlayer)) {
                            serverPlayer.sendSystemMessage(Component.literal("お金が足りません"));
                            cnt = 0;
                        } else {
                            Money.setPlayerMoney(serverPlayer, Money.getPlayerMoney(serverPlayer) - totalFee);
                            for (int i = 0; i < this.getContainerSize(); i++) {
                                ItemStack stack = this.getItem(i);
                                if (!stack.isEmpty() && !Therapist.isInsured(stack) && !stack.getDescriptionId().equals("item.mmorpg.tp_back")) {
                                    Therapist.AddInsuredToItem(serverPlayer, stack);
                                }
                            }
                        }
                        for (int i = 0; i < this.getContainerSize(); i++) {
                            ItemStack stack = this.getItem(i);
                            if (!stack.isEmpty()) {
                                serverPlayer.addItem(stack);
                            }
                        }
                        if (cnt > 0) {
                            serverPlayer.sendSystemMessage(Component.literal("保険をかけたアイテム数: " + cnt + " 残金: " + Money.getPlayerMoney(serverPlayer)));
                            serverPlayer.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            };

            MenuProvider menuProvider = new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> new ChestMenu(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6),
                    Component.literal("保険をかけたいアイテムを入れて閉じる")
            );

            NetworkHooks.openScreen((ServerPlayer) player, menuProvider);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
