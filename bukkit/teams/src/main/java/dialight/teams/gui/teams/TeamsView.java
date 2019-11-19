package dialight.teams.gui.teams;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.page.Scroll9x5PageView;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class TeamsView extends Scroll9x5PageView<TeamsGui, CachedPageElement<ObservableTeam>> {

    private final ObservableScoreboard scoreboard;
    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);
    private final Slot tutorial;

    public TeamsView(TeamsGui gui, CachedPageElement<ObservableTeam> layout, ObservableScoreboard scoreboard) {
        super(gui, layout, "Команды");
        this.scoreboard = scoreboard;
        Teams proj = getGui().getProj();
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
        Slot addTeam = new StaticSlot(new ItemStackBuilder(Material.NETHER_STAR)
                .displayName(Colorizer.apply("|a|Добавить команду"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: добавить команду"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        proj.getGuilib().openGui(e.getPlayer(), gui.getAddTeamGui());
                    } break;
                }
            }
        };
        Slot controlItems = new StaticSlot(new ItemStackBuilder()
                .let(isb -> {
                    ItemStackBuilderBc.of(isb).bed(DyeColor.WHITE);
                })
                .displayName(Colorizer.apply("|a|Распределение команд"))
                .addLore(Colorizer.asList(
                        "|a|ЛКМ|y|: Откыть список распределителей"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
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
        Slot clearTeams = new StaticSlot(new ItemStackBuilder(Material.LAVA_BUCKET)
                .displayName(Colorizer.apply("|a|Очистка"))
                .addLore(Colorizer.asList(
                        "|a|Shift|y|+|a|ЛКМ|y|: удалить игроков из команд",
                        "|a|Shift|y|+|a|ПКМ|y|: удалить команды"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT:
                        break;
                    case SHIFT_LEFT:
                        for (String name : proj.getTeamWhiteList()) {
                            ObservableTeam oteam = scoreboard.teamsByName().get(name);
                            if(oteam == null) continue;
                            oteam.getMembers().removeIf(UuidPlayer::isOffline);
                        }
                        break;
                    case RIGHT:
                        break;
                    case SHIFT_RIGHT:
                        for (String name : proj.getTeamWhiteList()) {
                            ObservableTeam oteam = scoreboard.teamsByName().get(name);
                            if(oteam == null) continue;
                            oteam.getTeam().unregister();
                        }
                        break;
                }
            }
        };
        TeamWhiteListSlot teamWhiteListSlot = new TeamWhiteListSlot(proj, scoreboard);

//        Slot test = new StaticSlot(new ItemStackBuilder(Material.ARROW)
//                .displayName(Colorizer.apply("|a|Тест"))
//                .lore(Colorizer.asList(
//                        "|a|ЛКМ|y|: тест1"
//                ))
//                .build()) {
//            @Override
//            public void onClick(SlotClickEvent e) {
//                switch (e.getEvent().getClick()) {
//                    case LEFT: {
//                        ObservableScoreboard scoreboard = proj.getScoreboardManager().getMainScoreboard();
//                        for (ObservableInjectTeam team : scoreboard.teamsByName().values()) {
//                            System.out.println(team.getName());
//                        }
//                    } break;
//                }
//            }
//        };

        setBotPaneSlot(0, addTeam);
        setBotPaneSlot(1, controlItems);
//        setBotPaneSlot(3, test);
        setBotPaneSlot(7, teamWhiteListSlot);
        setBotPaneSlot(8, clearTeams);
        tutorial = new StaticSlot(new ItemStackBuilder()
                .let(isb -> {
                    ItemStackBuilderBc.of(isb).stainedGlassPane(DyeColor.RED);
                })
                .displayName(Colorizer.apply("|a|Туториал"))
                .addLore(Colorizer.asList(
                        "|y|Для работы с командами,",
                        "|y| добавьте их в белый список команд.",
                        "|y|Белый список команд находится",
                        "|y| внизу справа текущего гуи."
                ))
                .build());
    }

    @Override protected void renderContent() {
        if(getLayout().isEmpty()) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    ItemStack item = tutorial.createItem();
                    getInventory().setItem(x + y * 9, item);
                }
            }
        } else {
            super.renderContent();
        }
    }

    @Override protected Slot getLayoutSlot(int x, int y) {
        if(getLayout().isEmpty()) {
            return tutorial;
        }
        return super.getLayoutSlot(x, y);
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
