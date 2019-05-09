package dialight.guilib.view;

import dialight.guilib.layout.SlotLayout;
import dialight.guilib.layout.LayoutListener;
import dialight.guilib.gui.Gui;
import dialight.guilib.slot.LocSlot;
import dialight.nms.InventoryNms;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by DiaLight on 09.06.2016.
 */
public abstract class View<G extends Gui, L extends SlotLayout> implements InventoryHolder, LayoutListener {

    private final G gui;
    private final L layout;
    private final Inventory inventory;
    private String title;

    public View(G gui, L layout, int width, int height, String title) {
        this.gui = gui;
        this.layout = layout;
        if(width != 9) throw new RuntimeException("Width should be == 9");
        this.inventory = Bukkit.createInventory(this, width * height, title);
        this.title = title;
    }

    @Nullable public static View getView(Inventory inv) {
        if(!(inv.getHolder() instanceof View)) return null;
        return (View) inv.getHolder();
    }

    @NotNull @Override public Inventory getInventory() {
        return this.inventory;
    }

    public G getGui() {
        return gui;
    }

    public L getLayout() {
        return layout;
    }

    @Nullable public abstract LocSlot getSlot(Player player, int index);

    public void onOutsideClick(Player player, ClickType click) {}

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }

    public void setTitle(@NotNull String title) {
        if(this.title.equals(title)) return;
        this.title = title;
        for (HumanEntity viewer : getInventory().getViewers()) {
            if(!(viewer instanceof Player)) continue;
            Player player = (Player) viewer;
            InventoryNms.sendInventoryTitle(player, inventory, title);
        }
    }

    @NotNull public String getTitle() {
        return title;
    }

}
