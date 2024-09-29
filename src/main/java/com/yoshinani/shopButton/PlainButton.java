package com.yoshinani.shopButton;

public class PlainButton implements ShopButton {
    public int slotId;
    public String displayName;

    public PlainButton(int pSlotId, String pDisplayName) {
        slotId = pSlotId;
        displayName = pDisplayName;
    }
}
