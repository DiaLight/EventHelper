package dialight.guilib;

import dialight.guilib.slot.Slot;
import org.jetbrains.annotations.Nullable;

public interface UpdateListener {

    void updateSlot(int x, int y, @Nullable Slot slot);
    void updateDataBounds(int width, int height);
    void refresh();

}
