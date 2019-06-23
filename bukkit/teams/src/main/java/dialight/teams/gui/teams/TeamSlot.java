package dialight.teams.gui.teams;


import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TeamSlot implements Slot {

    @NotNull private final Teams proj;
    @NotNull private final ObservableTeam oteam;
    private final ItemStack item;

    public TeamSlot(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        this.oteam = oteam;
        this.item = new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .leatherArmorColor(oteam.getLeatherColor())
                .hideAttributes(true)
                .hideMiscellaneous(true)
                .displayName(Colorizer.apply("|a|" + oteam.getName()))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Получить инструмент",
                        "|y| управления командой",
                        "|a|Shift|y|+|a|ЛКМ|y|: добавить всех в команду",
                        "|a|Shift|y|+|a|ПКМ|y|: очистить команду",
                        "|a|СКМ|y|: удалить команду"
                ))
                .build();
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                e.getPlayer().closeInventory();
                PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(proj.getTool().createItem(oteam.getTeam()));
                break;
            case SHIFT_LEFT:
                for (Player player : proj.getPlugin().getServer().getOnlinePlayers()) {
                    oteam.getTeam().addEntry(player.getName());
                }
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                oteam.getMembers().clear();
                break;
            case MIDDLE:
                proj.runTask(() -> {
                    oteam.getTeam().unregister();
                });
                break;
        }
    }

    public int ti = 1;
    @NotNull @Override public ItemStack createItem() {
        ItemStack item = this.item.clone();
//        item.setAmount(ti);
        return item;
    }

}
