package dialight.freezer.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.freezer.Freezer;
import dialight.freezer.Frozen;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
                ActionInvoker invoker = new ActionInvoker(proj.getOfflineLib().getUuidPlayer(e.getPlayer()));
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        List<Frozen> toFreeze = new ArrayList<>();
                        for (UuidPlayer player : proj.getOfflineLib().getOffline()) {
                            toFreeze.add(new Frozen(player, player.getLocation(), invoker, "freeze all players"));
                        }
                        proj.register(toFreeze);
                    }
                    break;
                    case SHIFT_LEFT: {
                        List<Frozen> toFreeze = new ArrayList<>();
                        for (Player player : proj.getOfflineLib().getOnline()) {
                            toFreeze.add(new Frozen(proj.getOfflineLib().getUuidPlayer(player), player.getLocation(), invoker, "freeze online players"));
                        }
                        proj.register(toFreeze);
                    }
                    break;
                    case RIGHT: {
                        proj.unregisterAll(invoker);
                    }
                    break;
                    case SHIFT_RIGHT: {
                        List<Frozen> toFreeze = new ArrayList<>();
                        for (UuidPlayer player : proj.getOfflineLib().getOffline()) {
                            if (player.isOnline()) continue;
                            toFreeze.add(new Frozen(player, player.getLocation(), invoker, "freeze online players"));
                        }
                        proj.register(toFreeze);
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
