package dialight.guilib.view;

import dialight.guilib.gui.Gui;
import dialight.guilib.elements.SlotElement;
import dialight.guilib.slot.LocSlot;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.Vec2i;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * It's just horizontal scroll view,
 * with left/right movement in SlotLayout.
 *
 * [ X _ _ _ _ _ _ _ X ]
 * [ X _ _ _ _ _ _ _ X ]
 * [ L _ _ _ _ _ _ _ R ]
 * [ L _ _ _ _ _ _ _ R ]
 * [ X _ _ _ _ _ _ _ X ]
 * [ X _ _ _ _ _ _ _ X ]
 *
 * L - Backward button
 * R - Forward button
 * X - Background
 * _ - SlotLayout content
 *
 */
public abstract class Scroll7x6View<G extends Gui, L extends SlotElement> extends ScrollView<G, L> {

    private final int width = 7;
    private final int height = 6;
    private final Slot[] leftPane = new Slot[6];
    private final Slot[] rightPane = new Slot[6];
    private int offset = 0;
    protected String prefix;

    public Scroll7x6View(G gui, L layout, String title) {
        super(gui, layout, 9, 6, title);
        this.prefix = title;
    }

    protected int calcLimit() {
        int limit = getLayout().getWidth() - 1;
        if(limit < 0) limit = 0;
        return limit;
    }

    @Override public void moveBackward(int dx) {
        int oldValue = offset;
        offset -= dx;
        if(offset < 0) {
            offset = 0;
            if(offset != oldValue) {
                refresh();
            }
        } else {
            refresh();
        }
    }

    @Override public void moveForward(int dx) {
        int oldValue = offset;
        offset += dx;
        int limit = calcLimit();
        if(offset > limit) {
            offset = limit;
            if(offset != oldValue) {
                refresh();
            }
        } else {
            refresh();
        }
    }

    protected abstract void updateViewPanels();

    @Override public void refresh() {
        int limit = calcLimit();
        if(offset > limit) offset = limit;
        updateViewPanels();
        SlotElement layout = getLayout();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Slot slot = layout.getSlot(x + offset, y);
                ItemStack item = null;
                if(slot != null) item = slot.createItem();
                getInventory().setItem(1 + x + y * 9, item);
            }
        }
        updateTitle();
    }

    protected void updateTitle() {
        int page = offset + 1;
        int pages = getLayout().getWidth();
        if(pages <= 1) {
            setTitle(prefix);
        } else {
            setTitle(prefix + " " + page + "/" + pages);
        }
    }

    @Override public void onOpenView(Player player) {
        refresh();
        super.onOpenView(player);
    }

    protected void setLeftPaneSlot(int y, Slot slot) {
        leftPane[y] = slot;
        getInventory().setItem(y * 9, slot.createItem());
    }

    protected void setRightPaneSlot(int y, Slot slot) {
        rightPane[y] = slot;
        getInventory().setItem(8 + y * 9, slot.createItem());
    }

    @Nullable @Override public LocSlot getSlot(Player player, int index) {
        int x = index % 9;
        int y = index / 9;
        if(x == 0) return new LocSlot(null, leftPane[y]);
        if(x == 8) return new LocSlot(null, rightPane[y]);
        SlotElement layout = getLayout();
        Vec2i pos = new Vec2i(x - 1 + offset, y);
        Slot slot = layout.getSlot(pos.x, pos.y);
        if(slot == null) return null;
        return new LocSlot(pos, slot);
    }

    @Override
    public void updateSlot(int x, int y, @Nullable Slot slot) {
        x -= offset;
        if(x < 0) return;
        if(x >= width) return;
        if(y >= height) return;
        ItemStack item = null;
        if(slot != null) item = slot.createItem();
        getInventory().setItem(x + 1 + y * 9, item);
    }

    @Override
    public void updateDataBounds(int width, int height) {
        int limit = calcLimit();
        if(offset > limit) {
            offset = limit;
            refresh();
        }
        updateTitle();
    }

    public void setTitlePrefix(@NotNull String prefix) {
        this.prefix = prefix;
        updateTitle();
    }

    @Override public int getWidth() {
        return width;
    }

    @Override public int getHeight() {
        return height;
    }

    @Override public int getOffset() {
        return offset;
    }

    public static void defaultUpdateViewPanels(Scroll7x6View view, Slot background, Slot forward, Slot backward) {
        int limit = view.calcLimit();
        for (int y = 0; y < 6; y++) {
            if((y == 2 || y == 3) && view.getOffset() != 0) {
                view.setLeftPaneSlot(y, backward);
            } else {
                view.setLeftPaneSlot(y, background);
            }
            if((y == 2 || y == 3) && view.getOffset() != limit) {
                view.setRightPaneSlot(y, forward);
            } else {
                view.setRightPaneSlot(y, background);
            }
        }
    }

}
