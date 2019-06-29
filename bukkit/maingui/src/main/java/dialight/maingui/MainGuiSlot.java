package dialight.maingui;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.InventoryEx;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.toollib.Tool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MainGuiSlot implements Slot {

    private final ItemStack item;
    private final MainGuiProject proj;

    public MainGuiSlot(MainGuiProject proj) {
        this.proj = proj;
        this.item = new ItemStackBuilder(Material.EMERALD)
                .displayName(Colorizer.apply("|a|EventHelper"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y| или |a|ПКМ|y|: получить инструмент в активный слот",
                        "|a|Shift|y|+|a|ПКМ|y|: добавить инструмент в инвентарь",
                        "|a|Shift|y|+|a|ЛКМ|y|: убрать все инструменты из инвентаря",
                        ""
                ))
                .addLore(proj.getItemSuffix())
                .build();
    }

    @Override
    public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
            case RIGHT: {
                PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(proj.getTool().createItem());
            } break;
            case SHIFT_RIGHT: {
                ItemStack item = proj.getTool().createItem();
                if(!InventoryEx.of(e.getPlayer().getInventory()).addToEmptySlot(item)) {
                    e.getPlayer().sendMessage(Colorizer.apply("|r|Не могу добавить итем"));
                }
            } break;
            case SHIFT_LEFT: {
                boolean found = false;
                PlayerInventory inventory = e.getPlayer().getInventory();
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack cur = inventory.getItem(i);
                    String id = Tool.parseId(cur);
                    if(id != null) {
                        inventory.clear(i);
                        found = true;
                    }
                }
                if(!found) {
                    e.getPlayer().sendMessage(Colorizer.apply("|r|Инструменты не найдены"));
                } else {
                    e.getPlayer().sendMessage(Colorizer.apply("|g|Инструменты очищены"));
                }
            } break;
        }
    }

    @Override
    public ItemStack createItem() {
        return item;
    }

}
