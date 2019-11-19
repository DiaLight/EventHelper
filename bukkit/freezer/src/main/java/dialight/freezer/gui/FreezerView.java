package dialight.freezer.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.ActionInvoker;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.freezer.Freezer;
import dialight.freezer.Frozen;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class FreezerView extends NamedElementScroll9X5View<FreezerGui, FreezerViewState> {

    private final Slot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public FreezerView(FreezerGui gui, FreezerViewState layout) {
        super(gui, layout);
        Freezer proj = getGui().getFreezer();
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
                        .displayName("Замораживатель")
                        .lore(DEFAULT_BACKGROUND_LORE)
                        .addLore(Colorizer.asList(
                                "",
                                "|g|Плагин: |y|Замораживатель",
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
        Slot groupSelect = new StaticSlot(new ItemStackBuilder(Material.ICE)
                .displayName("Групповое замораживание")
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
                        for (UuidPlayer player : proj.getOfflinelib().getOffline()) {
                            proj.register(new Frozen(player, player.getLocation(), new ActionInvoker(e.getPlayer()), "freeze all players"));
                        }
                    }
                    break;
                    case SHIFT_LEFT: {
                        for (Player player : proj.getOfflinelib().getOnline()) {
                            proj.register(new Frozen(proj.getOfflinelib().getUuidPlayer(player), player.getLocation(), new ActionInvoker(e.getPlayer()), "freeze online players"));
                        }
                    }
                    break;
                    case RIGHT: {
                        for (UuidPlayer player : proj.getOfflinelib().getOffline()) {
                            proj.unregister(player);
                        }
                    }
                    break;
                    case SHIFT_RIGHT: {
                        for (UuidPlayer player : proj.getOfflinelib().getOffline()) {
                            if (player.isOnline()) continue;
                            proj.register(new Frozen(player, player.getLocation(), new ActionInvoker(e.getPlayer()), "freeze online players"));
                        }
                    }
                    break;
                }
            }
        };
        setBotPaneSlot(0, selectView);
        setBotPaneSlot(1, groupSelect);
        setEmptyTitleReplace("Замораживатель");
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

    @Override
    public NamedElement getNamedLayout() {
        return getLayout().getCurrent();
    }

}
