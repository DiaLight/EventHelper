package dialight.teams.gui.playerblacklist;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerBlackListConfigSlot extends DynamicSlot {

    private final Teams proj;

    public PlayerBlackListConfigSlot(Teams proj) {
        this.proj = proj;
        proj.getOfflineMode().onChange(this::onChangeOfflineMode);
    }

    private void onChangeOfflineMode(Boolean from, Boolean to) {
        updateLater(proj.getPlugin());
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                proj.setOfflineMode(!proj.isOfflineMode());
            } break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStackBuilder builder = new ItemStackBuilder(Material.LEVER)
                .displayName(Colorizer.apply("|a|Настройки"));
        if(proj.isOfflineMode()) {
            builder.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: Не учитывать офлайн игроков"
            ));
        } else {
            builder.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: Учитывать офлайн игроков"
            ));
        }
        return builder.build();
    }

}
