package dialight.guilib.slot;

import dialight.guilib.layout.DataLayout;

public class DataSlotUsage implements SlotUsage {

    private final DataLayout layout;
    private final Object data;

    public DataSlotUsage(DataLayout layout, Object data) {
        this.layout = layout;
        this.data = data;
    }

    @Override
    public void update() {
        layout.update(data);
    }

    public Object getData() {
        return this.data;
    }
}
