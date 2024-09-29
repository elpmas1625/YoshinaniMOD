package com.yoshinani.shopButton;

public class SelectButton implements ShopButton {
    public int slotId;
    public String displayName;

    public SelectButton(int pSlotId, String pDisplayName) {
        slotId = pSlotId;
        displayName = pDisplayName;
    }
}
