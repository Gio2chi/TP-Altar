package com.angaronigiovanni.lpwb_tp_altar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    public static final Logger LOGGER = Logger.getLogger("tp_altar");
    public Altar altar;

    public void onEnable() {
        Storage data = new Storage(this, "data.yml");
        AltarLocationListSingleton.init(this, data);
        
        Material sacrifice = Material.TRIPWIRE_HOOK;

        Material diamond = Material.DIAMOND_BLOCK;
        Material lodestone = Material.LODESTONE;
        Material cryingOb = Material.CRYING_OBSIDIAN;
        Material quarzSlab = Material.QUARTZ_SLAB;
        Material seaLantern = Material.SEA_LANTERN;
        Material resAncor = Material.RESPAWN_ANCHOR;
        Material brick = Material.POLISHED_BLACKSTONE_BRICK_WALL;

        List<List<List<Material>>> structure = new ArrayList<>(Arrays.asList(
                Arrays.asList(
                        Arrays.asList(diamond, seaLantern, diamond),
                        Arrays.asList(seaLantern, resAncor, seaLantern),
                        Arrays.asList(diamond, seaLantern, diamond)),
                Arrays.asList(
                        Arrays.asList(cryingOb, quarzSlab, cryingOb),
                        Arrays.asList(quarzSlab, lodestone, quarzSlab),
                        Arrays.asList(cryingOb, quarzSlab, cryingOb)),
                Arrays.asList(
                        Arrays.asList(brick, null, brick),
                        Arrays.asList(null, null, null),
                        Arrays.asList(brick, null, brick))));

        

        this.altar = new Altar(structure, new Sacrifice(this, sacrifice, 1, "Passaporta"), new int[] { 1, 1, 1 });

        this.getServer().getPluginManager().registerEvents(new AltarListener(this, this.altar), this);
        this.getCommand("edit").setExecutor(new EditAltarCommand());
        this.getCommand("edit").setTabCompleter(new EditAltarTabCompleter());
        this.getCommand("listAltarInfo").setExecutor(new ListAltarCommand());
        
        LOGGER.info("tp_altar enabled");
    }

    public void onDisable() {
        LOGGER.info("tp_altar disabled");
    }

    public Altar getAltar() {
        return this.altar;
    }

}