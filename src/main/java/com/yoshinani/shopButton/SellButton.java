package com.yoshinani.shopButton;

public class SellButton implements ShopButton {
    public int slotId;
    public String displayName;

    public SellButton(int pSlotId, String pDisplayName) {
        slotId = pSlotId;
        displayName = pDisplayName;
    }
}
