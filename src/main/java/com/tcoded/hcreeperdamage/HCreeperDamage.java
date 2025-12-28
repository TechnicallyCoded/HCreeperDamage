package com.tcoded.hcreeperdamage;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class HCreeperDamage extends JavaPlugin {

    private PluginConfig config;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Load configuration
        config = PluginConfig.load(this);

        // Register event listener
        getServer().getPluginManager().registerEvents(
                new MobFilterListener(this, config),
                this
        );

        getLogger().info("Configured worlds: " + config.getWorlds());
        getLogger().info("No block damage entities: " + config.getNoBlockDamage());
        getLogger().info("No health damage entities: " + config.getNoHealthDamage());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll(this);
    }
}
