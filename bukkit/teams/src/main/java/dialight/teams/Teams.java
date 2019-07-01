package dialight.teams;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.observable.ObservableObject;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.collection.ObservableCollectionWrapper;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.event.TeamEvent;
import dialight.teams.gui.playerblacklist.PlayerBlackListGui;
import dialight.teams.gui.whitelist.TeamWhiteListGui;
import dialight.teams.gui.TeamsSlot;
import dialight.teams.gui.control.ControlGui;
import dialight.teams.gui.teams.TeamsGui;
import dialight.teleporter.TeleporterApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Teams extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private OfflineLibApi offlinelib;
    private GuiLibApi guilib;
    private TeleporterApi teleporter;

    private Scoreboard scoreboard;
    private TeamsTool tool;
    private ControlGui controlGui;
    private TeamsGui gui;
    private TeamWhiteListGui teamWhiteListGui;
    private PlayerBlackListGui playerBlackListGui;
    private TeamsListener listener;

    private final ObservableMap<String, ObservableTeamImpl> teamsMap = new ObservableMapWrapper<>();
    private final ObservableCollection<ObservableTeamImpl> teamsInternal = teamsMap.asCollectionObaervable(ObservableTeamImpl::getName);
    private final ObservableCollection<ObservableTeamImpl> teamsImmutable = teamsMap.asImmutableCollectionObaervable(ObservableTeamImpl::getName);

    private final Collection<Consumer<ObservableTeam>> onUpdate = new LinkedList<>();
    private final Collection<BiConsumer<ObservableTeam, String>> onMemberJoin = new LinkedList<>();
    private final Collection<BiConsumer<ObservableTeam, String>> onMemberLeave = new LinkedList<>();

    private final ObservableCollection<String> teamWhiteList = new ObservableCollectionWrapper<>(new HashSet<>());
    private final ObservableCollection<UUID> playerBlackList = new ObservableCollectionWrapper<>(new HashSet<>());
    private final ObservableObject<Boolean> offlineMode = new ObservableObject<>(false);

    public Teams(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        offlinelib = eh.require("OfflineLib");
        guilib = eh.require("GuiLib");
        teleporter = eh.optional("Teleporter");

        update();

        scoreboard = getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        tool = new TeamsTool(this);
        controlGui = new ControlGui(this);
        gui = new TeamsGui(this);
        teamWhiteListGui = new TeamWhiteListGui(this);
        playerBlackListGui = new PlayerBlackListGui(this);

        maingui.registerToolItem(TeamsTool.ID, new TeamsSlot(this));
        toollib.register(tool);

        listener = new TeamsListener(this);
    }

    @Override public void disable() {
        if(listener != null) {
            listener.stop();
            listener = null;
        }
    }

    @Override public ProjectApi getApi() {
        return new TeamsApi(this);
    }

    public void update() {
        List<OfflinePlayer> notInTeamToAdd = new ArrayList<>(offlinelib.getOffline());
        List<OfflinePlayer> notInTeamToRemove = new ArrayList<>();
        Scoreboard sb = getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        List<String> teamsToRemove = new ArrayList<>(teamsMap.keySet());
        for (Team team : sb.getTeams()) {
            ObservableTeamImpl oteam = new ObservableTeamImpl(this, team);
            if(teamsMap.putIfAbsent(team.getName(), oteam) == null) {
                for (String member : team.getEntries()) {
                    OfflinePlayer op = offlinelib.getOfflinePlayerByName(member);
                    if(op != null) {
                        oteam.onAddMember(op);
                        notInTeamToRemove.add(op);
                        notInTeamToAdd.remove(op);
                    }
                }
            }
            teamsToRemove.remove(team.getName());
        }
        for (String name : teamsToRemove) {
            teamsMap.remove(name);
        }
    }

    @SuppressWarnings("unchecked")
    public ObservableCollection<ObservableTeam> getTeamsInternal() {
        return (ObservableCollection<ObservableTeam>) (ObservableCollection) teamsInternal;
    }

    @SuppressWarnings("unchecked")
    public ObservableCollection<ObservableTeam> getTeamsImmutable() {
        return (ObservableCollection<ObservableTeam>) (ObservableCollection) teamsImmutable;
    }

    public TeamsListener getListener() {
        return listener;
    }

    public MainGuiApi getMaingui() {
        return maingui;
    }

    public ToolLibApi getToollib() {
        return toollib;
    }

    public OfflineLibApi getOfflinelib() {
        return offlinelib;
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

    @Nullable public TeleporterApi getTeleporter() {
        return teleporter;
    }

    public ControlGui getControlGui() {
        return controlGui;
    }

    public TeamsGui getGui() {
        return gui;
    }

    public TeamWhiteListGui getTeamWhiteListGui() {
        return teamWhiteListGui;
    }

    public PlayerBlackListGui getPlayerBlackListGui() {
        return playerBlackListGui;
    }

    public TeamsTool getTool() {
        return tool;
    }

    @Nullable public ObservableTeam get(String name) {
        return teamsMap.get(name);
    }

    public void onTeamUpdate(Consumer<ObservableTeam> op) {
        onUpdate.add(op);
    }
    public void unregisterOnTeamUpdate(Consumer<ObservableTeam> op) {
        onUpdate.add(op);
    }
    public void onMemberJoin(BiConsumer<ObservableTeam, String> op) {
        onMemberJoin.add(op);
    }
    public void unregisterOnMemberJoin(BiConsumer<ObservableTeam, String> op) {
        onMemberJoin.remove(op);
    }
    public void onMemberLeave(BiConsumer<ObservableTeam, String> op) {
        onMemberLeave.add(op);
    }
    public void unregisterOnMemberLeave(BiConsumer<ObservableTeam, String> op) {
        onMemberLeave.remove(op);
    }

    public ObservableCollection<String> getTeamWhiteList() {
        return teamWhiteList;
    }

    public ObservableCollection<UUID> getPlayerBlackList() {
        return playerBlackList;
    }

    public ObservableObject<Boolean> getOfflineMode() {
        return offlineMode;
    }
    public boolean isOfflineMode() {
        return offlineMode.getValue();
    }
    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode.setValue(offlineMode);
    }


    public void onTeamEvent(TeamEvent event) {
        switch (event.getType()) {
            case ADD: {
                Team team = scoreboard.getTeam(event.getName());
                ObservableTeamImpl ot = new ObservableTeamImpl(this, team);
                if(!teamsInternal.contains(ot)) teamsInternal.add(ot);
            } break;
            case REMOVE: {
                ObservableTeamImpl oteam = teamsMap.remove(event.getName());
            } break;
            case UPDATE: {
                ObservableTeamImpl ot = teamsMap.get(event.getName());
                ot.onUpdate();
                for (Consumer<ObservableTeam> op : onUpdate) {
                    op.accept(ot);
                }
            } break;
            case ADD_MEMBERS: {
                ObservableTeamImpl oteam = teamsMap.get(event.getName());
                TeamEvent.Members membersEvent = (TeamEvent.Members) event;
//                System.out.println("ADD_MEMBERS " + event.getName() + " " + membersEvent.getMembers());
                for (String member : membersEvent.getMembers()) {
                    OfflinePlayer op = offlinelib.getOfflinePlayerByName(member);
                    if(op != null) {
                        oteam.onAddMember(op);
                    }
                    for (BiConsumer<ObservableTeam, String> consumer : onMemberJoin) {
                        consumer.accept(oteam, member);
                    }
                }
            } break;
            case REMOVE_MEMBERS: {
                ObservableTeamImpl oteam = teamsMap.get(event.getName());
                TeamEvent.Members membersEvent = (TeamEvent.Members) event;
//                System.out.println("REMOVE_MEMBERS " + event.getName() + " " + membersEvent.getMembers());
                for (String member : membersEvent.getMembers()) {
                    OfflinePlayer op = offlinelib.getOfflinePlayerByName(member);
                    if (op != null) {
                        oteam.onRemoveMember(op);
                    }
                    for (BiConsumer<ObservableTeam, String> consumer : onMemberLeave) {
                        consumer.accept(oteam, member);
                    }
                }
            } break;
        }
    }

}
