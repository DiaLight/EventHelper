package dialight.maingui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class MainGuiSlot implements Slot {

    private final ItemStack item;
    private final MainGuiProject proj;

    public MainGuiSlot(MainGuiProject proj) {
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.EMERALD)
                .displayName(Colorizer.apply("|a|EventHelper"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: получить инструмент",
                        "|a|ПКМ|y|: добавить инструмент в инвентарь",
                        "",
                        "|g|Плагин: |y|EventHelper",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .build();
    }

    @Override
    public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
            case SHIFT_LEFT: {
                e.getPlayer().closeInventory();
                proj.getToollib().giveTool(e.getPlayer(), MainGuiTool.ID);
            } break;
            case RIGHT: {
                proj.getToollib().giveTool(e.getPlayer(), MainGuiTool.ID);
            }
        }
    }

    @Override
    public ItemStack createItem() {
        return item;
    }

}
