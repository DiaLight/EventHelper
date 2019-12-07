package dialight.teams.gui.teams;


import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.InventoryEx;
import dialight.guilib.gui.Gui;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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
                .displayName(oteam.color().getValue() + Colorizer.apply("⬛ |w|" + oteam.getName()))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: открыть управление командой",
                        "|a|ПКМ|y|: получить инструмент в активный слот",
                        "|a|Shift|y|+|a|ЛКМ|y|: добавить всех в команду",
                        "|a|Shift|y|+|a|ПКМ|y|: добавить инструмент в инвентарь",
                        "|a|СКМ|y|: удалить команду"
                ))
                .build();
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                Gui teamGui = proj.getTeamsGui().teamGui(oteam.getName());
                if (teamGui != null) {
                    proj.getGuilib().openGui(e.getPlayer(), teamGui);
                }
            } break;
            case RIGHT: {
                Gui teamGui = proj.getTeamsGui().teamGui(oteam.getName());
                if (teamGui != null) {
                    proj.getGuilib().openGui(e.getPlayer(), teamGui);
                }
                ItemStack item = proj.getTool().createItem(oteam.getTeam());
                PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(item);
            } break;
            case SHIFT_LEFT:
                for (Player player : proj.getPlugin().getServer().getOnlinePlayers()) {
                    oteam.getTeam().addEntry(player.getName());
                }
                break;
            case SHIFT_RIGHT: {
                ItemStack item = proj.getTool().createItem(oteam.getTeam());
                if(!InventoryEx.of(e.getPlayer().getInventory()).addToEmptySlot(item)) {
                    e.getPlayer().sendMessage(Colorizer.apply("|r|Не могу добавить итем"));
                }
            } break;
            case MIDDLE:
                proj.runTask(() -> {
                    oteam.getTeam().unregister();
                });
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStack item = this.item.clone();
        ObservableCollection<UuidPlayer> members = oteam.getMembers();
        item.setAmount(members.size());
        ItemStackBuilder isb = new ItemStackBuilder(item);
        if(members.isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|В команде нет игроков"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Игроки в команде:"
            ));
            Iterator<UuidPlayer> iterator = members.iterator();
            int previewSize = 8;
            for (int i = 0; i < previewSize && iterator.hasNext(); i++) {
                UuidPlayer up = iterator.next();
                String name = up.getName();
                isb.addLore(Colorizer.asList(
                        oteam.color().getValue() + "⬛ |w|" + name
                ));
            }
            if (members.size() > previewSize) {
                int left = members.size() - previewSize;
                isb.addLore(Colorizer.asList(
                        "|g|и еще |w|" + left + "|g| игроков"
                ));
            }
        }
        return isb.build();
    }

}
