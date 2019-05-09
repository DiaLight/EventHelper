package dialight.teleporter.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.GuiUtils;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.Scroll9x5View;
import dialight.teleporter.Teleporter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class TeleporterView extends Scroll9x5View<TeleporterGui, SelectedViewState> {

    private final Slot selectView;
    private final Slot groupselect;
    private final Slot background;
    private final Slot backward;
    private final Slot forward;

    public TeleporterView(TeleporterGui gui, SelectedViewState layout) {
        super(gui, layout, "");
        Teleporter proj = getGui().getTeleporter();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Все игроки",
                        "|a|ПКМ|y|: Выделенные",
                        //                    "|a|Shift|y|+|a|ЛКМ|y|: ",
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
                        .lore(Colorizer.asList(
                                "|r|Для верного отображения",
                                "|r|заголовков столбцов",
                                "|r|используйте шрифт Unicode.",
                                "|w|Навигация",
                                "|a|ЛКМ снаружи инвертаря|y|:",
                                "|y| Скролл влево",
                                "|a|ПКМ снаружи инвертаря|y|:",
                                "|y| Скролл вправо",
                                "|a|СКМ снаружи инвертаря|y|:",
                                "|y| Вернуться назад",
                                "",
                                "|g|Плагин: |y|Телепорт",
                                "|g|Версия: |y|v" + desc.getVersion()
                        ))
                        .build();
            }
        };
        groupselect = new StaticSlot(new ItemStackBuilder(Material.STICK)
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
        backward = new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).playerHead();
                })
                .displayName("Влево")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Скролл влево",
                        "|a|Shift|y|+|a|ЛКМ|y|: Перейти на предыдущую страницу"
                ))
                .nbt(GuiUtils.BACKWARD_NBT)
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        moveBackward(1);
                    } break;
                    case SHIFT_LEFT: {
                        moveBackward(width);
                    } break;
                }
            }
        };
        forward = new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).playerHead();
                })
                .displayName("Вправо")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Скролл вправо",
                        "|a|Shift|y|+|a|ЛКМ|y|: Перейти на следующую страницу"
                ))
                .nbt(GuiUtils.FORWARD_NBT)
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        moveForward(1);
                    } break;
                    case SHIFT_LEFT: {
                        moveForward(width);
                    }
                }
            }
        };
    }

    @Override
    public void onOutsideClick(Player player, ClickType click) {
        switch (click) {
            case LEFT: {
                moveBackward(1);
            } break;
            case SHIFT_LEFT: {
                moveBackward(width);
            }
            case RIGHT: {
                moveForward(1);
            } break;
            case SHIFT_RIGHT: {
                moveForward(width);
            } break;
        }
    }

    @Override protected int calcLimit() {
        int limit = getLayout().getWidth() - width;
        if(limit < 0) limit = 0;
        return limit;
    }

    @Override protected void updateView() {
        int limit = calcLimit();
        for (int x = 0; x < 9; x++) {
            if(x == 0) {
                setBotPaneSlot(x, selectView);
            } else if(x == 1) {
                setBotPaneSlot(x, groupselect);
            } else if(x == 3 && offset != 0) {
                setBotPaneSlot(x, backward);
            } else if(x == 5 && offset != limit) {
                setBotPaneSlot(x, forward);
            } else {
                setBotPaneSlot(x, background);
            }
        }
    }

    @Override protected void updateTitle() {
        SelectedViewState state = getLayout();
        NamedLayout<OfflinePlayer> layout = state.getCurrent();
        String header = layout.buildColumnsHeader(offset, width);
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 0; i < header.length(); i++) {
            char c = header.charAt(i);
            titleBuilder.append("  ").append(c);
            titleBuilder.append((i % 2 == 0) ? " " : "  ");
        }
        setTitle(titleBuilder.toString());
    }
}
