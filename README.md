# BlastAndSmoke
![Spigot](https://img.shields.io/badge/Spigot-1.21.x-yellow.svg)
![MIT License](https://img.shields.io/badge/PaperMC-1.21.x-blue.svg)
![Version](https://img.shields.io/badge/Version-1.0-gray.svg)
![MIT License](https://img.shields.io/badge/License-MIT-green.svg)
## Overview
BlastAndSmoke is a Minecraft plugin designed for Spigot 1.21+ that introduces custom furnace recipes for both **Blasting** and **Smoking** furnaces. This plugin allows you to add and manage recipes for these special furnaces independently via configuration files, providing more flexibility for your Minecraft server. **Support for both `standard materials` and custom items using `CustomModelData`.**

## Features
- **Blasting Recipes**: Allows you to register custom recipes for the Blast Furnace.
- **Smoking Recipes**: Allows you to register custom recipes for the Smoker.
- **CustomModelData Support**: Optionally, you can specify `CustomModelData` for both input and output items, allowing for full customization of your recipes.
- **Reload Command**: A command to reload specific recipes without needing a server restart.
- **Permission System**: Only players with the `bas.admin` permission can reload the configuration.

## Requirements
- `Spigot 1.21+` or compatible version.
- `Java 21` or higher
## Installation

1. Download the latest release of the plugin from the [releases page](https://github.com/CptGummiball/BlastAndSmoke/releases) (if available).
2. Place the `BlastAndSmoke.jar` file into the `plugins` folder of your Spigot server.
3. Restart or reload the server.

Once the plugin is installed, it will create configuration files (`blasting.yml`, `smoking.yml`, and `config.yml`) in the `plugins/BlastAndSmoke` folder.

## Configuration Files

- **config.yml**: Main configuration file. Contains global settings for enabling or disabling the `blasting` and `smoking` features.
    - `blasting.enabled`: Whether or not the blasting feature is enabled.
    - `smoking.enabled`: Whether or not the smoking feature is enabled.

- **blasting.yml**: Custom recipes for the Blast Furnace. You can add different recipes here by specifying the input material, output material, custommodeldata, cooking time, and experience.

- **smoking.yml**: Custom recipes for the Smoker. Similar to `blasting.yml`, you can define recipes for the Smoker furnace.

Example structure for recipe definitions in `blasting.yml`:

```yaml
  recipe1:
    input: "IRON_ORE"
    output: "IRON_INGOT"
    cookingTime: 200
    experience: 0.7
    inputCustomModelData: 123  # Optional CustomModelData for input
    outputCustomModelData: 456  # Optional CustomModelData for output
````

## Commands
- `/bas reload blasting`: Reloads the blasting.yml recipe configuration.
- `/bas reload smoking`: Reloads the smoking.yml recipe configuration.

## Permissions
- `bas.admin`: Allows players to use the reload commands (/bas reload blasting or /bas reload smoking). By default, this permission is granted to server operators (admins).

## Troubleshooting
- If the plugin is not loading properly, check the server logs for errors. Ensure that all necessary files (like blasting.yml and smoking.yml) are present in the plugin's data folder.
- If you encounter issues with CustomModelData, ensure that the item in your resource pack supports this feature.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributions
Feel free to contribute to this plugin by opening issues or submitting pull requests. Please ensure that your changes are well-documented and follow the existing coding style.