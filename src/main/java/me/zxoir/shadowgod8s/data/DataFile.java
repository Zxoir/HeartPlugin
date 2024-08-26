package me.zxoir.shadowgod8s.data;

import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.managers.HeartManager;
import me.zxoir.shadowgod8s.utils.HeartsUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataFile {
    public FileConfiguration configuration;
    public File configurationFile;

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public void setup() {
        Shadowgod8s mainInstance = Shadowgod8s.getPlugin(Shadowgod8s.class);
        if (!mainInstance.getDataFolder().exists()) {
            mainInstance.getDataFolder().mkdir();
        }

        File dataFile = new File(mainInstance.getDataFolder() + File.separator + "Data" + File.separator);
        this.configurationFile = new File(dataFile.getPath(), "DataFile.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.mkdirs();
                Logger.info("the Data folder has been created!");
            } catch (SecurityException e) {
                Logger.warn("Could not create the Data folder. Error: {}", e.getMessage());
            }
        }

        if (!this.configurationFile.exists()) {
            try {
                this.configurationFile.createNewFile();
                Logger.info("the DataFile.yml file has been created!");
            } catch (IOException e) {
                Logger.trace("Could not create the DataFile.yml file. Error: {}", e.getMessage());
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);

        if (configuration.contains("activeHearts")) {
            for (String key : configuration.getConfigurationSection("activeHearts").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                String heartName = configuration.getString("activeHearts." + key + ".name");
                HeartManager.getActiveHearts().put(uuid, HeartsUtils.getHeartByName(heartName, uuid));
                HeartManager.sendHeartActionBarUpdate();
            }

            configuration.set("activeHearts", null);
            saveConfig();
        }
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

    public void saveConfig() {
        try {
            this.configuration.save(this.configurationFile);
        } catch (IOException localIOException) {
            Logger.trace("Could not save the DataFile.yml file. Error: {}", localIOException.getMessage());
        }
    }

    public void reloadConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
    }
}
