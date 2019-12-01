package dialight.teams.captain;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.misc.ActionInvoker;
import dialight.observable.ObservableObject;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.TeamsApi;
import org.bukkit.plugin.java.JavaPlugin;

public class SortByPriority extends Project {

    private MainGuiApi maingui;
    private GuiLibApi guilib;
    private OfflineLibApi offlinelib;
    private TeamsApi teams;

    private final ObservableObject<SortByPriorityState> state = new ObservableObject<>(SortByPriorityState.NONE);

    public SortByPriority(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        guilib = eh.require("GuiLib");
        offlinelib = eh.require("OfflineLib");
        teams = eh.require("Teams");
        state.setValue(SortByPriorityState.NONE);

        teams.addControlItem(new SortByPrioritySlot(this));

    }

    @Override public void disable() {

    }

    public ObservableObject<SortByPriorityState> getState() {
        return state;
    }

    public void startDistribution(ActionInvoker invoker) {
        if(state.getValue() != SortByPriorityState.NONE) {
            invoker.sendMessage("Распредеение игроков по командам уже находится в состоянии " + state.getValue());
            return;
        }

    }
    public void killDistribution(ActionInvoker invoker) {
        if(state.getValue() == SortByPriorityState.NONE) {
            invoker.sendMessage("Распредеение игроков по командам не запущено");
            return;
        }

    }

    @Override public ProjectApi getApi() {
        return new SortByPriorityApi(this);
    }


    public OfflineLibApi getOfflineLib() {
        return offlinelib;
    }

}
