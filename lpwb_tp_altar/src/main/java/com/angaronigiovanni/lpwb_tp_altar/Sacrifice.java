package com.angaronigiovanni.lpwb_tp_altar;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class Sacrifice {
    private Plugin plugin;
    private Material material;
    private int amount;
    private String name;
    private ItemStack stack;

    public Sacrifice(Plugin plugin, Material material, int amount, String name) {
        this.plugin = plugin;
        this.material = material;
        this.amount = amount;
        this.name = name;

        this.stack = new ItemStack(material);

        registerCustomTripwireHookRecipe();
    }

    private void registerCustomTripwireHookRecipe() {
        // Create a unique key for the custom recipe
        NamespacedKey customKey = new NamespacedKey(plugin, "custom_tripwire_hook");

        // Create a shaped recipe for the custom tripwire hook using glowstone blocks
        ShapedRecipe customRecipe = new ShapedRecipe(customKey, createUniqueTripwireHook())
                .shape(" G ", "GTG", " G ")
                .setIngredient('G', Material.GLOWSTONE)
                .setIngredient('T', Material.TRIPWIRE_HOOK);

        // Register the custom recipe
        plugin.getServer().addRecipe(customRecipe);
    }

    private ItemStack createUniqueTripwireHook() {
        ItemStack uniqueTripwireHook = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = uniqueTripwireHook.getItemMeta();

        // Set a custom name for the unique tripwire hook
        meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + this.name);

        // Add a dummy enchantment to hide the enchantment glint
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);

        // Add a custom tag to identify the item as unique
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, this.name), PersistentDataType.BYTE, (byte) 1);

        // Apply the updated ItemMeta to the ItemStack
        uniqueTripwireHook.setItemMeta(meta);
        this.stack.setItemMeta(meta);

        return uniqueTripwireHook;
    }

    public boolean isEqual(ItemStack stack) {
        if (stack == null) return false;

        // Check material type
        if (this.material != stack.getType()) return false;

        // Check display name
        ItemMeta meta1 = this.stack.getItemMeta();
        ItemMeta meta2 = stack.getItemMeta();
        

        if (meta1 == null || meta2 == null || !meta1.getDisplayName().equals(meta2.getDisplayName())) return false;

        // Check custom tag for uniqueness
        NamespacedKey uniqueKey = new NamespacedKey(this.plugin, this.name);
        Byte value = meta2.getPersistentDataContainer().get(uniqueKey, PersistentDataType.BYTE);

        return (value != null && value.byteValue() == 1 && value.byteValue() == 1);
    }

    public int getAmount() {
        return this.amount;
    }
}
