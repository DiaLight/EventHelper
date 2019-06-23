package dialight.teams.random;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.ObservableTeam;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class TeamRandomizerSlot extends DynamicSlot {

    private final TeamRandomizerProject proj;

    public TeamRandomizerSlot(TeamRandomizerProject proj) {
        this.proj = proj;
        proj.getFilter().onAdd(this::update);
        proj.getFilter().onRemove(this::update);
    }

    private void update(String name) {
        update();
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                proj.doRandomize();
                break;
            case SHIFT_LEFT:
                break;
            case RIGHT:
                proj.getGuilib().openGui(e.getPlayer(), proj.getFilterGui());
                break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ItemStackBuilder isb = new ItemStackBuilder(Material.EYE_OF_ENDER)
                .displayName(Colorizer.apply("|a|Рандомизатор команд"));

        isb.lore(Colorizer.asList(
                "|a|ЛКМ|y|: рандомизировать игроков",
                "|a|ПКМ|y|: фильтр команд",
                ""
        ));
        if(proj.getFilter().isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|Команды для рандомизации:",
                    " |a|все команды"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Команды для рандомизации:"
            ));
            for (String name : proj.getFilter()) {
                ObservableTeam team = proj.getTeams().get(name);
                if(team != null) {
                    isb.addLore(Colorizer.asList(
                            "|g|- " + team.getColor() + name
                    ));
                } else {
                    isb.addLore(Colorizer.asList(
                            "|g|- |a|" + name
                    ));
                }
            }
        }
        isb.addLore(Colorizer.asList(
                "",
                "|g|Плагин: |y|Рандомизатор команд",
                "|g|Версия: |y|v" + desc.getVersion()
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}
