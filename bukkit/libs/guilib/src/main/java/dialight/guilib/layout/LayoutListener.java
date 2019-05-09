package dialight.guilib.layout;

import dialight.guilib.slot.Slot;
import org.jetbrains.annotations.Nullable;

public interface LayoutListener {

    void updateSlot(int x, int y, @Nullable Slot slot);
    void updateDataBounds(int width, int height);
    void refresh();

}
