package com.angaronigiovanni.lpwb_tp_altar;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AltarListener implements Listener {
    private Altar altar;
    private Plugin plugin;

    private AltarLocationListSingleton singleton;

    public AltarListener(Plugin plugin, Altar altar) {
        this.altar = altar;
        this.plugin = plugin;

        AltarLocationListSingleton singleton = AltarLocationListSingleton.getInstance();
        this.singleton = singleton;
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlacedBlock(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() == altar.getAltarMaterial()) {
            // Potrebbe non funzionare per strutture asimmetriche
            // la posizione del centro non è aggiornata
            if (!checkAltar(block, altar))
                altar.rotateStructure();
            if (!checkAltar(block, altar))
                altar.rotateStructure();
            if (!checkAltar(block, altar))
                altar.rotateStructure();
            if (!checkAltar(block, altar))
                return;

            event.getPlayer().spawnParticle(Particle.WHITE_SMOKE, block.getLocation().add(0.5, 1, 0.5), 500, 0.25, 1,
                    0.25, 0.01);

            Location location = new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 1,
                    block.getZ() + 0.5);

            
            for (AltarLocation loc : this.singleton.locations) {
                if (loc.location.equals(location)) {
                    TextComponent message = new TextComponent(ChatColor.GREEN + "Structure completed with id: " + ChatColor.BOLD + ChatColor.DARK_RED + loc.uniqueIdString);
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/edit name " + loc.uniqueIdString + " "));
                    message.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "click to change the name of the altar" ).create() ));
                    event.getPlayer().spigot().sendMessage(ChatMessageType.CHAT, message);
                    return;
                }
            }

            UUID uniqueId = UUID.randomUUID();
            String uniqueIdString = uniqueId.toString();

            TextComponent message = new TextComponent(ChatColor.GREEN + "Structure completed with id: " + ChatColor.BOLD + ChatColor.DARK_RED + uniqueIdString);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/edit name " + uniqueIdString + " "));
            message.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "click to change the name of the altar" ).create() ));

            event.getPlayer().spigot().sendMessage(ChatMessageType.CHAT, message);

            AltarLocation aLocation = new AltarLocation(uniqueIdString, location);

            this.singleton.add(aLocation);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        final Item droppedItem = event.getItemDrop();

        if (!altar.sacrifice.isEqual(droppedItem.getItemStack()))
            return;

        // Schedule a repeating task to check if the item has finished falling
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the item is no longer in motion
                if (!droppedItem.isValid() || droppedItem.isOnGround()) {

                    Block block = droppedItem.getLocation().subtract(0, 1, 0).getBlock();
                    // Your code to execute after the item has finished falling
                    if (block.getType().equals(Material.LODESTONE)) {
                        // Potrebbe non funzionare per strutture asimmetriche
                        // la posizione del centro non è aggiornata
                        if (!checkAltar(block, altar))
                            altar.rotateStructure();
                        if (!checkAltar(block, altar))
                            altar.rotateStructure();
                        if (!checkAltar(block, altar))
                            altar.rotateStructure();
                        if (!checkAltar(block, altar))
                            return;
                        

                        Location location = block.getLocation().add(0.5, 1, 0.5);
                        event.getPlayer().spawnParticle(Particle.WHITE_SMOKE, location,
                                500, 0.25, 1,
                                0.25, 0.01);

                        droppedItem.remove();
                        openChoicesMenu(event.getPlayer(), location);
                    }

                    // Cancel the task as it's no longer needed
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Check every tick (1L)

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof MenuHolder) {
            event.setCancelled(true); // Prevent the player from moving or taking items from the inventory

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                ClickType clickType = event.getClick();

                if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) {
                    String choiceName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                    // Perform action based on the selected choice
                    for (AltarLocation loc : this.singleton.locations) {
                        Block underBlock = (new Location(loc.location.getWorld(), loc.location.getX() - 0.5,
                                loc.location.getY() - 1, loc.location.getZ() - 0.5)).getBlock();

                        if (choiceName.equals(loc.name) && underBlock.getType() == altar.getAltarMaterial()) {
                            // Potrebbe non funzionare per strutture asimmetriche
                            // la posizione del centro non è aggiornata
                            if (!checkAltar(underBlock, altar))
                                altar.rotateStructure();
                            if (!checkAltar(underBlock, altar))
                                altar.rotateStructure();
                            if (!checkAltar(underBlock, altar))
                                altar.rotateStructure();
                            if (!checkAltar(underBlock, altar)) {

                                player.sendTitle("Il portale è stato distrutto", "", 0, 0, 0);
                                this.singleton.remove(loc.uniqueIdString);
                                return;
                            }
                            player.teleport(loc.location);
                        }
                    }
                }
            }
        }
    }

    private void openChoicesMenu(Player player, Location location) {
        // Create an inventory for choices
        Inventory choicesMenu = Bukkit.createInventory(new MenuHolder(), 9, "Choose an Option");

        // Add choices to the inventory
        for (AltarLocation loc : this.singleton.locations) {
            if (loc.location.equals(location)){
                continue;
            }
            Block underBlock = (new Location(loc.location.getWorld(), loc.location.getX() - 0.5,
                    loc.location.getY() - 1, loc.location.getZ() - 0.5)).getBlock();
            
            if (underBlock.getType() == altar.getAltarMaterial()) {
                // Potrebbe non funzionare per strutture asimmetriche
                // la posizione del centro non è aggiornata
                if (!checkAltar(underBlock, altar))
                    altar.rotateStructure();
                if (!checkAltar(underBlock, altar))
                    altar.rotateStructure();
                if (!checkAltar(underBlock, altar))
                    altar.rotateStructure();
                if (!checkAltar(underBlock, altar)) {
                    this.singleton.remove(loc.uniqueIdString);
                    return;
                }

                choicesMenu.addItem(createChoiceItem(loc.color + loc.name, loc.material));
            }
        }

        // Open the inventory for the player
        player.openInventory(choicesMenu);
    }

    private static class MenuHolder implements org.bukkit.inventory.InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null; // You can return an inventory here if needed
        }
    }

    private ItemStack createChoiceItem(String choiceName, Material material) {
        // Create an ItemStack for a choice
        // You can customize the ItemStack as per your needs
        // For simplicity, creating a simple paper item here
        ItemStack choiceItem = new ItemStack(material);
        ItemMeta itemMeta = choiceItem.getItemMeta();
        itemMeta.setDisplayName(choiceName);
        choiceItem.setItemMeta(itemMeta);

        return choiceItem;
    }

    private boolean checkAltar(Block block, Altar altar) {
        int offsetX = altar.centerPos[0],
                offsetY = altar.centerPos[1],
                offsetZ = altar.centerPos[2];

        int blockX = block.getX(),
                blockY = block.getY(),
                blockZ = block.getZ();

        int firstBlockX = blockX - offsetX,
                firstBlockY = blockY - offsetY,
                firstBlockZ = blockZ - offsetZ;

        boolean isAltar = true;
        for (int y = 0; y != altar.height && isAltar; y++) {
            for (int x = 0; x != altar.width && isAltar; x++) {
                for (int z = 0; z != altar.depth && isAltar; z++) {
                    Location currPos = new Location(block.getWorld(), firstBlockX + x, firstBlockY + y,
                            firstBlockZ + z);
                    Block currBlock = currPos.getBlock();

                    if (altar.getStructureMaterial(x, y, z) != null
                            && currBlock.getType() != altar.getStructureMaterial(x, y, z))
                        isAltar = false;
                }
            }
        }

        return isAltar;
    }

}