package dialight.guilib.view;

import dialight.guilib.gui.Gui;
import dialight.guilib.elements.SlotElement;
import dialight.guilib.slot.LocSlot;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotUsage;
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
public abstract class Scroll9x5View<G extends Gui, L extends SlotElement> extends ScrollView<G, L> {

    private final int width = 9;
    private final int height = 5;
    private final BotPanelUsage[] botPane = new BotPanelUsage[9];
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

    protected abstract Slot createBotBackground(int x);

    protected void updateView() {
        for (int x = 0; x < botPane.length; x++) {
            BotPanelUsage slotUsage = botPane[x];
            Slot slot;
            if(slotUsage != null) {
                slot = slotUsage.slot;
            } else {
                slot = createBotBackground(x);
            }
            getInventory().setItem(x + height * 9, slot.createItem());
        }
    }
    protected void updateOffset() {
        int limit = calcLimit();
        if(offset > limit) offset = limit;
    }
    protected void renderContent() {
        SlotElement layout = getLayout();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Slot slot = layout.getSlot(x + offset, y);
                ItemStack item = null;
                if(slot != null) item = slot.createItem();
                getInventory().setItem(x + y * 9, item);
            }
        }
    }

    @Override public void refresh() {
        updateOffset();
        updateView();
        renderContent();
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

    protected void setBotPaneSlot(int x, Slot slot) {
        BotPanelUsage prevSlot = botPane[x];
        if(prevSlot != null) {
            prevSlot.slot.detached(prevSlot);
        }
        BotPanelUsage slotUsage = new BotPanelUsage(this, slot, x);
        slot.attached(slotUsage);
        botPane[x] = slotUsage;
        getInventory().setItem(x + height * 9, slot.createItem());
    }

    protected Slot getLayoutSlot(int x, int y) {
        return getLayout().getSlot(x, y);
    }

    @Nullable @Override public LocSlot getSlot(Player player, int index) {
        int x = index % 9;
        int y = index / 9;
        if(y == height) {
            BotPanelUsage slotUsage = botPane[x];
            Slot slot;
            if(slotUsage != null) {
                slot = slotUsage.slot;
            } else {
                slot = createBotBackground(x);
            }
            return new LocSlot(null, slot);
        }
        Vec2i pos = new Vec2i(x + offset, y);
        Slot slot = getLayoutSlot(pos.x, pos.y);
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

    public static Slot defaultCreateBotBackground(Scroll9x5View view, int x, Slot background, Slot forward, Slot backward) {
        int offset = view.getOffset();
        if(x == 3 && offset != 0) {
            return backward;
        }
        if(x == 5 && offset != view.calcLimit()) {
            return forward;
        }
        return background;
    }
    @Deprecated
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

    public static class BotPanelUsage implements SlotUsage {

        private final Scroll9x5View view;
        private final Slot slot;
        private final int x;

        public BotPanelUsage(Scroll9x5View view, Slot slot, int x) {
            this.view = view;
            this.slot = slot;
            this.x = x;
        }

        @Override public void update() {
            view.getInventory().setItem(x + view.getHeight() * 9, slot.createItem());
        }

    }

}
