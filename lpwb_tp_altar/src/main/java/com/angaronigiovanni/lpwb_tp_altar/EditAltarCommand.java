package com.angaronigiovanni.lpwb_tp_altar;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EditAltarCommand implements CommandExecutor {
    private AltarLocationListSingleton singleton;
    
    public EditAltarCommand(){
        this.singleton = AltarLocationListSingleton.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( args[0].equalsIgnoreCase("name") ){
            if( !(args.length == 3 || args.length == 4) ) {
                Plugin.LOGGER.info("(String) " + args.length);
                sender.sendMessage(ChatColor.RED + "Invalid Syntax: /edit name uuid name");
                return false;
            }

            ChatColor color = ChatColor.WHITE;
            if( args.length == 4 ) {
                try {
                    color = ChatColor.valueOf(args[3].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid ChatColor: " + args[3]);
                    return true;
                }
            }
            for ( AltarLocation loc : this.singleton.locations ) {
                if (loc.uniqueIdString.equals(args[1])) {
                    loc.name = args[2];
                    loc.color = color;
                    this.singleton.update(loc);
                    return true;
                }
            }

            sender.sendMessage(ChatColor.RED + "Invalid UUID");
            return false;

        } else if( args[0].equalsIgnoreCase("material") ) {
            if( args.length != 3 ) {
                sender.sendMessage(ChatColor.RED + "Invalid Syntax: /edit material uuid minecraft:material");
                return false;
            }

            for ( int i = 0; i != this.singleton.locations.size(); i++  ) {
                AltarLocation loc = this.singleton.locations.get(i);
                if (loc.uniqueIdString.equals(args[1])) {
                    Material material = Material.matchMaterial(args[2]);
                    if( material != null ) {
                        loc.material = material;
                        this.singleton.update(loc);
                        return true;
                    }
                    
                    sender.sendMessage(ChatColor.RED + "Invalid Material");
                    return false;
                }
            }

            sender.sendMessage(ChatColor.RED + "Invalid UUID");
            return false;

        } 
        return false;
    }
}
