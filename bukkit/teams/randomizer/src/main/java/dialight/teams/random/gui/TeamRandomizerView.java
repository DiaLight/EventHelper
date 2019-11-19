package dialight.teams.random.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.gui.Gui;
import dialight.guilib.elements.SlotElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.Fixed9x6View;
import org.bukkit.DyeColor;

public class TeamRandomizerView extends Fixed9x6View {

    private final Slot background = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
            })
            .displayName(" ")
            .build());

    public TeamRandomizerView(Gui gui, SlotElement layout) {
        super(gui, layout, "Рандомизатор команд");
    }

    @Override protected Slot getBackground(int x, int y) {
        return background;
    }

}
