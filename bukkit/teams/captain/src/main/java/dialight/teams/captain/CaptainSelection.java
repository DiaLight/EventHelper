package dialight.teams.captain;

import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.gui.captain.member.SelectCaptainGui;
import dialight.teams.captain.gui.captain.team.CaptainTeamGui;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CaptainSelection {

    private final Map<UuidPlayer, String> teamSelected = new HashMap<>();
    private final SortByCaptain proj;
    private final CaptainTeamGui captainTeamGui;
    private final SelectCaptainGui selectCaptainGui;
    private final ObservableScoreboard mainScoreboard;

    public CaptainSelection(SortByCaptain proj) {
        this.proj = proj;
        this.mainScoreboard = proj.getTeams().getScoreboardManager().getMainScoreboard();
        this.captainTeamGui = new CaptainTeamGui(proj, mainScoreboard);
        this.selectCaptainGui = new SelectCaptainGui(proj);
    }

    public void selectTeam(Player invoker, ObservableTeam oteam) {
        UuidPlayer uuidPlayer = proj.getOfflineLib().getUuidPlayer(invoker);
        teamSelected.put(uuidPlayer, oteam.getName());
        proj.getGuilib().openGui(invoker, selectCaptainGui);
//        invoker.sendMessage(CaptainMessages.selectCaptainForTeam(oteam));
    }

    public void selectCaptain(Player invoker, UuidPlayer target) {
        UuidPlayer uuidInvoker = proj.getOfflineLib().getUuidPlayer(invoker);
        String teamName = teamSelected.get(uuidInvoker);
        if(teamName != null) {
            ObservableTeam oteam = mainScoreboard.teamsByName().get(teamName);
            if(oteam != null) {
                Bukkit.broadcastMessage(CaptainMessages.selectedCaptainForTeam(oteam, target));
                proj.getCaptainsMap().put(target, teamName);
            } else {
                invoker.sendMessage(CaptainMessages.teamNotFound(teamName));
            }
        } else {
            invoker.sendMessage(CaptainMessages.selectTeamFirst);
        }
        proj.getGuilib().openGui(invoker, proj.getControlGui());
    }

    public void open(Player player) {
        proj.getGuilib().openGui(player, captainTeamGui);
    }

    public void clear(Player invoker) {
        proj.getCaptainsMap().clear();
        invoker.sendMessage(CaptainMessages.clearedAllCaptainsForTeam);
        proj.getGuilib().openGui(invoker, proj.getControlGui());
    }

    public void clear(Player invoker, ObservableTeam oteam) {
        UuidPlayer remove = proj.getCaptainsMap().removeByTeam(oteam.getName());
        if(remove != null) {
            invoker.sendMessage(CaptainMessages.clearedCaptainForTeam(oteam, remove));
        }
        proj.getGuilib().openGui(invoker, proj.getControlGui());
    }

}
