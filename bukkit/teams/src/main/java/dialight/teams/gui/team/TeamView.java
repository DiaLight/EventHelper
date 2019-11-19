package dialight.teams.gui.team;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableTeam;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.TeleporterApi;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;
import java.util.stream.Collectors;

public class TeamView extends NamedElementScroll9X5View<TeamGui, TeamElement> {

    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public TeamView(TeamGui gui, TeamElement layout) {
        super(gui, layout);
        Teams proj = getGui().getProj();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ObservableTeam oteam = gui.getOteam();
        background = new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).stainedGlassPane(ColorConverter.toWoolColor(oteam.color().getValue()));
                })
                .displayName(Colorizer.apply(
                        "|g|- " + oteam.color().getValue() + "⬛ |w|" + oteam.getName()
                ))
                .lore(Colorizer.asList(
                        "|y|display name: |w|" + oteam.getTeam().getDisplayName(),
                        ""
                ))
                .addLore(DEFAULT_BACKGROUND_LORE)
                .addLore(Colorizer.asList(
                        "",
                        "|g|Плагин: |y|Команды",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build());
        Slot selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Игроки в текущей команде",
                        "|a|ПКМ|y|: Игроки не в текущей команде"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        getLayout().setTeamLayout();
                    }
                    break;
                    case RIGHT: {
                        getLayout().setNotMembersLayout();
                    }
                    break;
                }
            }
        };
        TeleporterApi teleporter = proj.getTeleporter();
        Slot groupSelect;
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
                            selected.addAllUuidPlayers(oteam.getMembers());
                        } break;
                        case SHIFT_LEFT: {
                            List<UuidPlayer> online = oteam.getMembers().stream()
                                    .filter(UuidPlayer::isOnline)
                                    .collect(Collectors.toList());
                            selected.addAllUuidPlayers(online);
                        } break;
                        case RIGHT: {
                            selected.removeAllUuidPlayers(oteam.getMembers());
                        } break;
                        case SHIFT_RIGHT: {
                            List<UuidPlayer> offline = oteam.getMembers().stream()
                                    .filter(it -> !it.isOnline())
                                    .collect(Collectors.toList());
                            selected.addAllUuidPlayers(offline);
                        } break;
                    }
                }
            };
        } else {
            groupSelect = background;
        }
        Slot clearTeam = new StaticSlot(new ItemStackBuilder(Material.LAVA_BUCKET)
                .displayName(Colorizer.apply("|a|Очистка"))
                .addLore(Colorizer.asList(
                        "|a|Shift|y|+|a|ЛКМ|y|: удалить игроков из команды"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT:
                        break;
                    case SHIFT_LEFT:
                        oteam.getMembers().removeIf(UuidPlayer::isOffline);
                        break;
                    case RIGHT:
                        break;
                    case SHIFT_RIGHT:
                        break;
                }
            }
        };
        setBotPaneSlot(0, selectView);
        setBotPaneSlot(1, groupSelect);
        setBotPaneSlot(8, clearTeam);
//        setEmptyTitleReplace(Colorizer.apply(oteam.getColor() + "⬛ |w|" + oteam.getName()));
        setEmptyTitleReplace(oteam.getName());
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

    @Override
    public NamedElement getNamedLayout() {
        return getLayout().getCurrent();
    }

}
