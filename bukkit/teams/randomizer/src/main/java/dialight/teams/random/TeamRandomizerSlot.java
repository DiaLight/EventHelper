package dialight.teams.random;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.TeamBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.ObservableTeam;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.UUID;

public class TeamRandomizerSlot extends DynamicSlot {

    private final TeamRandomizerProject proj;
    private final Scoreboard scoreboard;

    public TeamRandomizerSlot(TeamRandomizerProject proj) {
        this.proj = proj;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        ObservableCollection<String> teamFilter = proj.getTeams().getTeamWhiteList();
        teamFilter.onAdd(this::update);
        teamFilter.onRemove(this::update);
        ObservableCollection<UUID> playerFilter = proj.getTeams().getPlayerBlackList();
        playerFilter.onAdd(this::update);
        playerFilter.onRemove(this::update);
        proj.getTeams().onMemberJoin(this::update);
        proj.getTeams().onMemberLeave(this::update);
    }

    private void update(String name) {
        updateLater(proj.getPlugin());
    }
    private void update(ObservableTeam oteam, String name) {
        updateLater(proj.getPlugin());
    }
    private void update(UUID uuid) {
        updateLater(proj.getPlugin());
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                proj.doFillRandomize(e.getPlayer());
                break;
            case SHIFT_LEFT:
                proj.doRandomize(e.getPlayer());
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ItemStackBuilder isb = new ItemStackBuilder();
        ItemStackBuilderBc.of(isb).enderEye();
        isb.displayName(Colorizer.apply("|a|Рандомизатор команд"));

        isb.lore(Colorizer.asList(
                "|a|ЛКМ|y|: дозаполнить команды",
                "|y| игроками, которые не",
                "|y| присутствуют ни в одной",
                "|y| команде для рандомизации",
                "|a|Shift|y|+|a|ЛКМ|y|: очистить команды и",
                "|y| распределить всех игроков",
                ""
        ));
        if(proj.getTeams().getTeamWhiteList().isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|Команды для рандомизации:",
                    " |a|все команды"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Команды для рандомизации:"
            ));
            for (String name : proj.getTeams().getTeamWhiteList()) {
                ObservableTeam team = proj.getTeams().get(name);
                if(team != null) {
                    isb.addLore(Colorizer.asList(
                            "|g|- " + team.getColor() + "⬛ |w|" + name
                    ));
                } else {
                    isb.addLore(Colorizer.asList(
                            "|g|- |w|" + name
                    ));
                }
            }
        }
        if(proj.getTeams().getPlayerBlackList().isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|Исключенные из рандомизации игроки:",
                    " |a|нет таковых"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Исключенные из рандомизации игроки:"
            ));
            ObservableCollection<UUID> playerFilter = proj.getTeams().getPlayerBlackList();
            Iterator<UUID> iterator = playerFilter.iterator();
            int previewSize = 8;
            for (int i = 0; i < previewSize && iterator.hasNext(); i++) {
                UUID uuid = iterator.next();
                UuidPlayer uuidPlayer = proj.getOfflinelib().getUuidPlayer(uuid);
                String name = uuidPlayer.getName();
                Team team = scoreboard.getEntryTeam(name);
                if(team != null) {
                    isb.addLore(Colorizer.asList(
                            "|g|- " + TeamBc.of(team).getColor() + "⬛ |w|" + name
                    ));
                } else {
                    isb.addLore(Colorizer.asList(
                            "|g|- |w|" + name
                    ));
                }
            }
            if (playerFilter.size() > previewSize) {
                int left = playerFilter.size() - previewSize;
                isb.addLore(Colorizer.asList(
                        "|g|и еще |w|" + left + "|g| игроков"
                ));
            }
        }
        isb.addLore(Colorizer.asList(
                "",
                "|g|Плагин: |y|Рандомизатор команд",
                "|g|Версия: |y|v" + desc.getVersion()
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}
