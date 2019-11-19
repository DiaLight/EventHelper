package dialight.guilib.gui;

import dialight.guilib.Viewable;
import dialight.guilib.view.View;
import org.bukkit.entity.Player;

// 1 data flow <-> * view
// 1 view <-> * players
public abstract class Gui extends Viewable {

    abstract public View createView(Player player);

}
