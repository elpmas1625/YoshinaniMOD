package com.yoshinani.shopButton;

public class SellButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;

    public SellButton(String pItemId, int pSlotId, String pDisplayName) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public int getSlotId() {
        return slotId;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
