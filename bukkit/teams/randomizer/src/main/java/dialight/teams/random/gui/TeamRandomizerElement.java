package dialight.teams.random.gui;

import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.elements.FixedElement;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.teams.random.SortByRandom;
import org.bukkit.Material;

public class TeamRandomizerElement extends FixedElement {

    private final SortByRandom proj;

    public TeamRandomizerElement(SortByRandom proj) {
        super(9, 6);
        this.proj = proj;

        setSlot(1, 1, new StaticSlot(new ItemStackBuilder(Material.BRICK)
                .displayName(Colorizer.apply("|a|Фильтр команд"))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {

            }
        });
        setSlot(3, 1, new StaticSlot(new ItemStackBuilder(Material.ARROW)
                .displayName(Colorizer.apply("|a|Фильтр команд"))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {

            }
        });
    }

}
