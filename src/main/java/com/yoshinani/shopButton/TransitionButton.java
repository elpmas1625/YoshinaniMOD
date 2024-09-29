package com.yoshinani.shopButton;

public class TransitionButton implements ShopButton {
    public int slotId;
    public String displayName;
    public String next;

    public TransitionButton(int pSlotId, String pDisplayName, String pNext) {
        slotId = pSlotId;
        displayName = pDisplayName;
        next = pNext;
    }
}
