package dialight.guilib.gui;

import dialight.guilib.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

// 1 data flow <-> * view
// 1 view <-> * players
public interface Gui {

    View createView(Player player);

    default boolean onOpen(Player player) { return true; }
    default void onClose(Player player) {}

}
