package dialight.teams.captain.gui.control;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.view.Fixed9x6View;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ControlView extends Fixed9x6View {

    private final DynamicSlot background;

    public ControlView(ControlGui gui, ControlLayout layout) {
        super(gui, layout, "Управление сортировкой по капитанам");
        background = new DynamicSlot() {
            @Override public void onClick(SlotClickEvent e) {

            }

            @NotNull @Override public ItemStack createItem() {
                ObservableTeam oteam = gui.getProj().getCaptainHandler().getCurrentTeam().getValue();
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
                        .build();
            }
        };
    }

}
