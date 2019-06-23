package dialight.guilib.slot;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicSlot implements Slot {

    private final List<SlotUsage> usages = new ArrayList<>();

    @Override public void attached(SlotUsage usage) {
        usages.add(usage);
    }

    @Override public void detached(SlotUsage usage) {
        usages.remove(usage);
    }

    public void update() {
        for (SlotUsage usage : usages) {
            usage.update();
        }
    }

}
