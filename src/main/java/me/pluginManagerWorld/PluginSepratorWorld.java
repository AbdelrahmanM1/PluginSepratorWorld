package me.pluginManagerWorld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PluginSepratorWorld extends JavaPlugin {

    // Maps to hold the enabled and disabled plugins per world
    private Map<String, List<String>> enabledPluginsPerWorld;
    private Map<String, List<String>> disabledPluginsPerWorld;
    private EventListener eventListener;

    @Override
    public void onEnable() {
        // Load config or create default config
        saveDefaultConfig(); // This will copy config.yml from resources to the plugin folder
        loadWorldsToConfig(); // Automatically load worlds to config
        loadPluginsFromConfig(); // Load enabled/disabled plugins for the worlds

        // Register the event listener for handling per-world functionality
        eventListener = new EventListener(this);
        getServer().getPluginManager().registerEvents(eventListener, this);

        getLogger().info("PluginManagerWorld has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginManagerWorld has been disabled!");
    }

    // Command to enable/disable plugins in the current world or show help
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the sender has permission for commands
        if (!player.hasPermission("pluginmanagerworld.admin")) {
            player.sendMessage(getMessage("error.no-permission"));
            return true;
        }

        // If no arguments are provided, show help
        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String worldName = player.getWorld().getName();

        switch (args[0].toLowerCase()) {
            case "help":
                showHelp(player);
                return true;

            case "on":
                if (!player.hasPermission("pluginmanagerworld.enable")) {
                    player.sendMessage(getMessage("error.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("error.specify-plugin"));
                    return true;
                }
                enablePluginInWorld(args[1], worldName);
                player.sendMessage(getMessage("enable.enabled", args[1], worldName));
                return true;

            case "off":
                if (!player.hasPermission("pluginmanagerworld.disable")) {
                    player.sendMessage(getMessage("error.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("error.specify-plugin"));
                    return true;
                }
                disablePluginInWorld(args[1], worldName);
                player.sendMessage(getMessage("disable.disabled", args[1], worldName));
                return true;

            case "disable":
                if (!player.hasPermission("pluginmanagerworld.disable.world")) {
                    player.sendMessage(getMessage("error.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("error.specify-world"));
                    return true;
                }
                disableWorld(args[1]);
                player.sendMessage(getMessage("disable.all", args[1]));
                return true;

            case "enable":
                if (!player.hasPermission("pluginmanagerworld.enable.world")) {
                    player.sendMessage(getMessage("error.no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(getMessage("error.specify-world"));
                    return true;
                }
                enableWorld(args[1]);
                player.sendMessage(getMessage("enable.all", args[1]));
                return true;

            case "plugins":
                // Show enabled/disabled plugins in the player's current world
                eventListener.showPluginsInfo(player);
                return true;

            default:
                player.sendMessage(getMessage("error.invalid-command"));
                return false; // Command not recognized
        }
    }

    // Method to display help information
    private void showHelp(Player player) {
        player.sendMessage(getMessage("help.header"));
        player.sendMessage(getMessage("help.help"));
        player.sendMessage(getMessage("help.list"));
        player.sendMessage(getMessage("help.dump"));
        player.sendMessage(getMessage("help.info"));
        player.sendMessage(getMessage("help.usage"));
        player.sendMessage(getMessage("help.enable"));
        player.sendMessage(getMessage("help.disable"));
        player.sendMessage(getMessage("help.reload"));
        player.sendMessage(getMessage("help.unload"));
        player.sendMessage("Plugin created by Abdelrahman Moharram");
    }

    // Enable a plugin in the specified world
    private void enablePluginInWorld(String pluginName, String worldName) {
        List<String> enabledPlugins = enabledPluginsPerWorld.getOrDefault(worldName, new ArrayList<>());
        if (!enabledPlugins.contains(pluginName)) {
            enabledPlugins.add(pluginName);
            enabledPluginsPerWorld.put(worldName, enabledPlugins);
            savePluginsToConfig();
        }
        // Ensure that it is removed from disabled plugins if previously disabled
        List<String> disabledPlugins = disabledPluginsPerWorld.getOrDefault(worldName, new ArrayList<>());
        disabledPlugins.remove(pluginName);
        disabledPluginsPerWorld.put(worldName, disabledPlugins);
        savePluginsToConfig();
    }

    // Disable a plugin in the specified world
    private void disablePluginInWorld(String pluginName, String worldName) {
        List<String> enabledPlugins = enabledPluginsPerWorld.getOrDefault(worldName, new ArrayList<>());
        if (enabledPlugins.remove(pluginName)) {
            enabledPluginsPerWorld.put(worldName, enabledPlugins);
            savePluginsToConfig();
        }

        // Add to disabled plugins if not already present
        List<String> disabledPlugins = disabledPluginsPerWorld.getOrDefault(worldName, new ArrayList<>());
        if (!disabledPlugins.contains(pluginName)) {
            disabledPlugins.add(pluginName);
            disabledPluginsPerWorld.put(worldName, disabledPlugins);
            savePluginsToConfig();
        }
    }

    // Disable a whole world
    private void disableWorld(String worldName) {
        if (disabledPluginsPerWorld.containsKey(worldName)) {
            getLogger().warning("World " + worldName + " is already disabled.");
            return;
        }
        List<String> disabledPlugins = new ArrayList<>(); // Create a new list for disabled plugins
        disabledPluginsPerWorld.put(worldName, disabledPlugins);
        savePluginsToConfig();
    }

    // Enable a whole world
    private void enableWorld(String worldName) {
        if (!disabledPluginsPerWorld.containsKey(worldName)) {
            getLogger().warning("World " + worldName + " is not disabled.");
            return;
        }
        disabledPluginsPerWorld.remove(worldName); // Remove the world from the disabled list
        savePluginsToConfig();
    }

    // Load enabled and disabled plugins from config
    private void loadPluginsFromConfig() {
        enabledPluginsPerWorld = new HashMap<>();
        disabledPluginsPerWorld = new HashMap<>();

        for (String world : getConfig().getConfigurationSection("worlds").getKeys(false)) {
            enabledPluginsPerWorld.put(world, getConfig().getStringList("worlds." + world + ".enabled-plugins"));
            disabledPluginsPerWorld.put(world, getConfig().getStringList("worlds." + world + ".disabled-plugins"));
        }
    }

    // Save current enabled and disabled plugins to the config
    private void savePluginsToConfig() {
        for (Map.Entry<String, List<String>> entry : enabledPluginsPerWorld.entrySet()) {
            getConfig().set("worlds." + entry.getKey() + ".enabled-plugins", entry.getValue());
        }
        for (Map.Entry<String, List<String>> entry : disabledPluginsPerWorld.entrySet()) {
            getConfig().set("worlds." + entry.getKey() + ".disabled-plugins", entry.getValue());
        }
        saveConfig();
    }

    // Load worlds present on the server and add them to config
    private void loadWorldsToConfig() {
        for (World world : Bukkit.getWorlds()) {
            if (!getConfig().getConfigurationSection("worlds").contains(world.getName())) {
                getConfig().createSection("worlds." + world.getName());
                getConfig().set("worlds." + world.getName() + ".enabled-plugins", new ArrayList<String>());
                getConfig().set("worlds." + world.getName() + ".disabled-plugins", new ArrayList<String>());
            }
        }
        saveConfig();
    }

    // Get enabled plugins for a specific world
    public List<String> getEnabledPluginsForWorld(String worldName) {
        return enabledPluginsPerWorld.getOrDefault(worldName, new ArrayList<>());
    }

    // Get disabled plugins for a specific world
    public List<String> getDisabledPluginsForWorld(String worldName) {
        return disabledPluginsPerWorld.getOrDefault(worldName, new ArrayList<>());
    }

    // Method to get messages from messages.yml
    public String getMessage(String key, String... params) {
        String message = getConfig().getString("messages." + key, key);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                message = message.replace("{" + i + "}", params[i]);
            }
        }
        return message.replace("&", "§"); // Replace '&' with '§' for color formatting
    }
}
