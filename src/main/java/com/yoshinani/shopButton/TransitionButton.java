package com.yoshinani.shopButton;

public class TransitionButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;
    public String next;

    public TransitionButton(String pItemId, int pSlotId, String pDisplayName, String pNext) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
        next = pNext;
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
