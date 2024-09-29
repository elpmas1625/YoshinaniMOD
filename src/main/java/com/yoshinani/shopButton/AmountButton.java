package com.yoshinani.shopButton;

public class AmountButton implements ShopButton {
    public int slotId;
    public String displayName;
    public int sign;
    public int amount;

    public AmountButton(int pSlotId, String pDisplayName, int pSign, int pAmount) {
        slotId = pSlotId;
        displayName = pDisplayName;
        sign = pSign;
        amount = pAmount;
    }
}
