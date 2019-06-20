package dialight.guilib.gui;

import dialight.guilib.layout.FixedLayout;
import dialight.guilib.view.View;
import dialight.guilib.view.Fixed9x6View;
import org.bukkit.entity.Player;

public class Fixed9x6Gui extends Gui {

    private final FixedLayout layout = new FixedLayout(9, 6);
    private final Fixed9x6View view;

    public Fixed9x6Gui(String title) {
        this.view = new Fixed9x6View(this, this.layout, title);
    }

    @Override public View createView(Player player) {
        return this.view;
    }

}
