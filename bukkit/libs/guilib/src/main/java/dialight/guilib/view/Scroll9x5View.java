package dialight.guilib.view;

import dialight.guilib.gui.Gui;
import dialight.guilib.layout.SlotLayout;
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
 * [ _ _ _ _ _ _ _ _ _ ]
 * [ _ _ _ _ _ _ _ _ _ ]
 * [ _ _ _ _ _ _ _ _ _ ]
 * [ _ _ _ _ _ _ _ _ _ ]
 * [ _ _ _ _ _ _ _ _ _ ]
 * [ X X X L X R X X X ]
 *
 * L - Backward button
 * R - Forward button
 * U - Go back
 * X - Background
 * _ - SlotLayout content
 *
 */
public abstract class Scroll9x5View<G extends Gui, L extends SlotLayout> extends ScrollView<G, L> {

    private final int width = 9;
    private final int height = 5;
    private final Slot[] botPane = new Slot[9];
    private int offset = 0;
    protected String prefix;

    public Scroll9x5View(G gui, L layout, String title) {
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

    protected abstract void updateView();

    @Override public void refresh() {
        int limit = calcLimit();
        if(offset > limit) offset = limit;
        updateView();
        SlotLayout layout = getLayout();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Slot slot = layout.getSlot(x + offset, y);
                ItemStack item = null;
                if(slot != null) item = slot.createItem();
                getInventory().setItem(x + y * 9, item);
            }
        }
        updateTitle();
    }

    protected void updateTitle() {
        int page = offset + 1;
        int pages = getLayout().getWidth();
        if(pages == 1) {
            setTitle(prefix);
        } else {
            setTitle(prefix + " " + page + "/" + pages);
        }
    }

    @Override public void onOpenView(Player player) {
        refresh();
        super.onOpenView(player);
    }

    protected void setBotPaneSlot(int x, Slot slot) {
        botPane[x] = slot;
        getInventory().setItem(x + height * 9, slot.createItem());
    }

    @Nullable @Override public LocSlot getSlot(Player player, int index) {
        int x = index % 9;
        int y = index / 9;
        if(y == height) return new LocSlot(null, botPane[x]);
        SlotLayout layout = getLayout();
        Vec2i pos = new Vec2i(x + offset, y);
        Slot slot = layout.getSlot(pos.x, pos.y);
        if(slot == null) return null;
        return new LocSlot(pos, slot);
    }

    @Override public void updateSlot(int x, int y, @Nullable Slot slot) {
        x -= offset;
        if(x < 0) return;
        if(x >= width) return;
        if(y >= height) return;
        ItemStack item = null;
        if(slot != null) item = slot.createItem();
        getInventory().setItem(x + y * 9, item);
    }

    @Override public void updateDataBounds(int width, int height) {
        int limit = calcLimit();
        if(offset > limit) {
            offset = limit;
            refresh();
        }
        updateView();
        updateTitle();
    }

    public void setTitlePrefix(@NotNull String prefix) {
        this.prefix = prefix;
        updateTitle();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override public int getOffset() {
        return offset;
    }

    public static void defaultUpdateView(Scroll9x5View view, Slot background, Slot forward, Slot backward) {
        int limit = view.calcLimit();
        for (int x = 0; x < 9; x++) {
            if(x == 3 && view.getOffset() != 0) {
                view.setBotPaneSlot(x, backward);
            } else if(x == 5 && view.getOffset() != limit) {
                view.setBotPaneSlot(x, forward);
            } else {
                view.setBotPaneSlot(x, background);
            }
        }
    }

}
