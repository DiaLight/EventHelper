package dialight.teams.gui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.InventoryEx;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.Teams;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class TeamsSlot implements Slot {

    private final Teams proj;
    private final ItemStack item;

    public TeamsSlot(Teams proj) {
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder()
                .let(isb -> {
                    ItemStackBuilderBc.of(isb).banner(DyeColor.WHITE);
                })
                .displayName(Colorizer.apply("|a|Команды"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: открыть распределитель",
                        "|a|ПКМ|y|: получить инструмент в активный слот",
                        "|a|Shift|y|+|a|ПКМ|y|: добавить инструмент в инвентарь",
                        ""
                ))
                .addLore(proj.getItemSuffix())
                .nbt("{BlockEntityTag:{Patterns:[" +
                        "{Color:11,Pattern:\"vhr\"}," +
                        "{Color:10,Pattern:\"vh\"}," +
                        "{Color:14,Pattern:\"tr\"}," +
                        "{Color:12,Pattern:\"bl\"}," +
                        "{Color:5,Pattern:\"br\"}," +
                        "{Color:1,Pattern:\"tl\"}" +
                        "]}}")
                .hideMiscellaneous(true)
                .build();
    }

    @Override public void onClick(SlotClickEvent e) {
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

    @NotNull @Override public ItemStack createItem() {
        return item;
    }

}
