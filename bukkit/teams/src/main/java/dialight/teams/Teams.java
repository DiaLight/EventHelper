package dialight.teams;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.ServerEx;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.observable.set.ObservableSetWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.gui.TeamsLobbyGui;
import dialight.teams.gui.TeamsSlot;
import dialight.teams.gui.playerblacklist.PlayerBlackListGui;
import dialight.teams.gui.sort.SortGui;
import dialight.teams.gui.teams.TeamsGui;
import dialight.teams.gui.whitelist.TeamWhiteListGui;
import dialight.teams.observable.ObservableScoreboardManager;
import dialight.teams.observable.ObservableTeam;
import dialight.teams.observable.inject.ObservableInjectScoreboardManager;
import dialight.teleporter.TeleporterApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Teams extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private OfflineLibApi offlinelib;
    private GuiLibApi guilib;
    private TeleporterApi teleporter;

    private TeamsTool tool;
    private TeamsLobbyGui lobbyGui;
    private SortGui sortGui;
    private TeamsGui teamsGui;
    private TeamWhiteListGui teamWhiteListGui;
    private PlayerBlackListGui playerBlackListGui;

    private final ObservableSet<String> teamWhiteList = new ObservableSetWrapper<>();
    private final ObservableSet<UUID> playerBlackList = new ObservableSetWrapper<>(new HashSet<>());
    private final ObservableObject<Boolean> offlineMode = new ObservableObject<>(false);
    private final Map<String, Location> teamEntryPoints = new HashMap<>();

    private ObservableScoreboardManager scoreboardManager;

    public Teams(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        offlinelib = eh.require("OfflineLib");
        guilib = eh.require("GuiLib");
        teleporter = eh.optional("Teleporter");

        this.scoreboardManager = new ObservableInjectScoreboardManager(offlinelib, getPlugin().getServer().getScoreboardManager());
        this.scoreboardManager.inject();

        tool = new TeamsTool(this);
        lobbyGui = new TeamsLobbyGui(this);
        sortGui = new SortGui(this);
        teamsGui = new TeamsGui(this, scoreboardManager.getMainScoreboard());
        teamWhiteListGui = new TeamWhiteListGui(this, scoreboardManager.getMainScoreboard());
        playerBlackListGui = new PlayerBlackListGui(this);

        maingui.registerToolItem(TeamsTool.ID, new TeamsSlot(this));
        toollib.register(tool);

        // load config
        World mainWorld = ServerEx.of(getPlugin().getServer()).getMainWorld();
        File file = new File(mainWorld.getWorldFolder(), "map.json");
        if(file.exists()) {
            JsonParser parser = new JsonParser();
            try(FileReader reader = new FileReader(file)) {
                JsonObject tree = parser.parse(reader).getAsJsonObject();
                readMapConfig(tree);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void readMapConfig(JsonObject tree) {
        JsonElement helper = tree.get("EventHelper");
        if (helper == null) return;
        JsonObject eventHelper = helper.getAsJsonObject();
        if (eventHelper == null) return;
        JsonObject teams = eventHelper.get("teams").getAsJsonObject();
        if (teams == null) return;
        World mainWorld = ServerEx.of(getPlugin().getServer()).getMainWorld();
        for (Map.Entry<String, JsonElement> entry : teams.entrySet()) {
            String teamName = entry.getKey();
            JsonObject team = entry.getValue().getAsJsonObject();
            JsonArray entryLoc = team.getAsJsonArray("entry");
            if(entryLoc != null && entryLoc.size() == 3) {
                double x = entryLoc.get(0).getAsDouble();
                double y = entryLoc.get(1).getAsDouble();
                double z = entryLoc.get(2).getAsDouble();
                teamEntryPoints.put(teamName, new Location(mainWorld, x, y, z));
            }
            teamWhiteList.add(teamName);
        }
    }

    @Override public void disable() {
        if(this.scoreboardManager != null) {
            this.scoreboardManager.uninject();
            this.scoreboardManager = null;
        }
    }

    @Override public ProjectApi getApi() {
        return new TeamsApi(this);
    }

    public ObservableScoreboardManager getScoreboardManager() {
        return scoreboardManager;
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

    public SortGui getSortGui() {
        return sortGui;
    }

    public TeamsGui getTeamsGui() {
        return teamsGui;
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

    @Deprecated public void onTeamUpdate(Object key, Consumer<ObservableTeam> op) {
        // TODO
//        throw new RuntimeException("delete me");
    }
    public void unregisterOnTeamUpdate(Object key) {
//        throw new RuntimeException("delete me");
    }

    public ObservableSet<String> getTeamWhiteList() {
        return teamWhiteList;
    }

    public ObservableSet<UUID> getPlayerBlackList() {
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

    public Map<String, Location> getTeamEntryPoints() {
        return teamEntryPoints;
    }

}
