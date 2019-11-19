package dialight.eventhelper.database;

import dialight.database.DbConfig;
import dialight.database.Dependencies;
import dialight.database.DependencyManager;
import dialight.database.ReflectionClassLoader;
import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.TeamsApi;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;

public class Database extends Project {

    private OfflineLibApi offlinelib;
    private TeamsApi teams;

    private final DbConfig config = new DbConfig(this);
    private final DependencyManager dependencyManager = new DependencyManager();
    private final ReflectionClassLoader reflectionClassLoader;

    public Database(JavaPlugin plugin) {
        super(plugin);
        this.reflectionClassLoader = new ReflectionClassLoader(plugin);
    }

    @Override public void enable(EventHelper eh) {
        offlinelib = eh.require("OfflineLib");
        teams = eh.require("Teams");
        config.load();

        File dataFolder = getPlugin().getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        File libsDir = new File(dataFolder, "libs");
        if (!libsDir.exists()) libsDir.mkdirs();
        try {
            Path path = dependencyManager.downloadDependency(libsDir.toPath(), Dependencies.MYSQL_DRIVER);
            reflectionClassLoader.loadJar(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void disable() {
        config.save();
    }

    @Override public ProjectApi getApi() {
        return new DatabaseApi(this);
    }

}
