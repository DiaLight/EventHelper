package dialight.guilib.view;

import dialight.guilib.layout.SlotLayout;
import dialight.guilib.gui.Gui;
import dialight.guilib.slot.LocSlot;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.Vec2i;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Fixed9x6View extends View {

    private final int width;
    private final int height;

    public Fixed9x6View(Gui gui, SlotLayout layout, String title) {
        super(gui, layout, 9, 6, title);
        this.width = 9;
        this.height = 6;
    }

    @Nullable @Override public LocSlot getSlot(Player player, int index) {
        int x = index % width;
        int y = index / width;
        Slot slot = getLayout().getSlot(x, y);
        if(slot == null) return null;
        return new LocSlot(
                new Vec2i(x, y),
                slot
        );
    }

    @Override
    public void updateSlot(int x, int y, @Nullable Slot slot) {
        ItemStack item = null;
        if(slot != null) item = slot.createItem();
        getInventory().setItem(x + y * width, item);
    }

    @Override
    public void updateDataBounds(int width, int height) {

    }

    @Override
    public void refresh() {

    }

}
