package dialight.teams.random;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.collection.ObservableCollectionWrapper;
import dialight.teams.ObservableTeam;
import dialight.teams.TeamsApi;
import dialight.teams.random.gui.TeamRandomizerGui;
import dialight.teams.random.gui.filter.FilterGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class TeamRandomizerProject extends Project {

    private MainGuiApi maingui;
    private GuiLibApi guilib;
    private TeamsApi teams;

    private FilterGui filterGui;
    private TeamRandomizerGui gui;

    private final Random rnd = new Random(Calendar.getInstance().getTimeInMillis());

    private final ObservableCollection<String> filter = new ObservableCollectionWrapper<>(new HashSet<>());

    public TeamRandomizerProject(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        guilib = eh.require("GuiLib");
        teams = eh.require("Teams");

        filterGui = new FilterGui(this);
        gui = new TeamRandomizerGui(this);

        teams.addControlItem(new TeamRandomizerSlot(this));

    }

    @Override public void disable() {

    }

    @Override public ProjectApi getApi() {
        return new TeamRandomizerApi(this);
    }

    public ObservableCollection<String> getFilter() {
        return filter;
    }

    public FilterGui getFilterGui() {
        return filterGui;
    }

    public TeamRandomizerGui getGui() {
        return gui;
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

    public TeamsApi getTeams() {
        return teams;
    }

    public void doRandomize() {
        List<ObservableTeam> toRandomize = new ArrayList<>();
        if (filter.isEmpty()) {
            toRandomize.addAll(teams.getTeams());
        } else {
            for (String name : filter) {
                ObservableTeam oteam = teams.get(name);
                if(oteam != null) toRandomize.add(oteam);
            }
        }

        LinkedList<Player> online = new LinkedList<>(Bukkit.getOnlinePlayers());
        if(online.isEmpty()) return;
        int players_in_team = online.size() / toRandomize.size();
        for (ObservableTeam team : toRandomize) {
            team.clear();
            for (int i = 0; i < players_in_team; i++) {
                Player player = online.remove(rnd.nextInt(online.size()));
                team.getTeam().addEntry(player.getName());
            }
        }
        LinkedList<ObservableTeam> teamsLeft = new LinkedList<>(toRandomize);
        for (Player player : online) {
            ObservableTeam team = teamsLeft.remove(rnd.nextInt(teamsLeft.size()));
            team.getTeam().addEntry(player.getName());
        }
    }

}
