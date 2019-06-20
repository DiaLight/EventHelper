package dialight.teleporter.gui;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleporterGui extends Gui {

    private final Teleporter proj;
    private final Map<UUID, TeleporterViewState> layoutMap = new HashMap<>();

    public TeleporterGui(Teleporter proj) {
        this.proj = proj;
    }

    public TeleporterViewState getOrCreateLayout(Player player) {
        TeleporterViewState state = layoutMap.get(player.getUniqueId());
        if(state != null) return state;
        SelectedPlayers selected = proj.getSelectedPlayers(player.getUniqueId());
        state = new TeleporterViewState(proj, selected);
        layoutMap.put(player.getUniqueId(), state);
        return state;
    }

    @Override
    public View createView(Player player) {
        return new TeleporterView(this, getOrCreateLayout(player));
    }

    public Teleporter getTeleporter() {
        return proj;
    }

}
