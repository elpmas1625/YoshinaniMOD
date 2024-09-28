package com.yoshinani.customTrader;

import com.yoshinani.yoshinanimod.YoshinaniMod;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ShopTraderRenderer extends MobRenderer<ShopTrader, PlayerModel<ShopTrader>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(YoshinaniMod.MODID, "textures/entity/peacekeeper.png");

    public ShopTraderRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), true), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ShopTrader shopTrader) {
        return TEXTURE;
    }
}
