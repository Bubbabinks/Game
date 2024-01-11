package game.inventory;

import java.io.Serializable;

public class ItemStack implements Serializable {

    public static final int MAX_STACK_COUNT = 128;

    private ItemType type;
    private int amount;

    public ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ItemType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int addAmount(int add) {
        amount += add;
        if (amount > MAX_STACK_COUNT) {
            int r = amount - MAX_STACK_COUNT;
            amount -= r;
            return r;
        }
        return 0;
    }

    public int removeAmount(int i) {
        return amount-=i;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
