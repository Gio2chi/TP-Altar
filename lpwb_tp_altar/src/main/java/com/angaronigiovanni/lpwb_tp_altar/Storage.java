package com.angaronigiovanni.lpwb_tp_altar;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Storage {
    public FileConfiguration dataConfig;
    private File dataFile;

    public Storage(Plugin plugin, String fileName) {
        // Create the data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        // Initialize the data file
        this.dataFile = new File(plugin.getDataFolder(), fileName);

        // Load the data from the configuration file
        loadData();
        
    }

    private void loadData() {
        // Check if the data file exists
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the data from the configuration file
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveData() {
        // Save data to the configuration file

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
