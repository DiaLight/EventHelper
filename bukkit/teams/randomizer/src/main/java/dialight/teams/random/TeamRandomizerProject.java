package dialight.teams.random;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.Colorizer;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.ObservableTeam;
import dialight.teams.TeamsApi;
import dialight.teams.random.gui.TeamRandomizerGui;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

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


    public void doFillRandomize(Player invoker) {
        List<ObservableTeam> teamsToRandomize = new ArrayList<>();
        if (teams.getTeamWhiteList().isEmpty()) {
            teamsToRandomize.addAll(teams.getTeams());
        } else {
            for (String name : teams.getTeamWhiteList()) {
                ObservableTeam oteam = teams.get(name);
                if(oteam != null) teamsToRandomize.add(oteam);
            }
        }
        List<UUID> playersToRandomize = new ArrayList<>();
        Scoreboard scoreboard = getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        List<UuidPlayer> uuidPlayers = new ArrayList<>();
        if (teams.isOfflineMode()) {
            for (OfflinePlayer offlinePlayer : getPlugin().getServer().getOfflinePlayers()) {
                uuidPlayers.add(offlinelib.getUuidPlayer(offlinePlayer.getUniqueId()));
            }
        } else {
            for (Player player : getPlugin().getServer().getOnlinePlayers()) {
                uuidPlayers.add(offlinelib.getUuidPlayer(player.getUniqueId()));
            }
        }
        for (UuidPlayer uuidPlayer : uuidPlayers) {
            Team team = scoreboard.getEntryTeam(uuidPlayer.getName());
            if(team == null) {
                playersToRandomize.add(uuidPlayer.getUuid());
            } else {
                ObservableTeam oteam = teams.get(team.getName());
                if(!teamsToRandomize.contains(oteam)) {
                    playersToRandomize.add(uuidPlayer.getUuid());
                }
            }
        }
        for (UUID uuid : teams.getPlayerBlackList()) {
            playersToRandomize.remove(uuid);
        }
        if(playersToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет игроков для рандомизации"));
            return;
        }
        if(teamsToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет команд для рандомизации"));
            return;
        }
        invoker.sendMessage(Colorizer.apply("|y|Рандомизация с дозаполнением команд: команды |w|" + teamsToRandomize.size() + "|y| игроки |w|" + playersToRandomize.size()));

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

    public void doRandomize(Player invoker) {
        List<ObservableTeam> teamsToRandomize = new ArrayList<>();
        if (teams.getTeamWhiteList().isEmpty()) {
            teamsToRandomize.addAll(teams.getTeams());
        } else {
            for (String name : teams.getTeamWhiteList()) {
                ObservableTeam oteam = teams.get(name);
                if(oteam != null) teamsToRandomize.add(oteam);
            }
        }
        List<UUID> playersToRandomize = new ArrayList<>();
        if (teams.isOfflineMode()) {
            for (OfflinePlayer offlinePlayer : getPlugin().getServer().getOfflinePlayers()) {
                playersToRandomize.add(offlinePlayer.getUniqueId());
            }
        } else {
            for (Player player : getPlugin().getServer().getOnlinePlayers()) {
                playersToRandomize.add(player.getUniqueId());
            }
        }
        for (UUID uuid : teams.getPlayerBlackList()) {
            playersToRandomize.remove(uuid);
        }

        if(playersToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет игроков для рандомизации"));
            return;
        }
        if(teamsToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет команд для рандомизации"));
            return;
        }
        invoker.sendMessage(Colorizer.apply("|y|Рандомизация: команды |w|" + teamsToRandomize.size() + "|y| игроки |w|" + playersToRandomize.size()));

        int players_in_team = playersToRandomize.size() / teamsToRandomize.size();
        for (ObservableTeam team : teamsToRandomize) {
            team.clearOfflines();
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
}
