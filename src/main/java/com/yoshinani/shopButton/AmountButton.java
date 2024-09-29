package com.yoshinani.shopButton;

public class AmountButton implements ShopButton {
    public String itemId;
    public int slotId;
    public String displayName;
    public String sign;
    public int amount;

    public AmountButton(String pItemId, int pSlotId, String pDisplayName, String pSign, int pAmount) {
        itemId = pItemId;
        slotId = pSlotId;
        displayName = pDisplayName;
        sign = pSign;
        amount = pAmount;
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
