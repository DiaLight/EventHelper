package dialight.teams.random.gui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.FixedLayout;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.teams.random.TeamRandomizerProject;
import org.bukkit.Material;

public class TeamRandomizerLayout extends FixedLayout {

    private final TeamRandomizerProject proj;

    public TeamRandomizerLayout(TeamRandomizerProject proj) {
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
