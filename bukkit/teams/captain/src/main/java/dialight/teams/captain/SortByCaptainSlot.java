package dialight.teams.captain;

import dialight.extensions.PlayerEx;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.stateengine.StateEngineHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class SortByCaptainSlot extends DynamicSlot {

    private final SortByCaptain proj;

    public SortByCaptainSlot(SortByCaptain proj) {
        this.proj = proj;
        proj.getStateEngine().getHandler().onChange(this, this::changeState);
    }

    private void changeState(StateEngineHandler oldState, StateEngineHandler newState) {
        update();
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getControlGui());
            } break;
            case SHIFT_LEFT: {
            } break;
            case RIGHT:
                proj.getGuilib().openGui(e.getPlayer(), proj.getControlGui());
                break;
            case SHIFT_RIGHT:
                PlayerEx.of(e.getPlayer()).teleport(proj.getArenaHandler().getLocation());
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ItemStackBuilder isb = new ItemStackBuilder(Material.BEACON);
        isb.displayName(Colorizer.apply("|a|Сортировка по капитанам"));



        isb.addLore(Colorizer.asList(
                "|a|ЛКМ|y|: открыть гуи",
                "|a|ПКМ|y|: открыть гуи",
//                "|a|Shift|y|+|a|ПКМ|y|: что-то еще",
                "|g|state: |y|" + proj.getStateEngine().getHandler().getValue().getState(),
                "",
                "|g|Плагин: |y|Сортировка по капитанам",
                "|g|Версия: |y|v" + desc.getVersion()
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}
