package dialight.guilib;

import dialight.misc.Colorizer;
import dialight.guilib.gui.Gui;
import dialight.guilib.elements.SlotElement;
import dialight.guilib.slot.LocSlot;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.view.View;
import dialight.fake.InventoryFk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by DiaLight on 26.02.2016.
 */
public class GuiListener implements Listener {

    private final GuiLib proj;

    public GuiListener(GuiLib proj) {
        this.proj = proj;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        View view = View.getView(e.getInventory());
        if(view == null) return;
        Player player = (Player) e.getWhoClicked();
        int capacity = view.getInventory().getSize();
        for (Integer slot : e.getRawSlots()) {
            if(slot >= capacity) continue;
            e.setCancelled(true);
            break;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        View view = View.getView(e.getInventory());
        if(view == null) return;
        Player player = (Player) e.getWhoClicked();
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) {  // e.getClickedInventory() == null
            if(e.getClick() == ClickType.MIDDLE) {
                proj.openPrev(player);
            }
            try {
                view.onOutsideClick(player, e.getClick());
            } catch (Throwable ex) {
                ex.printStackTrace();
                player.sendMessage(Colorizer.apply("|r|Error while handle outside click: " + ex.getMessage()));
            }
            return;
        }
        view = View.getView(e.getClickedInventory());
        if (view == null) {
            if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                e.setCancelled(true);
            }
            return;
        }
        LocSlot lslot = view.getSlot(player, e.getSlot());
        if (lslot == null) {
            if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                player.sendMessage(GuiMessages.unexpectedItem);
                e.setCurrentItem(null);
            } else {
                e.setCancelled(true);
            }
            return;
        } else {
            Slot slot = lslot.getSlot();
            try {
                slot.onClick(new SlotClickEvent(lslot.getLayoutPos(), slot, player, e));
            } catch (Throwable ex) {
                ex.printStackTrace();
                player.sendMessage(Colorizer.apply("|r|Error while handle click: " + ex.getMessage()));
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inventory = e.getInventory();
        View view = View.getView(inventory);
        if(view == null) return;
        Player player = (Player) e.getPlayer();
        Gui gui = view.getGui();
        SlotElement layout = view.getLayout();

        layout.subscribe(view);
        gui.fireOpenView(player);
        layout.fireOpenView(player);
        view.fireOpenView(player);

        proj.getUsageRegistry().onOpenGui(player, gui);
//        Inventory inventory = player.getOpenInventory().getTopInventory();
        proj.runTask(() -> {
            InventoryFk.sendInventoryTitle(player, inventory, view.getTitle(player));
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        View view = View.getView(e.getInventory());
        if(view == null) return;
        Player player = (Player) e.getPlayer();
        Gui gui = view.getGui();
        SlotElement layout = view.getLayout();

        gui.fireCloseView(player);
        layout.fireCloseView(player);
        view.fireCloseView(player);
        layout.unsubscribe(view);

        proj.getUsageRegistry().onCloseGui(player, gui);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        proj.clearStory(e.getPlayer());
    }

}
