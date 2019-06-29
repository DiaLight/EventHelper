package dialight.teams.gui.control;

import dialight.compatibility.TeamBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.UUID;

public class ControlFiltersSlot extends DynamicSlot {

    private final Teams proj;
    private final Scoreboard scoreboard;

    public ControlFiltersSlot(Teams proj) {
        this.proj = proj;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        ObservableCollection<String> teamFilter = proj.getTeamFilter();
        teamFilter.onAdd(this::update);
        teamFilter.onRemove(this::update);
        ObservableCollection<UUID> playerFilter = proj.getPlayerFilter();
        playerFilter.onAdd(this::update);
        playerFilter.onRemove(this::update);
        proj.onMemberJoin(this::update);
        proj.onMemberLeave(this::update);
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
            case LEFT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getPlayerFilterGui());
            } break;
            case RIGHT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getTeamFilterGui());
            } break;
            case SHIFT_LEFT: {
                proj.setOfflineMode(!proj.isOfflineMode());
            } break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStackBuilder isb = new ItemStackBuilder(Material.HOPPER)
                .displayName(Colorizer.apply("|a|Фильтры"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: фильтр игроков",
                        "|a|Shift|y|+|a|ЛКМ|y|: Учитывать офлайн игроков",
                        "|a|ПКМ|y|: фильтр команд"
                ));
        if(proj.getTeamFilter().isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|Фильтр команд пуст"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Команды в фильтре:"
            ));
            for (String name : proj.getTeamFilter()) {
                ObservableTeam team = proj.get(name);
                if(team != null) {
                    isb.addLore(Colorizer.asList(
                            "|g|- " + team.getColor() + Colorizer.apply("⬛ |w|" + name)
                    ));
                } else {
                    isb.addLore(Colorizer.asList(
                            "|g|- |w|" + name
                    ));
                }
            }
        }
        if(proj.getPlayerFilter().isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|Фильтр игроков пуст"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Игроки в фильтре:"
            ));
            ObservableCollection<UUID> playerFilter = proj.getPlayerFilter();
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
        return isb.build();
    }

}
