package dialight.database;

import dialight.eventhelper.database.Database;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DbConfig {

    private final Database proj;
    private final YamlConfiguration config = new YamlConfiguration();
    private ConfigurationSection sql;

    public DbConfig(Database proj) {
        this.proj = proj;
    }

    private ConfigurationSection getOrCreate(ConfigurationSection section, String name) {
        ConfigurationSection out = section.getConfigurationSection(name);
        if(out != null) return out;
        return section.createSection(name);
    }

    private ConfigurationSection getOrCreate(MemorySection section, String name) {
        ConfigurationSection out = section.getConfigurationSection(name);
        if(out != null) return out;
        return section.createSection(name);
    }

    public File getFile() {
        try {
            File dataFolder = proj.getPlugin().getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            File configFile = new File(dataFolder, "database.yml");
            if (!configFile.exists()) proj.getPlugin().saveDefaultConfig();
            return configFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void load() {
        try {
            config.load(getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        sql = getOrCreate(config, "sql");
    }
    public void save() {
        try {
            config.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return sql.getString("address", "localhost:3306");
    }
    public String getDatabase() {
        return sql.getString("database", "EventHelper");
    }
    public String getUsername() {
        return sql.getString("username", "user");
    }
    public String getPassword() {
        return sql.getString("password", "pass");
    }
    public String getTablePrefix() {
        return sql.getString("table_prefix", "eventhelper_");
    }
    public String getOptions() {
        return sql.getString("options", "");
    }

}
