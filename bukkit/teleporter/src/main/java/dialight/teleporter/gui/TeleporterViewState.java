package dialight.teleporter.gui;

import dialight.guilib.elements.NamedElement;
import dialight.guilib.elements.ReplaceableElement;
import dialight.misc.player.UuidPlayer;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public class TeleporterViewState extends ReplaceableElement<NamedElement<UuidPlayer>> {

    public static void dumpThrow(UuidPlayer up, NamedElement<UuidPlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private final SelectedElement selectedLayout;
    private final UnselectedElement unselectedLayout;
    private final AllElement allLayout;
    @NotNull private final Teleporter proj;
    @NotNull private final Server server;
    @NotNull private final SelectedPlayers selected;

    public TeleporterViewState(Teleporter proj, SelectedPlayers selected) {
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
        this.selected = selected;

        selectedLayout = new SelectedElement(proj, selected);
        unselectedLayout = new UnselectedElement(proj, selected);
        allLayout = new AllElement(proj, selected);

        setCurrent(allLayout);
    }

    public NamedElement<UuidPlayer> getAllLayout() {
        return allLayout;
    }

    public NamedElement<UuidPlayer> getUnselectedLayout() {
        return unselectedLayout;
    }

    public NamedElement<UuidPlayer> getSelectedLayout() {
        return selectedLayout;
    }

    @NotNull public SelectedPlayers getSelected() {
        return selected;
    }

}
