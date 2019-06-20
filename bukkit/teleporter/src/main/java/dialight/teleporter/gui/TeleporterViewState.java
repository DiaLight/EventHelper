package dialight.teleporter.gui;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public class TeleporterViewState extends ReplaceableLayout<NamedLayout<OfflinePlayer>> {

    public static void dumpThrow(OfflinePlayer op, NamedLayout<OfflinePlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private final SelectedLayout selectedLayout;
    private final UnselectedLayout unselectedLayout;
    private final AllLayout allLayout;
    @NotNull private final Teleporter proj;
    @NotNull private final Server server;
    @NotNull private final SelectedPlayers selected;

    public TeleporterViewState(Teleporter proj, SelectedPlayers selected) {
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
        this.selected = selected;

        selectedLayout = new SelectedLayout(proj, selected);
        unselectedLayout = new UnselectedLayout(proj, selected);
        allLayout = new AllLayout(proj, selected);

        setCurrent(allLayout);
    }

    public NamedLayout<OfflinePlayer> getAllLayout() {
        return allLayout;
    }

    public NamedLayout<OfflinePlayer> getUnselectedLayout() {
        return unselectedLayout;
    }

    public NamedLayout<OfflinePlayer> getSelectedLayout() {
        return selectedLayout;
    }

    @NotNull public SelectedPlayers getSelected() {
        return selected;
    }

}
