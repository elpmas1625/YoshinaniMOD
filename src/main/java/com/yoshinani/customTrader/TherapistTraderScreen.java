package com.yoshinani.customTrader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class TherapistTraderScreen extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("yoshinanimod", "textures/gui/therapist_trader.png");

    private final ChestMenu menu;
    private final Inventory inventory;

    protected TherapistTraderScreen(ChestMenu menu, Inventory inventory, Component title) {
        super(title);
        this.menu = menu;
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (this.width - 176) / 2;
        int y = (this.height - 166) / 2;
        guiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, 176, 166);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
