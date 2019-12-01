package dialight.teams.captain.gui.results;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.page.Scroll9x5PageView;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.TeamSortResult;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;

public class ResultsView extends Scroll9x5PageView<ResultsGui, CachedPageElement<TeamSortResult>> {

    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public ResultsView(ResultsGui gui, CachedPageElement<TeamSortResult> layout) {
        super(gui, layout, "Результат сортировки");
        SortByCaptain proj = getGui().getProj();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        background = new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
                })
                .displayName(Colorizer.apply("|a|Команды"))
                .lore(DEFAULT_BACKGROUND_LORE)
                .addLore(Colorizer.asList(
                        "",
                        "|g|Плагин: |y|Команды",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build());
        Slot teleportAll = new StaticSlot(new ItemStackBuilder(Material.FISHING_ROD)
                .displayName(Colorizer.apply("|a|Телепортировать игроков по командам"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Телепортировать игроков по командам",
                        "|w|У команд должны быть установлены точки вхождения",
                        "|a|ПКМ|y|: Добавить игроков в соответствующие команды"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        for (TeamSortResult result : proj.getSortResult().values()) {
                            Location entryPoint = proj.getTeams().getTeamEntryPoints().get(result.getName());
                            if(entryPoint != null) {
                                for (UuidPlayer member : result.getMembers()) {
                                    member.teleport(entryPoint);
                                }
                            }
                        }
                    } break;
                    case RIGHT: {
                        for (TeamSortResult result : proj.getSortResult().values()) {
                            ObservableTeam team = proj.getTeams().getScoreboardManager().getMainScoreboard().teamsByName().get(result.getName());
                            team.getMembers().addAll(result.getMembers());
                        }
                    } break;
                }
            }
        };

        setBotPaneSlot(0, teleportAll);
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
