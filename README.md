# Heart Plugin

**Heart Plugin** is a custom Minecraft plugin designed for Spigot/Paper servers. Originally made for **ShadowGod8**, but he has given me permission to share the code on GithHub! It introduces unique heart items that provide special abilities to players. The plugin is highly configurable, allowing server administrators to customize recipes, abilities, and much more.

**Thank you [SkytAsul](https://github.com/SkytAsul) for creating a wonderful [GlowingEntities](https://github.com/SkytAsul/GlowingEntities) library, this would have been much harder without it :)**

## Features

- **Custom Heart Items**: Various hearts with unique abilities, such as Flame Heart, Locked Heart, Strength Heart, Void Heart, Xray Heart, and Water Heart.
- **Dynamic Crafting Recipes**: Recipes are configurable, allowing for up to 9 different materials, each assigned dynamically based on configuration.
- **Cooldowns and Abilities**: Abilities can have configurable cooldowns, with custom messages displayed to players.
- **Permissions and Commands**: Manage hearts through easy-to-use commands with permission-based access.
- **Efficient and Modular Code**: The plugin is designed with efficiency in mind, ensuring minimal impact on server performance.

## Commands

### `/heart setheart <Player> <Heart>`
- **Description**: Assigns a specific heart to a player.
- **Permission**: `shadowgod8s.admin`
- **Usage Example**: `/heart setheart Steve FlameHeart`

### `/heart giveHeartItem <Player> <Heart>`
- **Description**: Gives a player the item representation of a specific heart.
- **Permission**: `shadowgod8s.admin`
- **Usage Example**: `/heart giveHeartItem Alex VoidHeart`

### `/heart reload`
- **Description**: Reloads the plugin configuration.
- **Permission**: `shadowgod8s.admin`
- **Usage Example**: `/heart reload`

### `/voidtp <Player>`
- **Description**: Teleports up to 3 players to the void for 30 seconds if the player executing the command has the Void Heart.
- **Usage Example**: `/voidtp Notch`

## Permissions

- **`shadowgod8s.admin`**: Grants access to all administrative commands in the Heart Plugin.

## Configuration

The plugin's configuration file allows you to customize the heart items, crafting recipes, cooldowns, and more. Below is an example configuration for a heart sword:

```yaml
heart_sword:
  name: "&0&bHeart Sword" 
  lore: 
    - "&7If you &8kill &7a player with this sword"
    - "&7they will be &8banned &7off the server!"
  enchants: 
    - "minecraft:Sharpness@5"
    - "minecraft:Knockback@2"
```

## Example Crafting Recipe Configuration
```yaml
recipe:
  top:
    - ""
    - ""
    - ""
  middle:
    - "NETHER_STAR"
    - "DRAGON_EGG"
    - "NETHER_STAR"
  bottom:
    - ""
    - ""
    - ""
```

## Installation
1. Download the latest version of the plugin jar file from the Releases section.
2. Place the jar file into your server's plugins folder.
3. Start or restart your server to generate the default configuration files.
4. Customize the configuration files in the plugins/HeartPlugin directory to suit your server's needs.
5. Reload or restart the server for the changes to take effect.

## Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch (feature/my-feature).
3. Commit your changes.
4. Push the branch (git push origin feature/my-feature).
5. Open a pull request.

## Support
For support, please open an issue on the GitHub repository.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.