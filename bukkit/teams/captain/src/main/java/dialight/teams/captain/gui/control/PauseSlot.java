package dialight.teams.captain.gui.control;

import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.player.UuidPlayer;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.CaptainMessages;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class PauseSlot extends DynamicSlot {

    private final SortByCaptain proj;

    public PauseSlot(SortByCaptain proj) {
        this.proj = proj;
        proj.getStateEngine().getHandler().onChange(this, this::changeState);
    }

    private void changeState(StateEngineHandler oldState, StateEngineHandler newState) {
        update();
    }

    @Override public void onClick(SlotClickEvent e) {
        if (proj.getStateEngine().getHandler().getValue().getState() == SortByCaptainState.NONE) {
            e.getPlayer().sendMessage(CaptainMessages.notStarted);
            return;
        }
        switch (e.getEvent().getClick()) {
            case LEFT: {
                UuidPlayer up = proj.getOfflineLib().getUuidPlayer(e.getPlayer());
                proj.getMemberHandler().getPause().setValue(new ActionInvoker(up), true);
            } break;
            case SHIFT_LEFT:
                break;
            case RIGHT: {
                UuidPlayer up = proj.getOfflineLib().getUuidPlayer(e.getPlayer());
                proj.getMemberHandler().getPause().setValue(new ActionInvoker(up), false);
            } break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStackBuilder isb = new ItemStackBuilder(Material.CAKE);
        isb.displayName(Colorizer.apply("|a|Пауза"));
        isb.addLore(Colorizer.asList(
                "|a|ЛКМ|y|: приостановить сортировку по капитанам",
                "|a|ПКМ|y|: возобновить сортировку",
                "|g|pause_state: |y|" + proj.getMemberHandler().getPause().getValue()
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}

