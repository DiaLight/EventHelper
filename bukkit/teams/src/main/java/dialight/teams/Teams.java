package dialight.teams;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.gui.teams.TeamsGui;
import dialight.teams.gui.TeamsSlot;
import dialight.teleporter.TeleporterApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class Teams extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private OfflineLibApi offlinelib;
    private GuiLibApi guilib;
    private TeleporterApi teleporter;

    private Scoreboard scoreboard;
    private TeamsTool tool;
    private TeamsGui gui;
    private TeamsListener listener;

    private final ObservableMap<String, ObservableTeamImpl> teamsMap = new ObservableMapWrapper<>();
    private final ObservableCollection<ObservableTeamImpl> teamsInternal = teamsMap.asCollectionObaervable(ObservableTeamImpl::getName);
    private final ObservableCollection<ObservableTeamImpl> teamsImmutable = teamsMap.asImmutableCollectionObaervable(ObservableTeamImpl::getName);

    private final ObservableMap<UUID, OfflinePlayer> notInTeam = new ObservableMapWrapper<>();
    private final ObservableCollection<OfflinePlayer> notInTeamInternal = notInTeam.asCollectionObaervable(OfflinePlayer::getUniqueId);
    private final ObservableCollection<OfflinePlayer> notInTeamImmutable = notInTeam.asImmutableCollectionObaervable(OfflinePlayer::getUniqueId);

    private final Collection<Consumer<ObservableTeam>> onUpdate = new LinkedList<>();

    public Teams(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        offlinelib = eh.require("OfflineLib");
        guilib = eh.require("GuiLib");
        teleporter = eh.optional("Teleporter");

        offlinelib.getOffline().onAdd(this::onAddOffline);
        offlinelib.getOffline().onRemove(this::onRemoveOffline);

        update();

        scoreboard = getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        tool = new TeamsTool(this);
        gui = new TeamsGui(this);

        maingui.registerToolItem(TeamsTool.ID, new TeamsSlot(this));
        toollib.register(tool);

        listener = new TeamsListener(this);
        listener.start(getPlugin());
    }

    @Override public void disable() {
        listener.stop();
        listener = null;

        offlinelib.getOffline().unregisterOnAdd(this::onAddOffline);
        offlinelib.getOffline().unregisterOnRemove(this::onRemoveOffline);
    }

    private void onAddOffline(OfflinePlayer op) {
        notInTeamInternal.add(op);
    }

    private void onRemoveOffline(OfflinePlayer op) {
        notInTeamInternal.remove(op);
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
                    OfflinePlayer op = getPlugin().getServer().getOfflinePlayer(member);
                    if(op == null) continue;
                    oteam.onAddMember(op);
                    notInTeamToRemove.add(op);
                    notInTeamToAdd.remove(op);
                }
            }
            teamsToRemove.remove(team.getName());
        }
        for (String name : teamsToRemove) {
            teamsMap.remove(name);
        }
        notInTeamInternal.addAll(notInTeamToAdd);
        notInTeamInternal.removeAll(notInTeamToRemove);
    }

    public ObservableCollection<? extends ObservableTeam> getTeamsInternal() {
        return teamsInternal;
    }

    public ObservableCollection<? extends ObservableTeam> getTeamsImmutable() {
        return teamsImmutable;
    }

    public ObservableCollection<OfflinePlayer> getNotInTeamImmutable() {
        return notInTeamImmutable;
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

    public TeamsGui getGui() {
        return gui;
    }

    public TeamsTool getTool() {
        return tool;
    }

    @Nullable public ObservableTeam get(String name) {
        return teamsMap.get(name);
    }

    public void onTeamAdd(String teamName) {
//        System.out.println("onTeamAdd: " + teamName);
        Team team = scoreboard.getTeam(teamName);
        teamsInternal.add(new ObservableTeamImpl(this, team));
    }

    public void onTeamRemove(String teamName) {
//        System.out.println("onTeamRemove: " + teamName);
        ObservableTeamImpl oteam = teamsMap.remove(teamName);
    }

    public void onTeamUpdate(String teamName) {
//        System.out.println("onTeamUpdate: " + teamName);
        ObservableTeamImpl oteam = teamsMap.get(teamName);
        oteam.onUpdate();
        for (Consumer<ObservableTeam> op : onUpdate) {
            op.accept(oteam);
        }
    }

    public void onTeamAddMember(String teamName, String member) {
        OfflinePlayer op = getPlugin().getServer().getOfflinePlayer(member);
        if(op == null) return;
        ObservableTeamImpl oteam = teamsMap.get(teamName);
        oteam.onAddMember(op);
        notInTeamInternal.remove(op);
    }

    public void onTeamRemoveMember(String teamName, String member) {
        OfflinePlayer op = getPlugin().getServer().getOfflinePlayer(member);
        if(op == null) return;
        ObservableTeamImpl oteam = teamsMap.get(teamName);
        oteam.onRemoveMember(op);
        notInTeamInternal.add(op);
    }

    public void onTeamUpdate(Consumer<ObservableTeam> op) {
        onUpdate.add(op);
    }
    public void unregisterOnTeamUpdate(Consumer<ObservableTeam> op) {
        onUpdate.add(op);
    }

}
