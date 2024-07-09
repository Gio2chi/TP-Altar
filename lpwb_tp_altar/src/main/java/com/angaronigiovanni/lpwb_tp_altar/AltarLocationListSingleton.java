package com.angaronigiovanni.lpwb_tp_altar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class AltarLocationListSingleton {
    private static AltarLocationListSingleton instance;

    public List<AltarLocation> locations = new ArrayList<AltarLocation>();
    private Storage data;

    private AltarLocationListSingleton(Plugin plugin, Storage data) {
        this.data = data;

        if (data.dataConfig.contains("locations")) {
            Set<String> locationKeys = data.dataConfig.getConfigurationSection("locations").getKeys(false);

            for (String locationKey : locationKeys) {

                String name = data.dataConfig.getString("locations." + locationKey + ".name");
                World world = plugin.getServer()
                        .getWorld(data.dataConfig.getString("locations." + locationKey + ".world"));
                Material material = Material
                        .getMaterial(data.dataConfig.getString("locations." + locationKey + ".material"));
                ChatColor color;
                try {
                    color = ChatColor.valueOf(data.dataConfig.getString("locations." + locationKey + ".color"));
                } catch (IllegalArgumentException e) {
                    color = ChatColor.WHITE;
                }
                double x = data.dataConfig.getDouble("locations." + locationKey + ".x");
                double y = data.dataConfig.getDouble("locations." + locationKey + ".y");
                double z = data.dataConfig.getDouble("locations." + locationKey + ".z");

                AltarLocation location = new AltarLocation(locationKey, new Location(world, x, y, z));
                location.material = material;
                location.name = name;
                location.color = color;
                locations.add(location);

            }
        }
    }

    public static AltarLocationListSingleton getInstance() {
        return instance;
    }

    public static void init(Plugin plugin, Storage data) {
        if (instance == null)
            instance = new AltarLocationListSingleton(plugin, data);
    }

    public void add(AltarLocation loc) {
        locations.add(loc);

        data.dataConfig.set("locations." + loc.uniqueIdString + ".name", loc.name);
        data.dataConfig.set("locations." + loc.uniqueIdString + ".world", loc.location.getWorld().getName());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".material", loc.material.toString());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".color", loc.color.name());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".x", loc.location.getX());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".y", loc.location.getY());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".z", loc.location.getZ());
        data.saveData();
    }

    public void update(AltarLocation loc) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).location.equals(loc.location)) {
                locations.set(i, loc);
            }
        }

        data.dataConfig.set("locations." + loc.uniqueIdString + ".name", loc.name);
        data.dataConfig.set("locations." + loc.uniqueIdString + ".world", loc.location.getWorld().getName());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".material", loc.material.toString());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".color", loc.color.name());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".x", loc.location.getX());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".y", loc.location.getY());
        data.dataConfig.set("locations." + loc.uniqueIdString + ".z", loc.location.getZ());
        data.saveData();
    }

    public void remove(UUID uuid) {
        String uniqueIdString = uuid.toString();
        // Load the existing array from the configuration
        List<String> existingArray = data.dataConfig.getStringList("locations");

        // Check if the array contains the element to remove
        if (existingArray.contains(uniqueIdString)) {
            // Remove the element
            existingArray.remove(uniqueIdString);

            // Save the modified array back to the configuration
            data.dataConfig.set("locations", existingArray);
            data.saveData();
        }
    }

    public void remove(String uniqueIdString) {
        // Load the existing array from the configuration
        List<String> existingArray = data.dataConfig.getStringList("locations");

        // Check if the array contains the element to remove
        if (existingArray.contains(uniqueIdString)) {
            // Remove the element
            existingArray.remove(uniqueIdString);

            // Save the modified array back to the configuration
            data.dataConfig.set("locations", existingArray);
            data.saveData();
        }
    }

}
