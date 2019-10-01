package net.publicvoid.Minigame.src.inventory;

public class InventorySlot {
    private Object item;
    private int numHeld;

    InventorySlot(Object item) {
        if(item != null) {
            this.item = item;
        }
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object obj) {
        item = obj;
    }

    public void setNumHeld(int numHeld) {
        this.numHeld = numHeld;
    }

    public int getNumHeld() {
        return numHeld;
    }

    public void removeItem() {
        item = null;
    }
}
