package dialight.teams.captain;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.fake.FakeBossBar;
import dialight.freezer.FreezerApi;
import dialight.guilib.GuiLibApi;
import dialight.guilib.gui.Gui;
import dialight.maingui.MainGuiApi;
import dialight.misc.ActionInvoker;
import dialight.misc.player.UuidPlayer;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.stateengine.StateEngine;
import dialight.teams.TeamsApi;
import dialight.teams.captain.gui.control.ControlGui;
import dialight.teams.captain.gui.results.ResultsGui;
import dialight.teams.captain.gui.select.SelectMemberGui;
import dialight.teams.captain.state.*;
import dialight.teams.captain.tool.SortByCaptainTool;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teleporter.TeleporterApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SortByCaptain extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private GuiLibApi guilib;
    private OfflineLibApi offlineLib;
    private TeamsApi teams;
    private FreezerApi freezer;
    private TeleporterApi teleporter;

    private ObservableScoreboard scoreboard;

    private NoneHandler noneHandler;
    private CollectMembersHandler membersHandler;
    private BuildArenaHandler arenaHandler;
    private SelectNextCaptainHandler captainHandler;
    private SelectNextMemberHandler memberHandler;
    private StateEngine<SortByCaptainState> stateEngine;

    private final ObservableMap<String, UuidPlayer> captainsByTeam = new ObservableMapWrapper<>();
    private final ObservableMap<String, TeamSortResult> sortResult = new ObservableMapWrapper<>();
    private SortByCaptainHandler sortByCaptainHandler;

    private SortByCaptainTool tool;
    private SelectMemberGui gui;
    private ControlGui controlGui;
    private ResultsGui resultsGui;

    private FakeBossBar fakeBossBar;

    public SortByCaptain(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        guilib = eh.require("GuiLib");
        offlineLib = eh.require("OfflineLib");
        teams = eh.require("Teams");
        freezer = eh.require("Freezer");
        teleporter = eh.require("Teleporter");

        fakeBossBar = new FakeBossBar(getPlugin());

        // state engine init
        stateEngine = new StateEngine<>(getPlugin(),
                (noneHandler = new NoneHandler(this)),
                SortByCaptainState.COLLECT_MEMBERS
        );
        stateEngine.addHandler(noneHandler = new NoneHandler(this));
        stateEngine.addHandler(membersHandler = new CollectMembersHandler(this));
        stateEngine.addHandler(arenaHandler = new BuildArenaHandler(this));
        stateEngine.addHandler(captainHandler = new SelectNextCaptainHandler(this));
        stateEngine.addHandler(memberHandler = new SelectNextMemberHandler(this));

        offlineLib.getOnline().onAdd(this, player -> {
            UuidPlayer uuidPlayer = offlineLib.getUuidPlayer(player);
            if (membersHandler.isMember(uuidPlayer)) {
                player.setScoreboard(scoreboard.asBukkit());
                if(uuidPlayer.equals(captainHandler.getCurrentCaptain().getValue())) {
                    player.getInventory().clear();
                    player.setItemInHand(tool.createItem(captainHandler.getCurrentTeam().getValue()));
                }
            }
        });

        scoreboard = teams.getScoreboardManager().getOrCreate("sort-by-captain");

        sortByCaptainHandler = new SortByCaptainHandler(this);
        sortByCaptainHandler.setup();

        teams.addControlItem(new SortByCaptainSlot(this));
        gui = new SelectMemberGui(this);
        controlGui = new ControlGui(this);
        resultsGui = new ResultsGui(this);
        tool = new SortByCaptainTool(this);
        toollib.register(tool);
    }

    @Override public void disable() {
        stateEngine.setHandler(SortByCaptainState.NONE);
    }

    public ObservableMap<String, TeamSortResult> getSortResult() {
        return sortResult;
    }

    public FakeBossBar getFakeBossBar() {
        return fakeBossBar;
    }

    public TeamsApi getTeams() {
        return teams;
    }

    public OfflineLibApi getOfflineLib() {
        return offlineLib;
    }

    public FreezerApi getFreezer() {
        return freezer;
    }

    public TeleporterApi getTeleporter() {
        return teleporter;
    }

    public ObservableMap<String, UuidPlayer> getCaptainsByTeam() {
        return captainsByTeam;
    }

    public ObservableScoreboard getScoreboard() {
        return scoreboard;
    }

    public NoneHandler getNoneHandler() {
        return noneHandler;
    }

    public CollectMembersHandler getMembersHandler() {
        return membersHandler;
    }

    public BuildArenaHandler getArenaHandler() {
        return arenaHandler;
    }

    public SelectNextCaptainHandler getCaptainHandler() {
        return captainHandler;
    }

    public SelectNextMemberHandler getMemberHandler() {
        return memberHandler;
    }

    @Override public ProjectApi getApi() {
        return new SortByCaptainApi(this);
    }

    public StateEngine<SortByCaptainState> getStateEngine() {
        return stateEngine;
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

    public ToolLibApi getToollib() {
        return toollib;
    }

    public Gui getGui() {
        return gui;
    }

    public ControlGui getControlGui() {
        return controlGui;
    }

    public ResultsGui getResultsGui() {
        return resultsGui;
    }

    public SortByCaptainTool getTool() {
        return tool;
    }

    public void select(ActionInvoker invoker, UuidPlayer uuidPlayer) {
        if(!validateInvoker(invoker)) return;
        UuidPlayer oldSelected = memberHandler.getSelected().getValue();
        if(Objects.equals(oldSelected, uuidPlayer)) {
            memberHandler.getSelected().setValue(null);
        } else {
            memberHandler.getSelected().setValue(uuidPlayer);
        }
    }

    public void selectAndConfirm(ActionInvoker invoker, UuidPlayer uuidPlayer) {
        if(!validateInvoker(invoker)) return;
        memberHandler.selectAndConfirm(uuidPlayer);
    }

    public boolean validateInvoker(ActionInvoker invoker) {
        UuidPlayer captain = this.getCaptainHandler().getCurrentCaptain().getValue();
        boolean isCaptain = captain.equals(invoker.getPlayer());
        boolean isMaster = isMaster(invoker.getPlayer());
        if(!isCaptain && !isMaster) {
            invoker.sendMessage(CaptainMessages.notSelector);
            return false;
        }
        return true;
    }

    public boolean isMaster(UuidPlayer player) {
        UuidPlayer up = stateEngine.getInvoker().getPlayer();
        if(up == null) return false;
        return up.equals(player);
    }

}
