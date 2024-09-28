package com.yoshinani.customTrader;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TheraistTrader extends CustomTrader {
    public TheraistTrader(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (player.level().isClientSide) {
            ModScreens.openTherapistTraderScreen(player);
            return InteractionResult.SUCCESS;
        }

        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            player.displayClientMessage(Component.literal("Hello, I am the Therapist Trader!"), false);
            this.playSound(SoundEvents.VILLAGER_YES, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }
}
