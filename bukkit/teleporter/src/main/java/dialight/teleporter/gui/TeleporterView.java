package dialight.teleporter.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedLayoutScroll9x5View;
import dialight.teleporter.Teleporter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class TeleporterView extends NamedLayoutScroll9x5View<TeleporterGui, TeleporterViewState> {

    private final Slot background;
    private final Slot selectView;
    private final Slot groupSelect;

    public TeleporterView(TeleporterGui gui, TeleporterViewState layout) {
        super(gui, layout);
        Teleporter proj = getGui().getTeleporter();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        background = new Slot() {
            @Override
            public void onClick(SlotClickEvent e) {

            }

            @NotNull @Override public ItemStack createItem() {
                NamedLayout<OfflinePlayer> current = getLayout().getCurrent();
                DyeColor color = DyeColor.LIGHT_BLUE;
                if(current == getLayout().getSelectedLayout()) {
                    color = DyeColor.LIME;
                } else if(current == getLayout().getUnselectedLayout()) {
                    color = DyeColor.GRAY;
                }
                final DyeColor colorf = color;
                return new ItemStackBuilder()
                        .let(builder -> {
                            ItemStackBuilderBc.of(builder).stainedGlassPane(colorf);
                        })
                        .displayName("Телепорт")
                        .lore(DEFAULT_BACKGROUND_LORE)
                        .addLore(Colorizer.asList(
                                "",
                                "|g|Плагин: |y|Телепорт",
                                "|g|Версия: |y|v" + desc.getVersion()
                        ))
                        .build();
            }
        };
        selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Все игроки",
                        "|a|ПКМ|y|: Выделенные",
//                        "|a|Shift|y|+|a|ЛКМ|y|: ",
                        "|a|Shift|y|+|a|ПКМ|y|: Не выделенные"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        getLayout().setCurrent(getLayout().getAllLayout());
                    } break;
                    case RIGHT: {
                        getLayout().setCurrent(getLayout().getSelectedLayout());
                    } break;
                    case SHIFT_RIGHT: {
                        getLayout().setCurrent(getLayout().getUnselectedLayout());
                    } break;
                }
            }
        };
        groupSelect = new StaticSlot(new ItemStackBuilder(Material.STICK)
                .displayName("Групповое выделение")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Выделить всех",
                        "|a|ПКМ|y|: Снять со всех выделение",
                        "|a|Shift|y|+|a|ЛКМ|y|: Выделить всех, кто онлайн",
                        "|a|Shift|y|+|a|ПКМ|y|: Выделить всех, кто офлайн"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        layout.getSelected().addAllPlayers(proj.getOfflinelib().getOffline());
                    } break;
                    case SHIFT_LEFT: {
                        layout.getSelected().addAllPlayers(proj.getOfflinelib().getOnline());
                    } break;
                    case RIGHT: {
                        layout.getSelected().removeAllPlayers(proj.getOfflinelib().getOffline());
                    } break;
                    case SHIFT_RIGHT: {
                        List<OfflinePlayer> offline = proj.getOfflinelib().getOffline().stream()
                                .filter(it -> !it.isOnline())
                                .collect(Collectors.toList());
                        layout.getSelected().addAllPlayers(offline);
                    } break;
                }
            }
        };
    }

    @Override protected void updateView() {
        int limit = calcLimit();
        for (int x = 0; x < 9; x++) {
            if(x == 0) {
                setBotPaneSlot(x, selectView);
            } else if(x == 1) {
                setBotPaneSlot(x, groupSelect);
            } else if(x == 3 && offset != 0) {
                setBotPaneSlot(x, defaultBackward);
            } else if(x == 5 && offset != limit) {
                setBotPaneSlot(x, defaultForward);
            } else {
                setBotPaneSlot(x, background);
            }
        }
    }


    @Override
    public NamedLayout getNamedLayout() {
        return getLayout().getCurrent();
    }

}
