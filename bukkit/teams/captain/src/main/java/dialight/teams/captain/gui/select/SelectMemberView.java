package dialight.teams.captain.gui.select;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SelectMemberView extends NamedElementScroll9X5View<SelectMemberGui, SelectMemberLayout> {

    private final DynamicSlot background;
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public SelectMemberView(SelectMemberGui gui, SelectMemberLayout layout) {
        super(gui, layout);
        final SortByCaptain proj = gui.getProj();
        background = new DynamicSlot() {
            @Override public void onClick(SlotClickEvent e) {

            }

            @NotNull @Override public ItemStack createItem() {
                ObservableTeam oteam = proj.getCaptainHandler().getCurrentTeam().getValue();
                if(oteam == null) return new ItemStack(Material.WOOL);
                return new ItemStackBuilder()
                        .let(builder -> {
                            ItemStackBuilderBc.of(builder).stainedGlassPane(oteam.getDyeColor());
                        })
                        .displayName(Colorizer.apply(
                                "|g|- " + oteam.color().getValue() + "⬛ |w|" + oteam.getName()
                        ))
                        .lore(Colorizer.asList(
                                "|y|display name: |w|" + oteam.getTeam().getDisplayName(),
                                ""
                        ))
                        .addLore(DEFAULT_BACKGROUND_LORE)
                        .build();
            }
        };
        setBotPaneSlot(0, new StaticSlot(new ItemStackBuilder(Material.HOPPER)
                .displayName(Colorizer.apply("|a|Рандомный выбор"))
                .lore(Colorizer.apply(
                        "|a|ЛКМ|y|: рандомно выбрать игрока",
                        "|a|Shift|y|+|a|ЛКМ|y|: рандомно выбрать игрока и подтвердить"
                ))
                .build()) {
            @Override public void onClick(SlotClickEvent e) {
                ActionInvoker invoker = new ActionInvoker(proj.getOfflineLib().getUuidPlayer(e.getPlayer()));
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        UuidPlayer target = proj.getMembersHandler().peekRandomUnsorted();
                        proj.select(invoker, target);
                        e.getPlayer().closeInventory();
                    } break;
                    case SHIFT_LEFT: {
                        UuidPlayer target = proj.getMembersHandler().peekRandomUnsorted();
                        proj.selectAndConfirm(invoker, target);
                        e.getPlayer().closeInventory();
                    } break;
                    case RIGHT:
                        break;
                    case SHIFT_RIGHT:
                        break;
                }
            }
        });
    }

    @Override public NamedElement getNamedLayout() {
        return getLayout();
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
