package com.tcoded.hcreeperdamage;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class MobFilterListener implements Listener {

    private final HCreeperDamage plugin;
    private final PluginConfig config;

    public MobFilterListener(HCreeperDamage plugin, PluginConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        // Check if this world is in the configured worlds list
        String worldName = event.getLocation().getWorld().getName();
        if (!config.getWorlds().contains(worldName)) {
            return;
        }

        Entity entity = event.getEntity();
        EntityType entityType = entity.getType();

        // Check if this entity type should not damage blocks
        if (config.getNoBlockDamage().contains(entityType)) {
            event.blockList().clear();
            plugin.getLogger().info("Prevented " + entityType + " from damaging blocks in world " + worldName + ". Location: " + event.getLocation());
            return; // If cleared, no need to continue with drops
        }

        // If enabled, make creepers drop all destroyed blocks
        if (config.isCreeperDropAllBlocks() && entityType == EntityType.CREEPER) {
            event.setYield(100); // Set yield to 100% to guarantee drops
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Only handle explosion damage
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            return;
        }

        // Check if this world is in the configured worlds list
        String worldName = event.getEntity().getWorld().getName();
        if (!config.getWorlds().contains(worldName)) {
            return;
        }

        Entity damager = event.getDamager();
        EntityType entityType = damager.getType();

        // Check if this entity type should not damage health
        if (config.getNoHealthDamage().contains(entityType)) {
            event.setCancelled(true);
            plugin.getLogger().info("Prevented " + entityType + " from damaging " + event.getEntity().getType() + " in world " + worldName);
        }
    }
}