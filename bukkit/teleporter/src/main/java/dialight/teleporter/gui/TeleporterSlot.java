package dialight.teleporter.gui;

import dialight.compatibility.PlayerInventoryBc;
import dialight.misc.Colorizer;
import dialight.extensions.InventoryEx;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teleporter.Teleporter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class TeleporterSlot implements Slot {

    private final Teleporter proj;
    private final ItemStack item;

    public TeleporterSlot(Teleporter proj) {
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.STICK)
                .displayName(Colorizer.apply("|a|Телепорт игроков"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: открыть телепортер",
                        "|a|ПКМ|y|: получить инструмент в активный слот",
                        " |y|и открыть телепортер",
                        "|a|Shift|y|+|a|ПКМ|y|: добавить инструмент в инвентарь",
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
                PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(proj.getTool().createItem());
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

    @Override
    public ItemStack createItem() {
        return item;
    }
}
