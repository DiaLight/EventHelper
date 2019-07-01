package dialight.teams.gui.addteam;

import dialight.guilib.indexcache.IndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AddTeamLayout extends CachedPageLayout<ChatColor> {

    private static final List<ChatColor> COLORS = Arrays.asList(
            ChatColor.WHITE,
            ChatColor.BLACK,
            ChatColor.DARK_GRAY,
            ChatColor.GRAY,
            ChatColor.DARK_RED,
            ChatColor.RED,
            ChatColor.GREEN,
            ChatColor.DARK_GREEN,
            ChatColor.BLUE,
            ChatColor.DARK_BLUE,
            ChatColor.YELLOW,
            ChatColor.GOLD,
            ChatColor.AQUA,
            ChatColor.DARK_AQUA,
            ChatColor.LIGHT_PURPLE,
            ChatColor.DARK_PURPLE
    );

    private final Set<ChatColor> coloredTeams = EnumSet.noneOf(ChatColor.class);
    @NotNull private final Teams proj;
    private final Consumer<ObservableTeam> onTeamUpdate = this::onTeamUpdate;
    private final Consumer<ObservableTeam> onAdd = this::onAdd;
    private final Consumer<ObservableTeam> onRemove = this::onRemove;

    public AddTeamLayout(Teams proj, IndexCache cache) {
        super(cache);
        this.proj = proj;
        this.setNameFunction(Enum::name);
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);

        proj.onTeamUpdate(onTeamUpdate);
        coloredTeams.addAll(proj.getTeamsImmutable().stream()
                .map(ObservableTeam::getColor)
                .collect(Collectors.toList()));

        ObservableCollection<ObservableTeam> teams = proj.getTeamsImmutable();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);

        COLORS.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);

        proj.unregisterOnTeamUpdate(onTeamUpdate);
        coloredTeams.clear();

        ObservableCollection<ObservableTeam> teams = proj.getTeamsImmutable();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);

        proj.runTask(this::clear);
    }

    private void onAdd(ObservableTeam oteam) {
        coloredTeams.add(oteam.getColor());
        update(oteam.getColor());
    }

    private void onRemove(ObservableTeam oteam) {
        coloredTeams.clear();
        coloredTeams.addAll(proj.getTeamsImmutable().stream()
                .map(ObservableTeam::getColor)
                .collect(Collectors.toList()));
        update(oteam.getColor());
    }

    private void onTeamUpdate(ObservableTeam oteam) {
        update(oteam.getColor());
    }

    private Slot createSlot(ChatColor color) {
        return new AddTeamSlot(color, proj, coloredTeams);
    }

}
