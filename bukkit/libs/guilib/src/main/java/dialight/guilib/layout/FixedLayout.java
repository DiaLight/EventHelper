package dialight.guilib.layout;

import dialight.guilib.slot.Slot;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FixedLayout extends DataLayout<Slot> {

    private final Slot[][] slots;

    public FixedLayout(int width, int height) {
        this.slots = new Slot[width][height];
    }

    @Override public int getWidth() {
        return slots.length;
    }
    @Override public int getHeight() {
        return slots[0].length;
    }

    @Override public boolean add(@NotNull Slot data) {
        throw new NotImplementedException();
    }
    @Override public boolean remove(@NotNull Slot data) {
        throw new NotImplementedException();
    }
    @Override public boolean update(@NotNull Slot data) {
        throw new NotImplementedException();
    }
    @Override public void clear() { throw new NotImplementedException(); }

    @Override public Slot getSlot(int x, int y) {
        return slots[x][y];
    }

    @Nullable @Override public Slot remove(int x, int y) {
        return null;
    }

    @Nullable @Override public Slot getData(int x, int y) {
        return slots[x][y];
    }

    public Slot setSlot(int x, int y, Slot slot) {
        Slot oldslot = slots[x][y];
        slots[x][y] = slot;
        fireUpdateSlot(x, y, slot);
        return oldslot;
    }

}
