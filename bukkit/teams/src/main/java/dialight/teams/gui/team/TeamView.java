package dialight.teams.gui.team;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedLayoutScroll9x5View;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.TeleporterApi;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;
import java.util.stream.Collectors;

public class TeamView extends NamedLayoutScroll9x5View<TeamGui, TeamLayout> {

    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);
    private final Slot selectView;
    private final Slot groupSelect;

    public TeamView(TeamGui gui, TeamLayout layout) {
        super(gui, layout);
        Teams proj = getGui().getProj();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ObservableTeam oteam = gui.getOteam();
        background = new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).stainedGlassPane(ColorConverter.toWoolColor(oteam.getColor()));
                })
                .displayName("Команда " + oteam.getName())
                .lore(DEFAULT_BACKGROUND_LORE)
                .addLore(Colorizer.asList(
                        "|y|display name: |g|" + oteam.getTeam().getDisplayName(),
                        "",
                        "|g|Плагин: |y|Команды",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build());
        selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Игроки в текущей команде",
                        "|a|ПКМ|y|: Игроки ни в какой команде",
                        "|a|Shift|y|+|a|ПКМ|y|: Игроки не в текущей команде"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        getLayout().setCurrent(getLayout().getTeamLayout());
                    } break;
                    case RIGHT: {
                        getLayout().setCurrent(getLayout().getNotCurTeamLayout());
                    } break;
                }
            }
        };
        TeleporterApi teleporter = proj.getTeleporter();
        if(teleporter != null) {
            groupSelect = new StaticSlot(new ItemStackBuilder(Material.STICK)
                    .displayName("Групповое выделение в телепортере")
                    .lore(Colorizer.asList(
                            "|a|ЛКМ|y|: Выделить всех, кто в команде",
                            "|a|ПКМ|y|: Снять выделение со всех, кто в команде",
                            "|a|Shift|y|+|a|ЛКМ|y|: Выделить тех из команды, кто онлайн",
                            "|a|Shift|y|+|a|ПКМ|y|: Выделить тех из команды, кто офлайн"
                    ))
                    .build()) {
                @Override
                public void onClick(SlotClickEvent e) {
                    SelectedPlayers selected = teleporter.getSelectedPlayers(e.getPlayer().getUniqueId());
                    switch (e.getEvent().getClick()) {
                        case LEFT: {
                            selected.addAllPlayers(oteam.getMembers());
                        } break;
                        case SHIFT_LEFT: {
                            List<OfflinePlayer> online = oteam.getMembers().stream()
                                    .filter(OfflinePlayer::isOnline)
                                    .collect(Collectors.toList());
                            selected.addAllPlayers(online);
                        } break;
                        case RIGHT: {
                            selected.removeAllPlayers(oteam.getMembers());
                        } break;
                        case SHIFT_RIGHT: {
                            List<OfflinePlayer> offline = oteam.getMembers().stream()
                                    .filter(it -> !it.isOnline())
                                    .collect(Collectors.toList());
                            selected.addAllPlayers(offline);
                        } break;
                    }
                }
            };
        } else {
            groupSelect = background;
        }
        setBotPaneSlot(0, selectView);
        setBotPaneSlot(1, groupSelect);
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

    @Override
    public NamedLayout getNamedLayout() {
        return getLayout().getCurrent();
    }

}
