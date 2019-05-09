package dialight.guilib.view;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.gui.Gui;
import dialight.guilib.layout.SlotLayout;
import dialight.guilib.slot.*;
import org.bukkit.DyeColor;
import org.bukkit.Material;
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
public class Scroll9x5View<G extends Gui, L extends SlotLayout> extends View<G, L> {

    protected final int width = 9;
    protected final int height = 5;
    private final Slot[] botPane = new Slot[9];
    protected String prefix;
    protected int offset = 0;

    private final Slot defaultBackground = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
            })
            .displayName("")
            .build());
    private final Slot defaultBackward = new StaticSlot(new ItemStackBuilder(Material.ARROW)
            .displayName("Backward")
            .build()) {
        @Override
        public void onClick(SlotClickEvent e) {
            switch (e.getEvent().getClick()) {
                case LEFT: {
                    moveBackward(1);
                } break;
                case SHIFT_LEFT: {
                    moveBackward(width);
                } break;
            }
        }
    };
    private final Slot defaultForward = new StaticSlot(new ItemStackBuilder(Material.ARROW)
            .displayName("Forward")
            .build()) {
        @Override
        public void onClick(SlotClickEvent e) {
            switch (e.getEvent().getClick()) {
                case LEFT: {
                    moveForward(1);
                } break;
                case SHIFT_LEFT: {
                    moveForward(width);
                }
            }
        }
    };

    public Scroll9x5View(G gui, L layout, String title) {
        super(gui, layout, 9, 6, title);
        this.prefix = title;
    }

    protected int calcLimit() {
        int limit = getLayout().getWidth() - 1;
        if(limit < 0) limit = 0;
        return limit;
    }

    public void moveBackward(int dx) {
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

    public void moveForward(int dx) {
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

    protected void updateView() {
        int limit = calcLimit();
        for (int x = 0; x < 9; x++) {
            if(x == 3 && offset != 0) {
                setBotPaneSlot(x, defaultBackward);
            } else if(x == 5 && offset != limit) {
                setBotPaneSlot(x, defaultForward);
            } else {
                setBotPaneSlot(x, defaultBackground);
            }
        }
    }

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
        setTitle(prefix + " " + (offset + 1) + "/" + getLayout().getWidth());
    }

    @Override
    public void onOpen(Player player) {
        refresh();
        super.onOpen(player);
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

    @Override
    public void updateSlot(int x, int y, @Nullable Slot slot) {
        x -= offset;
        if(x < 0) return;
        if(x >= width) return;
        if(y >= height) return;
        ItemStack item = null;
        if(slot != null) item = slot.createItem();
        getInventory().setItem(x + y * 9, item);
    }

    @Override
    public void updateDataBounds(int width, int height) {
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

}
