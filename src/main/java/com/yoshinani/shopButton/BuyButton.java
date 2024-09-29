package com.yoshinani.shopButton;

public class BuyButton implements ShopButton {
    public int slotId;
    public String displayName;

    public BuyButton(int pSlotId, String pDisplayName) {
        slotId = pSlotId;
        displayName = pDisplayName;
    }
}
