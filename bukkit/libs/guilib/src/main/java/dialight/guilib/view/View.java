package dialight.guilib.view;

import dialight.guilib.gui.Gui;
import dialight.guilib.UpdateListener;
import dialight.guilib.elements.SlotElement;
import dialight.guilib.slot.LocSlot;
import dialight.fake.InventoryFk;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by DiaLight on 09.06.2016.
 */
public abstract class View<G extends Gui, L extends SlotElement> implements InventoryHolder, UpdateListener {

    private final G gui;
    private final L layout;
    private final Inventory inventory;
    protected String title;

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

    public void onOutsideClick(Player player, ClickType click) {

    }

    public void setTitle(@NotNull String title) {
        if(this.title.equals(title)) return;
        this.title = title;
        for (HumanEntity viewer : getInventory().getViewers()) {
            if(!(viewer instanceof Player)) continue;
            Player player = (Player) viewer;
            InventoryFk.sendInventoryTitle(player, inventory, title);
        }
    }

    @NotNull public String getTitle(Player player) {
        return title;
    }

    public final void fireOpenView(Player player) {
        onOpenView(player);
        if(inventory.getViewers().size() == 1) onViewersNotEmpty();
    }
    public final void fireCloseView(Player player) {
        onCloseView(player);
        if(inventory.getViewers().isEmpty()) onViewersEmpty();
    }

    public void onOpenView(Player player) {}
    public void onCloseView(Player player) {}

    public void onViewersNotEmpty() {}
    public void onViewersEmpty() {}

    public Collection<Player> getViewers() {
        return inventory.getViewers().stream()
                .filter(he -> he instanceof Player)
                .map(he -> (Player) he)
                .collect(Collectors.toList());
    }

}
