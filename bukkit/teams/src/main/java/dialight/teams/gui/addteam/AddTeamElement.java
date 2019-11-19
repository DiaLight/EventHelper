package dialight.teams.gui.addteam;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.indexcache.IndexCache;
import dialight.guilib.slot.Slot;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class AddTeamElement extends CachedPageElement<ChatColor> {

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
    private final ObservableScoreboard scoreboard;

    public AddTeamElement(Teams proj, ObservableScoreboard scoreboard, IndexCache cache) {
        super(cache);
        this.proj = proj;
        this.scoreboard = scoreboard;
        this.setNameFunction(Enum::name);
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        proj.onTeamUpdate(this, this::onTeamUpdate);

        COLORS.forEach(this::add);

        scoreboard.teamsByName().onPut(this, this::onAdd);
        scoreboard.teamsByName().onRemove(this, this::onRemove);
        scoreboard.teamsByName().forEach(this::onAdd);
    }

    @Override public void onViewersEmpty() {
        proj.unregisterOnTeamUpdate(this);
        coloredTeams.clear();

        scoreboard.teamsByName().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onAdd(String name, ObservableTeam oteam) {
        coloredTeams.add(oteam.color().getValue());
        update(oteam.color().getValue());
    }

    private void onRemove(String name, ObservableTeam oteam) {
//        coloredTeams.clear();
//        coloredTeams.addAll(scoreboard.teamsByName().values().stream()
//                .map(ObservableTeam::getColor)
//                .collect(Collectors.toList()));
        coloredTeams.remove(oteam.color().getValue());
        update(oteam.color().getValue());
    }

    private void onTeamUpdate(ObservableTeam oteam) {
        update(oteam.color().getValue());
    }

    private Slot createSlot(ChatColor color) {
        return new AddTeamSlot(color, proj, coloredTeams);
    }

}
