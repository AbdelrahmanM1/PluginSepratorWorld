package me.pluginManagerWorld;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class EventListener implements Listener {
    private final PluginSepratorWorld plugin;

    public EventListener(PluginSepratorWorld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        List<String> enabledPlugins = plugin.getEnabledPluginsForWorld(worldName);
        List<String> disabledPlugins = plugin.getDisabledPluginsForWorld(worldName);

        // Check permissions before sending messages about enabled plugins
        if (!enabledPlugins.isEmpty() && player.hasPermission("pluginmanagerworld.info")) {
            player.sendMessage(plugin.getMessage("info.enabled-plugins", String.join(", ", enabledPlugins)));
        }
        // Check permissions before sending messages about disabled plugins
        if (!disabledPlugins.isEmpty() && player.hasPermission("pluginmanagerworld.info")) {
            player.sendMessage(plugin.getMessage("info.disabled-plugins", String.join(", ", disabledPlugins)));
        }
    }

    // Method to display information about enabled and disabled plugins in the player's world
    public void showPluginsInfo(Player player) {
        String worldName = player.getWorld().getName();
        List<String> enabledPlugins = plugin.getEnabledPluginsForWorld(worldName);
        List<String> disabledPlugins = plugin.getDisabledPluginsForWorld(worldName);

        // Check permissions before displaying enabled plugins
        if (!enabledPlugins.isEmpty() && player.hasPermission("pluginmanagerworld.info")) {
            player.sendMessage(plugin.getMessage("list.list", String.valueOf(enabledPlugins.size()), String.join(", ", enabledPlugins)));
        } else if (!player.hasPermission("pluginmanagerworld.info")) {
            player.sendMessage(plugin.getMessage("error.no-permission"));
        } else {
            player.sendMessage(plugin.getMessage("info.no-enabled-plugins"));
        }

        // Check permissions before displaying disabled plugins
        if (!disabledPlugins.isEmpty() && player.hasPermission("pluginmanagerworld.info")) {
            player.sendMessage(plugin.getMessage("list.disabled", String.valueOf(disabledPlugins.size()), String.join(", ", disabledPlugins)));
        } else if (!player.hasPermission("pluginmanagerworld.info")) {
            player.sendMessage(plugin.getMessage("error.no-permission"));
        } else {
            player.sendMessage(plugin.getMessage("info.no-disabled-plugins"));
        }
    }
}
