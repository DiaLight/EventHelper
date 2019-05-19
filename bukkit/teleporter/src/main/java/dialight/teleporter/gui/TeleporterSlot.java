package dialight.teleporter.gui;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teleporter.Teleporter;
import dialight.teleporter.TeleporterTool;
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
                        "|a|ЛКМ|y|: Получить инструмент",
                        "|a|ПКМ|y|: Открыть редактор",
                        "|y| выбранных игроков",
                        "",
                        "|g|Плагин: |y|Телепорт",
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
                TeleporterTool tool = proj.getToollib().get(TeleporterTool.class);
                if (tool != null) {
                    PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(tool.createItem());
                }
            } break;
            case RIGHT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
            } break;
        }
    }

    @Override
    public ItemStack createItem() {
        return item;
    }
}
