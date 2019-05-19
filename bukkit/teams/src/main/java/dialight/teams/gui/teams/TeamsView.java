package dialight.teams.gui.teams;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.Scroll9x5View;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class TeamsView extends Scroll9x5View<TeamsGui, CachedPageLayout<ObservableTeam>> {

    private final Slot background;
    private final Slot addTeam;

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
                        "|g|Плагин: |y|Телепорт",
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
            @Override
            public void onClick(SlotClickEvent e) {
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
    }

    @Override protected void updateView() {
        int limit = calcLimit();
        for (int x = 0; x < 9; x++) {
            if(x == 0) {
                setBotPaneSlot(x, addTeam);
            } else if(x == 3 && offset != 0) {
                setBotPaneSlot(x, defaultBackward);
            } else if(x == 5 && offset != limit) {
                setBotPaneSlot(x, defaultForward);
            } else {
                setBotPaneSlot(x, background);
            }
        }
    }

    @Override protected int calcLimit() {
        int limit = getLayout().getWidth() - width;
        if(limit < 0) limit = 0;
        return limit;
    }

}
