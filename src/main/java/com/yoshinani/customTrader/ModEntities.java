package com.yoshinani.customTrader;

import com.yoshinani.yoshinanimod.YoshinaniMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = YoshinaniMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YoshinaniMod.MODID);

    public static final RegistryObject<EntityType<TheraistTrader>> THERAPIST_TRADER = ENTITY_TYPES.register("therapist_trader",
        () -> EntityType.Builder.of(TheraistTrader::new, MobCategory.MISC)
            .sized(0.6F, 1.95F)
            .build("therapist_trader"));

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> {
            helper.register(THERAPIST_TRADER.getId(), THERAPIST_TRADER.get());
        });
    }
}
