package dialight.guilib.slot;

import dialight.guilib.elements.DataElement;

public class DataSlotUsage implements SlotUsage {

    private final DataElement layout;
    private final Object data;

    public DataSlotUsage(DataElement layout, Object data) {
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
