package com.angaronigiovanni.lpwb_tp_altar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListAltarCommand implements CommandExecutor {
    private AltarLocationListSingleton singleton;

    public ListAltarCommand() {
        this.singleton = AltarLocationListSingleton.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (AltarLocation loc : this.singleton.locations)
            sender.sendMessage(loc.toString());
        return true;
    }
}
