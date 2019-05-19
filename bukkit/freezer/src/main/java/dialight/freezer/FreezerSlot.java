package dialight.freezer;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class FreezerSlot implements Slot {

    private final Freezer proj;
    private final ItemStack item;

    public FreezerSlot(Freezer proj) {
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.ICE)
                .displayName(Colorizer.apply("|a|Замораживатель игроков"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: получить инструмент",
                        "|a|ПКМ|y|: Открыть редактор",
                        "|y| замороженных игроков",
                        "",
                        "|g|Плагин: |y|Замораживатель",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build();
    }

    @Override
    public void onClick(SlotClickEvent e) {
        Player player = e.getPlayer();
        switch (e.getEvent().getClick()) {
            case LEFT:
            case SHIFT_LEFT: {
                player.closeInventory();
                FreezerTool tool = proj.getToollib().get(FreezerTool.class);
                if(tool != null) {
                    PlayerInventoryBc.of(player.getInventory()).setItemInMainHand(tool.createItem());
                }
            } break;
            case RIGHT: {
//                proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
            } break;
        }
    }

    @NotNull
    @Override
    public ItemStack createItem() {
        return item;
    }
}
