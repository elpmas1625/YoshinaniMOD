package com.yoshinani.customTrader;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class ShopTrader extends CustomTrader {
    public ShopTrader(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
}
