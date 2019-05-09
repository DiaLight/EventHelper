package dialight.teleporter.gui;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleporterGui implements Gui {

    private final Teleporter proj;
    private final Map<UUID, SelectedViewState> layoutMap = new HashMap<>();

    public TeleporterGui(Teleporter proj) {
        this.proj = proj;
    }

    public SelectedViewState getOrCreateLayout(Player player) {
        SelectedViewState state = layoutMap.get(player.getUniqueId());
        if(state != null) return state;
        SelectedPlayers selected = proj.getSelectedPlayers(player.getUniqueId());
        state = new SelectedViewState(proj, selected);
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
