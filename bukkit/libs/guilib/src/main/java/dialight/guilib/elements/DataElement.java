package dialight.guilib.elements;

import dialight.guilib.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class DataElement<T> extends SlotElement {

    protected Function<T, String> nameFunction = Object::toString;
    protected Function<T, Slot> slotFunction = d -> null;

    public void setNameFunction(Function<T, String> nameFunction) {
        this.nameFunction = nameFunction;
    }
    public void setSlotFunction(Function<T, Slot> slotFunction) {
        this.slotFunction = slotFunction;
    }

    public abstract boolean add(@NotNull T data);
    public abstract boolean remove(@NotNull T data);
    public abstract boolean update(@NotNull T data);
    public abstract int getSize();
    public abstract void clear();
    @Nullable public abstract T remove(int x, int y);
    @Nullable public abstract T getData(int x, int y);

    public boolean isEmpty() {
        return getSize() == 0;
    }

}
