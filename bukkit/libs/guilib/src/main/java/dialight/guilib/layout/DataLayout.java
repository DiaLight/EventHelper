package dialight.guilib.layout;

import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.StaticSlot;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class DataLayout<T> extends SlotLayout {

    protected Function<T, String> nameFunction = Object::toString;
    protected Function<T, Slot> slotFunction = d -> {
        if(d instanceof Slot) return (Slot) d;
        return new StaticSlot(new ItemStackBuilder(Material.BRICK)
                .displayName(nameFunction.apply(d))
                .build());
    };

    public void setNameFunction(Function<T, String> nameFunction) {
        this.nameFunction = nameFunction;
    }
    public void setSlotFunction(Function<T, Slot> slotFunction) {
        this.slotFunction = slotFunction;
    }

    public abstract boolean add(@NotNull T data);
    public abstract boolean remove(@NotNull T data);
    public abstract boolean update(@NotNull T data);
    @Nullable public abstract T remove(int x, int y);
    @Nullable public abstract T getData(int x, int y);

}
