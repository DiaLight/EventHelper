package dialight.freezer;

import dialight.compatibility.PlayerInventoryBc;
import dialight.misc.Colorizer;
import dialight.extensions.InventoryEx;
import dialight.misc.ItemStackBuilder;
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
                        "|a|ЛКМ|y|: Открыть редактор",
                        "|a|ПКМ|y|: получить инструмент в активный слот",
                        "|a|Shift|y|+|a|ПКМ|y|: добавить инструмент в инвентарь",
                        "|y| замороженных игроков",
                        ""
                ))
                .addLore(proj.getItemSuffix())
                .build();
    }

    @Override
    public void onClick(SlotClickEvent e) {
        Player player = e.getPlayer();
        switch (e.getEvent().getClick()) {
            case LEFT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
            } break;
            case RIGHT: {
                PlayerInventoryBc.of(player.getInventory()).setItemInMainHand(proj.getTool().createItem());
                proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
            } break;
            case SHIFT_RIGHT: {
                ItemStack item = proj.getTool().createItem();
                if(!InventoryEx.of(e.getPlayer().getInventory()).addToEmptySlot(item)) {
                    e.getPlayer().sendMessage(Colorizer.apply("|r|Не могу добавить итем"));
                }
            } break;
        }
    }

    @NotNull
    @Override
    public ItemStack createItem() {
        return item;
    }
}
