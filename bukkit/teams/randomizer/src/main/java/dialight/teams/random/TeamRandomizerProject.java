package dialight.teams.random;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.ObservableTeam;
import dialight.teams.TeamsApi;
import dialight.teams.random.gui.TeamRandomizerGui;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class TeamRandomizerProject extends Project {

    private MainGuiApi maingui;
    private GuiLibApi guilib;
    private OfflineLibApi offlinelib;
    private TeamsApi teams;

    private TeamRandomizerGui gui;

    private final Random rnd = new Random(Calendar.getInstance().getTimeInMillis());

    public TeamRandomizerProject(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        guilib = eh.require("GuiLib");
        offlinelib = eh.require("OfflineLib");
        teams = eh.require("Teams");

        gui = new TeamRandomizerGui(this);

        teams.addControlItem(new TeamRandomizerSlot(this));

    }

    @Override public void disable() {

    }

    @Override public ProjectApi getApi() {
        return new TeamRandomizerApi(this);
    }

    public TeamRandomizerGui getGui() {
        return gui;
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

    public OfflineLibApi getOfflinelib() {
        return offlinelib;
    }

    public TeamsApi getTeams() {
        return teams;
    }

    public void doRandomize() {
        List<ObservableTeam> teamsToRandomize = new ArrayList<>();
        if (teams.getTeamFilter().isEmpty()) {
            teamsToRandomize.addAll(teams.getTeams());
        } else {
            for (String name : teams.getTeamFilter()) {
                ObservableTeam oteam = teams.get(name);
                if(oteam != null) teamsToRandomize.add(oteam);
            }
        }
        List<UUID> playersToRandomize = new ArrayList<>();
        if(teams.getPlayerFilter().isEmpty()) {
            List<UUID> collect = offlinelib.getOnline().stream().map(Player::getUniqueId).collect(Collectors.toList());
            playersToRandomize.addAll(collect);
        } else {
            playersToRandomize.addAll(teams.getPlayerFilter());
        }

        if(playersToRandomize.isEmpty()) return;
        int players_in_team = playersToRandomize.size() / teamsToRandomize.size();
        for (ObservableTeam team : teamsToRandomize) {
            team.clear();
            for (int i = 0; i < players_in_team; i++) {
                UUID uuid = playersToRandomize.remove(rnd.nextInt(playersToRandomize.size()));
                UuidPlayer uuidPlayer = offlinelib.getUuidPlayer(uuid);
                team.getTeam().addEntry(uuidPlayer.getName());
            }
        }
        LinkedList<ObservableTeam> teamsLeft = new LinkedList<>(teamsToRandomize);
        for (UUID uuid : playersToRandomize) {
            UuidPlayer uuidPlayer = offlinelib.getUuidPlayer(uuid);
            ObservableTeam team = teamsLeft.remove(rnd.nextInt(teamsLeft.size()));
            team.getTeam().addEntry(uuidPlayer.getName());
        }
    }

    public void doFillRandomize() {
        List<ObservableTeam> teamsToRandomize = new ArrayList<>();
        if (teams.getTeamFilter().isEmpty()) {
            teamsToRandomize.addAll(teams.getTeams());
        } else {
            for (String name : teams.getTeamFilter()) {
                ObservableTeam oteam = teams.get(name);
                if(oteam != null) teamsToRandomize.add(oteam);
            }
        }
        List<UUID> playersToRandomize = new ArrayList<>();
        Scoreboard scoreboard = getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        if(teams.getPlayerFilter().isEmpty()) {
            for (Player player : offlinelib.getOnline()) {
                Team team = scoreboard.getEntryTeam(player.getName());
                if(team == null) {
                    playersToRandomize.add(player.getUniqueId());
                }
            }
        } else {
            for (UUID uuid : teams.getPlayerFilter()) {
                UuidPlayer uuidPlayer = offlinelib.getUuidPlayer(uuid);
                Team team = scoreboard.getEntryTeam(uuidPlayer.getName());
                if(team == null) {
                    playersToRandomize.add(uuid);
                }
            }
        }
        if(playersToRandomize.isEmpty()) return;
        if(teamsToRandomize.isEmpty()) return;

        while(!playersToRandomize.isEmpty()) {
            UUID uuid = playersToRandomize.remove(rnd.nextInt(playersToRandomize.size()));
            UuidPlayer uuidPlayer = offlinelib.getUuidPlayer(uuid);

            ObservableTeam minTeam = teamsToRandomize.get(0);
            int minSize = minTeam.getTeam().getEntries().size();
            for (ObservableTeam team : teamsToRandomize) {
                int size = team.getTeam().getEntries().size();
                if(size < minSize) {
                    minSize = size;
                    minTeam = team;
                }
            }
            minTeam.getTeam().addEntry(uuidPlayer.getName());
        }
    }
}
