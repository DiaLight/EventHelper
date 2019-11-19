package dialight.guilib.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocSlot {

    private final Vec2i layoutPos;
    private final Slot slot;

    public LocSlot(Vec2i layoutPos, Slot slot) {
        this.layoutPos = layoutPos;
        this.slot = slot;
    }

    @Nullable public Vec2i getLayoutPos() {
        return layoutPos;
    }

    @NotNull public Slot getSlot() {
        return slot;
    }

}
