package dialight.teleporter.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;
import dialight.offlinelib.UuidPlayer;
import dialight.teleporter.Teleporter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class TeleporterView extends NamedElementScroll9X5View<TeleporterGui, TeleporterViewState> {

    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public TeleporterView(TeleporterGui gui, TeleporterViewState layout) {
        super(gui, layout);
        Teleporter proj = getGui().getTeleporter();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        background = new Slot() {
            @Override
            public void onClick(SlotClickEvent e) {

            }

            @NotNull @Override public ItemStack createItem() {
                NamedElement<UuidPlayer> current = getLayout().getCurrent();
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
        Slot selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Все игроки",
                        "|a|ПКМ|y|: Выделенные",
                        "|a|Shift|y|+|a|ПКМ|y|: Не выделенные"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        getLayout().setCurrent(getLayout().getAllLayout());
                    }
                    break;
                    case RIGHT: {
                        getLayout().setCurrent(getLayout().getSelectedLayout());
                    }
                    break;
                    case SHIFT_RIGHT: {
                        getLayout().setCurrent(getLayout().getUnselectedLayout());
                    }
                    break;
                }
            }
        };
        Slot groupSelect = new StaticSlot(new ItemStackBuilder(Material.STICK)
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
                        layout.getSelected().addAllUuidPlayers(proj.getOfflinelib().getOffline());
                    }
                    break;
                    case SHIFT_LEFT: {
                        layout.getSelected().addAllPlayers(proj.getOfflinelib().getOnline());
                    }
                    break;
                    case RIGHT: {
                        layout.getSelected().removeAllUuidPlayers(proj.getOfflinelib().getOffline());
                    }
                    break;
                    case SHIFT_RIGHT: {
                        List<UuidPlayer> offline = proj.getOfflinelib().getOffline().stream()
                                .filter(it -> !it.isOnline())
                                .collect(Collectors.toList());
                        layout.getSelected().addAllUuidPlayers(offline);
                    }
                    break;
                }
            }
        };
        setBotPaneSlot(0, selectView);
        setBotPaneSlot(1, groupSelect);
        setEmptyTitleReplace("Телепортер");
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

    @Override
    public NamedElement getNamedLayout() {
        return getLayout().getCurrent();
    }

}
