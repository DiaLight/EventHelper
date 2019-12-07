package dialight.teams.random;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.misc.Colorizer;
import dialight.misc.player.UuidPlayer;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.TeamSortResult;
import dialight.teams.TeamsApi;
import dialight.teams.observable.ObservableTeam;
import dialight.teams.random.gui.TeamRandomizerGui;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SortByRandom extends Project {

    private MainGuiApi maingui;
    private GuiLibApi guilib;
    private OfflineLibApi offlinelib;
    private TeamsApi teams;

    private TeamRandomizerGui gui;

    private final Random rnd = new Random(Calendar.getInstance().getTimeInMillis());

    public SortByRandom(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        guilib = eh.require("GuiLib");
        offlinelib = eh.require("OfflineLib");
        teams = eh.require("Teams");

        gui = new TeamRandomizerGui(this);

        teams.addControlItem(new SortByRandomSlot(this));

    }

    @Override public void disable() {

    }

    @Override public ProjectApi getApi() {
        return new SortByRandomApi(this);
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

    private static class TeamFillStatus extends TeamSortResult {

        private final int size;

        public TeamFillStatus(ObservableTeam team) {
            super(team);
            size = team.getMembers().size();
        }

        private int getSize() {
            return size + members.size();
        }

    }

    public void doFillRandomize(Player invoker) {
        List<UuidPlayer> playersToRandomize = teams.collectSortMembers();
        if(playersToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет игроков для рандомизации"));
            return;
        }
        List<ObservableTeam> teamsToRandomize = teams.collectSortTeams();
        if(teamsToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет команд для рандомизации"));
            return;
        }

        // filter players
        for (ObservableTeam team : teamsToRandomize) {
            playersToRandomize.removeIf(player -> team.getMembers().contains(player));
        }

        // prepare fill status
        Map<String, TeamFillStatus> fillTeams = teamsToRandomize.stream().map(TeamFillStatus::new).collect(Collectors.toMap(TeamSortResult::getName, Function.identity()));

        invoker.sendMessage(Colorizer.apply("|y|Рандомизация команд с дозаполнением: команды |w|" + fillTeams.size() + "|y| игроки |w|" + playersToRandomize.size()));

        while(!playersToRandomize.isEmpty()) {
            UuidPlayer uuidPlayer = playersToRandomize.remove(rnd.nextInt(playersToRandomize.size()));
            TeamFillStatus minTeam = null;
            int minSize = Integer.MAX_VALUE;
            for (TeamFillStatus team : fillTeams.values()) {
                int size = team.getSize();
                if(size < minSize) {
                    minSize = size;
                    minTeam = team;
                }
            }
            Objects.requireNonNull(minTeam);
            minTeam.addMember(uuidPlayer);
        }
        teams.getSortResult().setValue(fillTeams);
    }

    public void doRandomize(Player invoker) {
        List<ObservableTeam> teamsToRandomize = teams.collectSortTeams();
        if(teamsToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет команд для рандомизации"));
            return;
        }
        List<UuidPlayer> playersToRandomize = teams.collectSortMembers();
        if(playersToRandomize.isEmpty()) {
            invoker.sendMessage(Colorizer.apply("|r|Нет игроков для рандомизации"));
            return;
        }

        Map<String, TeamSortResult> teamsResult = teamsToRandomize.stream().map(TeamSortResult::new).collect(Collectors.toMap(TeamSortResult::getName, Function.identity()));

        invoker.sendMessage(Colorizer.apply("|y|Рандомизация: команды |w|" + teamsResult.size() + "|y| игроки |w|" + playersToRandomize.size()));

        int players_in_team = playersToRandomize.size() / teamsResult.size();
        for (TeamSortResult oteam : teamsResult.values()) {
            oteam.getMembers().removeIf(UuidPlayer::isOffline);
            for (int i = 0; i < players_in_team; i++) {
                UuidPlayer uuidPlayer = playersToRandomize.remove(rnd.nextInt(playersToRandomize.size()));
                oteam.addMember(uuidPlayer);
            }
        }
        LinkedList<TeamSortResult> teamsLeft = new LinkedList<>(teamsResult.values());
        for (UuidPlayer uuidPlayer : playersToRandomize) {
            TeamSortResult sortResult = teamsLeft.remove(rnd.nextInt(teamsLeft.size()));
            sortResult.addMember(uuidPlayer);
        }
        teams.getSortResult().setValue(teamsLeft.stream().collect(Collectors.toMap(TeamSortResult::getName, Function.identity())));
    }
}
