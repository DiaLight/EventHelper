package dialight.teams.gui.teams;

import dialight.compatibility.PlayerInventoryBc;
import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TeamsLayout extends CachedPageLayout<ObservableTeam> {

    private class TeamSlot implements Slot {

        @NotNull
        private final ObservableTeam oteam;
        private final ItemStack item;

        private TeamSlot(ObservableTeam oteam) {
            this.oteam = oteam;
            this.item = new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                    .leatherArmorColor(ColorConverter.toLeatherColor(TeamBc.of(oteam.getTeam()).getColor()))
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
                    oteam.getTeam().unregister();
                    break;
            }
        }

        @NotNull @Override public ItemStack createItem() {
            return item;
        }

    }

    @NotNull private final Teams proj;

    public TeamsLayout(Teams proj) {
        super(new SparkIndexCache(9, 6));
        this.proj = proj;


        this.setNameFunction(ObservableTeam::getName);
        this.setSlotFunction(TeamSlot::new);

        proj.onTeamUpdate(this::update);

        ObservableCollection<? extends ObservableTeam> teams = proj.getTeamsInternal();
        teams.onAdd(this::add);
        teams.onRemove(this::remove);
        teams.forEach(this::add);
    }

}
