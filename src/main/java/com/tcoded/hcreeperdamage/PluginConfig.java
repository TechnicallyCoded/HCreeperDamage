package com.tcoded.hcreeperdamage;

import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PluginConfig {

    private final Set<String> worlds;
    private final Set<EntityType> noBlockDamage;
    private final Set<EntityType> noHealthDamage;
    private final boolean creeperDropAllBlocks;

    public static PluginConfig load(HCreeperDamage plugin) {
        // Load worlds list
        List<String> worldsList = plugin.getConfig().getStringList("worlds");
        
        // Load no-block-damage list
        List<String> noBlockDamageList = plugin.getConfig().getStringList("no-block-damage");
        
        // Load no-health-damage list
        List<String> noHealthDamageList = plugin.getConfig().getStringList("no-health-damage");
        
        // New toggle for creeper block drops
        boolean creeperDropAllBlocks = plugin.getConfig().getBoolean("creeper-drop-all-blocks", false);
        
        // Create and return config object
        return new PluginConfig(worldsList, noBlockDamageList, noHealthDamageList, creeperDropAllBlocks, plugin);
    }

    private PluginConfig(List<String> worldsList, List<String> noBlockDamageList, 
                        List<String> noHealthDamageList, boolean creeperDropAllBlocks, HCreeperDamage plugin) {
        this.worlds = new HashSet<>(worldsList);
        
        this.noBlockDamage = new HashSet<>();
        for (String mobName : noBlockDamageList) {
            try {
                EntityType entityType = EntityType.valueOf(mobName.toUpperCase());
                noBlockDamage.add(entityType);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid entity type in no-block-damage: " + mobName);
            }
        }
        
        this.noHealthDamage = new HashSet<>();
        for (String mobName : noHealthDamageList) {
            try {
                EntityType entityType = EntityType.valueOf(mobName.toUpperCase());
                noHealthDamage.add(entityType);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid entity type in no-health-damage: " + mobName);
            }
        }

        this.creeperDropAllBlocks = creeperDropAllBlocks;

        // Validation
        if (worlds.isEmpty()) {
            plugin.getLogger().warning("No worlds configured for explosion damage filtering!");
        }
        if (noBlockDamage.isEmpty() && noHealthDamage.isEmpty()) {
            plugin.getLogger().warning("No entities configured for explosion damage filtering!");
        }
    }

    public Set<String> getWorlds() {
        return worlds;
    }

    public Set<EntityType> getNoBlockDamage() {
        return noBlockDamage;
    }

    public Set<EntityType> getNoHealthDamage() {
        return noHealthDamage;
    }

    public boolean isCreeperDropAllBlocks() {
        return creeperDropAllBlocks;
    }
}