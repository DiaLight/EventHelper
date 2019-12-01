package dialight.teams.captain.gui.control;

import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.extensions.PlayerEx;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.player.UuidPlayer;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.CaptainMessages;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;


public class ControlSlot extends DynamicSlot {

    private final SortByCaptain proj;

    public ControlSlot(SortByCaptain proj) {
        this.proj = proj;
        proj.getStateEngine().getHandler().onChange(this, this::changeState);
    }

    private void changeState(StateEngineHandler oldState, StateEngineHandler newState) {
        update();
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                UuidPlayer up = proj.getOfflineLib().getUuidPlayer(e.getPlayer());
                proj.getStateEngine().start(new ActionInvoker(up));
            } break;
            case SHIFT_LEFT: {
                UuidPlayer up = proj.getOfflineLib().getUuidPlayer(e.getPlayer());
                proj.getStateEngine().kill(new ActionInvoker(up));
            } break;
            case RIGHT:

                break;
            case SHIFT_RIGHT:
                if (proj.getStateEngine().getHandler().getValue().getState() != SortByCaptainState.NONE) {
                    PlayerEx.of(e.getPlayer()).teleport(proj.getArenaHandler().getLocation());
                } else {
                    e.getPlayer().sendMessage(CaptainMessages.arenaNotBuilt);
                }
                break;
        }
    }

    @NotNull
    @Override public ItemStack createItem() {
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ItemStackBuilder isb = new ItemStackBuilder(Material.BEACON);
        isb.displayName(Colorizer.apply("|a|Сортировка по капитанам"));

        isb.addLore(Colorizer.asList(
                "|a|ЛКМ|y|: запустить сортировку по капитанам",
                "|a|Shift|y|+|a|ЛКМ|y|: насильно остановить сортировку",
                "|a|Shift|y|+|a|ПКМ|y|: телепортироваться к арене",
                "|g|state: |y|" + proj.getStateEngine().getHandler().getValue().getState(),
                "",
                "|g|Плагин: |y|Сортировка по капитанам",
                "|g|Версия: |y|v" + desc.getVersion()
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}

