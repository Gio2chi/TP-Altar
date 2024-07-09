package com.angaronigiovanni.lpwb_tp_altar;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class AltarLocation {
    public Location location;
    public String name;
    public UUID uuid;
    public String uniqueIdString;
    public Material material = Material.PAPER;
    public ChatColor color = ChatColor.WHITE;

    public AltarLocation(UUID uuid, Location location) {
        this.uuid = uuid;
        this.uniqueIdString = uuid.toString();
        this.location = location;
        this.name = this.uniqueIdString;
    }
    public AltarLocation(String uniqueIdString, Location location) {
        this.uuid = UUID.fromString(uniqueIdString);
        this.uniqueIdString = uniqueIdString;
        this.location = location;
        this.name = uniqueIdString;
    }

    public String toString() {
        String res = "{";
        res += "\"Name\": \"" + name + "\", \"UUID\": \"" + uniqueIdString + "\", ";
        res += "\"Material\": \"" + material.toString() + "\", "; 
        res += "\"Color\": \"" + color.name() + "\", "; 
        res += " \"Location\": {\"x\": \"" + location.getX() + "\",\"y\": \"" + location.getY() + "\", \"z\": \"" + location.getZ() + "\"}}";
        return res;
    }
}
