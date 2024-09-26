package com.yoshinani.money;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static com.yoshinani.yoshinanimod.YoshinaniMod.MODID;
public class Money {
    // ログ用のLogger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public static Map<UUID, Long> playerMoneyMap = new HashMap<>();
    private static final String MONEY_FILE = "player_moneys.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path configPath = null;

    // クライアント側のmoneyを保持する変数
    public static long clientMoney = 0L;

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        INSTANCE.registerMessage(0, MoneyUpdatePacket.class, MoneyUpdatePacket::encode, MoneyUpdatePacket::decode, MoneyUpdatePacket::handle);

        configPath = FMLPaths.CONFIGDIR.get().resolve(MODID);
        configPath.toFile().mkdirs();
    }

    public static class MoneyUpdatePacket {
        private final long money;

        public MoneyUpdatePacket(long money) {
            this.money = money;
        }

        public static void encode(MoneyUpdatePacket packet, FriendlyByteBuf buffer) {
            buffer.writeLong(packet.money);
        }

        public static MoneyUpdatePacket decode(FriendlyByteBuf buffer) {
            return new MoneyUpdatePacket(buffer.readLong());
        }

        public static void handle(MoneyUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                // サーバ側かクライアント側かを判定
                if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                    // クライアント側の処理
                    LOGGER.info("Received money update on client: {}", packet.money);
                    // クライアント側のmoneyを更新
                    clientMoney = packet.money;
                } else {
                    // サーバ側の処理
                    Player player = ctx.get().getSender();
                    if (player != null) {
                        playerMoneyMap.put(player.getUUID(), packet.money);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    // 以下は既存のコード
    public static void loadPlayerMoney() {
        File file = configPath.resolve(MONEY_FILE).toFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<UUID, Long>>(){}.getType();
                playerMoneyMap = GSON.fromJson(reader, type);
                LOGGER.info("Loaded player money data from {}", file.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("Failed to load player money data", e);
            }
        } else {
            LOGGER.info("No existing player money data found at {}", file.getAbsolutePath());
        }
    }

    public static void savePlayerMoney() {
        File file = configPath.resolve(MONEY_FILE).toFile();
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(playerMoneyMap, writer);
            LOGGER.info("Saved player money data to {}", file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Failed to save player money data", e);
        }
    }

    public static void setPlayerMoney(Player player, long amount) {
        playerMoneyMap.put(player.getUUID(), amount);
        if (player instanceof ServerPlayer) {
            INSTANCE.sendTo(new MoneyUpdatePacket(amount), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static long getPlayerMoney(Player player) {
        return playerMoneyMap.getOrDefault(player.getUUID(), 0L);
    }

    // クライアント側のmoneyを取得するメソッド
    public static long getClientMoney() {
        return clientMoney;
    }
}
