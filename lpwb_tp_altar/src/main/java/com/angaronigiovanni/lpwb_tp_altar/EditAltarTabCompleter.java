package com.angaronigiovanni.lpwb_tp_altar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class EditAltarTabCompleter implements TabCompleter {
    
    private AltarLocationListSingleton singleton;

    public EditAltarTabCompleter() {
        this.singleton = AltarLocationListSingleton.getInstance();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        if( args.length == 1) {
            list.add("name");
            list.add("material");
        }
        if( args.length == 2 ) {
            for (AltarLocation loc : this.singleton.locations )
                list.add( loc.uniqueIdString );
        }
        if( args[0].equalsIgnoreCase("name") && args.length == 4){
            String input = args[3].toLowerCase();

            for (ChatColor color : ChatColor.values()) {
                String colorName = color.name().toLowerCase();
                if (colorName.startsWith(input)) {
                    list.add(colorName);
                }
            }

            // Sort the list for better readability
            Collections.sort(list);
        }
        if( args[0].equalsIgnoreCase("material") && args.length == 3){
            String input = args[2].toLowerCase();

            for (Material material : Material.values()) {
                String materialName = material.name().toLowerCase();
                if (materialName.startsWith(input)) {
                    list.add("minecraft:" + materialName);
                }
            }

            // Sort the list for better readability
            Collections.sort(list);
        }
        return list;
    }
    
}
