package com.yoshinani.yoshinanimod;

import com.mojang.logging.LogUtils;
import com.yoshinani.money.Money;
import com.yoshinani.therapist.Therapist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.slf4j.Logger;

import java.util.Random;

import static com.yoshinani.money.Money.*;

@Mod(YoshinaniMod.MODID)
public class YoshinaniMod {
    public static final String MODID = "yoshinanimod";
    private static final Logger LOGGER = LogUtils.getLogger();
    Random random = new Random();

    public YoshinaniMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("START onServerStarting()");

        Money.loadPlayerMoney();

        LOGGER.info("FINISH onServerStarting()");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {

        LOGGER.info("START onServerStopping()");

        Money.savePlayerMoney();

        LOGGER.info("FINISH onServerStopping()");
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.info("Player logged in: {}", event.getEntity().getName().getString());

        if (event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            long money = playerMoneyMap.getOrDefault(player.getUUID(), 0L);
            LOGGER.info("Sending money update to player: {}", money);
            INSTANCE.sendTo(new Money.MoneyUpdatePacket(money), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Money.init();
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void ItemTooltipEvent(ItemTooltipEvent event) {
        Therapist.AddInsuredTooltip(event);
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            ItemStack oakLog = new ItemStack(Items.OAK_LOG, 1);
            if (!player.getInventory().add(oakLog)) {
                player.drop(oakLog, false);
            }

            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                setPlayerMoney(serverPlayer, random.nextLong(10000));
                Therapist.AddInsuredItem(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public void livingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Therapist.InsureItemStore(player);
        }
    }

    @SubscribeEvent
    public void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Therapist.GiveInsuredItem(player);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRenderGameOverlay(ScreenEvent.Render.Post event) {
            if (event.getScreen() instanceof InventoryScreen) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                if (player != null) {
                    GuiGraphics guiGraphics = event.getGuiGraphics();
                    String moneyText = "所持金: " + String.format("%,d", getClientMoney()) + "円";
                    guiGraphics.drawString(mc.font, moneyText, 10, 10, 0xFFFFFF);
                }
            }
        }
    }
}
