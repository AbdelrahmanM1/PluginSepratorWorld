# PluginManagerWorld

**PluginManagerWorld** is a powerful Minecraft plugin that allows server administrators to manage plugins per world. You can enable or disable specific plugins in different worlds, which helps you tailor your server’s behavior based on each world. It also provides a simple command interface for managing these settings.

This plugin is useful for managing large servers with multiple worlds, allowing you to configure each world’s plugin environment independently.

---

## Installation

1. **Download the Plugin**:  
   Download the latest version of **PluginManagerWorld** from the plugin's official repository or website.

2. **Place the Plugin in the Plugins Folder**:  
   Move the `.jar` file to your server's `plugins` folder. The path should look something like this:

3. **Restart or Reload Your Server**:  
After placing the plugin in the `plugins` folder, restart your server or use the `/reload` command if the plugin was already loaded.

4. **Check for Config Files**:  
Upon first launch, the plugin will generate default `config.yml` and `messages.yml` files in the `PluginManagerWorld` folder. You can modify these files to customize your settings and messages.

---

## Features

- **World-Specific Plugin Management**: Enables or disables plugins in specific worlds.
- **Command Interface**: Allows server admins to manage plugin states using simple commands.
- **Per-World Plugin Information**: Shows a list of enabled and disabled plugins in the current world.
- **Permission-Based Access**: Provides customizable permissions to control who can manage plugins.
- **Automatic World Configuration**: When the plugin is enabled, it will automatically create world-specific configuration entries.

---

## Commands

Here’s a list of commands you can use in the plugin:

- `/pmw help` - Show a list of available commands.
- `/pmw on <plugin_name>` - Enable a specific plugin in the current world.
- `/pmw off <plugin_name>` - Disable a specific plugin in the current world.
- `/pmw enable <world_name>` - Enable all plugins in a specific world.
- `/pmw disable <world_name>` - Disable all plugins in a specific world.
- `/pmw plugins` - Show the enabled and disabled plugins in the current world.

---

## Permissions

### Available Permissions

- `pluginmanagerworld.admin`: Allows the player to manage plugins in worlds (Op permission).
- `pluginmanagerworld.enable`: Allows enabling plugins in a specific world (Op permission).
- `pluginmanagerworld.disable`: Allows disabling plugins in a specific world (Op permission).
- `pluginmanagerworld.enable.world`: Allows enabling all plugins in a world (Op permission).
- `pluginmanagerworld.disable.world`: Allows disabling all plugins in a world (Op permission).
- `pluginmanagerworld.info`: Allows the player to view the enabled and disabled plugins in the world. (Default: true for all players)
- `pluginmanagerworld.help`: Allows using the help command (Op permission).
- `pluginmanagerworld.list`: Allows listing plugins in the world (Op permission).
- `pluginmanagerworld.reload`: Allows reloading the plugin configuration (Op permission).

---

## Configuration

Upon first launch, the plugin will generate a `config.yml` file. This configuration file holds information about which plugins are enabled or disabled in each world.

Here’s an example of the `config.yml` structure:

```yaml
worlds:
world:
 enabled-plugins:
   - plugin1
   - plugin2
 disabled-plugins:
   - plugin3
   - plugin4
world_nether:
 enabled-plugins:
   - plugin5
 disabled-plugins:
   - plugin6
**Credit By Abdelrahman Moharram**
