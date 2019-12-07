package dialight.teams.captain.gui.control;

import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.observable.map.ObservableMap;
import dialight.observable.set.ObservableSet;
import dialight.teams.captain.CaptainMessages;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SelectCaptainSlot extends DynamicSlot {

    private final SortByCaptain proj;

    public SelectCaptainSlot(SortByCaptain proj) {
        this.proj = proj;
        proj.getCaptainsMap().onChange(this, this::update);

        ObservableMap<String, ObservableTeam> teamsByName = proj.getTeams().getScoreboardManager().getMainScoreboard().teamsByName();
        teamsByName.onPut(this, (teamName, team) -> this.update());
        teamsByName.onRemove(this, (teamName, team) -> this.update());
        teamsByName.onReplace(this, (teamName, fr, to) -> this.update());

        ObservableSet<String> teamWhiteList = proj.getTeams().getTeamWhiteList();
        teamWhiteList.onAdd(this, (teamName) -> this.update());
        teamWhiteList.onRemove(this, (teamName) -> this.update());
    }

    @Override public void onClick(SlotClickEvent e) {
        SortByCaptainState state = proj.getStateEngine().getHandler().getValue().getState();
        if (state != SortByCaptainState.NONE) {
            e.getPlayer().sendMessage(CaptainMessages.alreadyStarted(state));
            return;
        }
        switch (e.getEvent().getClick()) {
            case LEFT: {
                proj.getCaptainSelection().open(e.getPlayer());
            } break;
            case SHIFT_LEFT:
                break;
            case RIGHT: {
            } break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStackBuilder isb = new ItemStackBuilder(Material.WOOL);

        isb.displayName(Colorizer.apply("|a|Выбор капитанов"));
        isb.addLore(Colorizer.asList(
                "|a|ЛКМ|y|: открыть гуи выбора"
        ));
        ObservableScoreboard mainScoreboard = proj.getTeams().getScoreboardManager().getMainScoreboard();
        for (String teamName : proj.getTeams().getTeamWhiteList()) {
            ObservableTeam team = mainScoreboard.teamsByName().get(teamName);
            if (team == null) continue;
            UuidPlayer captain = proj.getCaptainsMap().getCaptainByTeam(teamName);
            String captainName = captain != null ? Colorizer.apply("|w|" + captain.getName()) : Colorizer.apply("|gr|random");
            isb.addLore(
                    team.color().getValue() + "⬛ " + team.getName() + Colorizer.apply("|y|: |w|") + captainName
            );
        }
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}

