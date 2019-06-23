package dialight.teams.gui.teams;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.page.Scroll9x5PageView;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class TeamsView extends Scroll9x5PageView<TeamsGui, CachedPageLayout<ObservableTeam>> {

    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);
    private final Slot addTeam;
    private final Slot controlItems;

    public TeamsView(TeamsGui gui, CachedPageLayout<ObservableTeam> layout) {
        super(gui, layout, "Команды");
        Teams proj = getGui().getProj();
        Scoreboard scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        background = new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
                })
                .displayName("Команды")
                .lore(DEFAULT_BACKGROUND_LORE)
                .addLore(Colorizer.asList(
                        "",
                        "|g|Плагин: |y|Команды",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build());
        addTeam = new StaticSlot(new ItemStackBuilder(Material.NETHER_STAR)
                .displayName("Добавить команду")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: добавить команду",
                        "|a|Shift|y|+|a|ЛКМ|y|: удалить все команды",
                        "|a|Shift|y|+|a|ПКМ|y|: очистить все команды"
                ))
                .build()) {
            @Override public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        proj.getGuilib().openGui(e.getPlayer(), gui.getAddTeamGui());
                    } break;
                    case SHIFT_LEFT: {
                        for (Team team : new ArrayList<>(scoreboard.getTeams())) {
                            team.unregister();
                        }
                    } break;
                    case RIGHT: {

                    } break;
                    case SHIFT_RIGHT: {
                        for (ObservableTeam oteam : proj.getTeamsInternal()) {
                            oteam.getMembers().clear();
                        }
                    } break;
                }
            }
        };
        controlItems = new StaticSlot(new ItemStackBuilder(Material.BED)
                .displayName("Распределение команд")
                .addLore(Colorizer.asList(
                        "|a|ЛКМ|y|: Откыть список распределителей"
                ))
                .build()) {
            @Override public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT:
                        proj.getGuilib().openGui(e.getPlayer(), proj.getControlGui());
                        break;
                    case SHIFT_LEFT:
                        break;
                    case RIGHT:
                        break;
                    case SHIFT_RIGHT:
                        break;
                }
            }
        };
    }

    @Override protected void updateView() {
        int limit = calcLimit();
        for (int x = 0; x < 9; x++) {
            if(x == 0) {
                setBotPaneSlot(x, addTeam);
            } else if(x == 1) {
                setBotPaneSlot(x, controlItems);
            } else if(x == 3 && getOffset() != 0) {
                setBotPaneSlot(x, backward);
            } else if(x == 5 && getOffset() != limit) {
                setBotPaneSlot(x, forward);
            } else {
                setBotPaneSlot(x, background);
            }
        }
    }

}
