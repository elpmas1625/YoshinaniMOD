package com.yoshinani.customTrader;

import com.yoshinani.yoshinanimod.YoshinaniMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;

public class ModScreens {
    public static void init(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ModScreens.class);
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init event) {
        // Register your screen here if needed
    }

    public static void openTherapistTraderScreen(Player player) {
        SimpleContainer container = new SimpleContainer(54); // ラージチェストのサイズ
        ChestMenu menu = ChestMenu.threeRows(0, player.getInventory(), container);
        Minecraft.getInstance().setScreen(new TherapistTraderScreen(menu, player.getInventory(), Component.literal("Therapist Trader")));
    }
}
