package org.cptgummiball.blastAndSmoke;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.BlastingRecipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Main extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        getLogger().info("Loading config and recipes...");
        saveResource("blasting.yml", false);
        saveResource("smoking.yml", false);
        loadConfig();  // Lädt die config.yml
        if (config.getBoolean("blasting.enabled")) {
            registerBlastingRecipes("blasting.yml");
        }
        if (config.getBoolean("smoking.enabled")) {
            registerSmokingRecipes("smoking.yml");
        }
    }

    private void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }


    // This method only loads the Blast Furnace recipes from blasting.yml
    private void registerBlastingRecipes(String recipeFile) {
        getLogger().info("Loading " + recipeFile + "...");
        File recipeConfigFile = new File(getDataFolder(), recipeFile);
        if (!recipeConfigFile.exists()) {
            getLogger().warning(recipeFile + " not found.");
            return;
        }

        FileConfiguration recipeConfig = YamlConfiguration.loadConfiguration(recipeConfigFile);
        Set<String> keys = recipeConfig.getKeys(false);

        for (String key : keys) {
            String path = key; // The complete path within the file
            Material input = Material.matchMaterial(recipeConfig.getString(path + ".input"));
            Material output = Material.matchMaterial(recipeConfig.getString(path + ".output"));
            int cookingTime = recipeConfig.getInt(path + ".cookingTime", 100);
            float experience = (float) recipeConfig.getDouble(path + ".experience", 0.3);
            int outputAmount = recipeConfig.getInt(path + ".output_amount", 1);

            // Get custom output name, if present
            String outputName = recipeConfig.getString(path + ".output_name", null);

            // Get CustomModelData values, if present
            int inputCustomModelData = recipeConfig.contains(path + ".inputCustomModelData") ? recipeConfig.getInt(path + ".inputCustomModelData") : -1;
            int outputCustomModelData = recipeConfig.contains(path + ".outputCustomModelData") ? recipeConfig.getInt(path + ".outputCustomModelData") : -1;

            // Get enchantments, if present
            List<String> enchantmentsList = recipeConfig.getStringList(path + ".enchantments");
            List<Enchantment> enchantments = new ArrayList<>();
            List<Integer> levels = new ArrayList<>();
            for (String enchantment : enchantmentsList) {
                String[] parts = enchantment.split(",");
                if (parts.length == 2) {
                    Enchantment enchantmentType = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toUpperCase()));
                    try {
                        int level = Integer.parseInt(parts[1]);
                        if (enchantmentType != null) {
                            enchantments.add(enchantmentType);
                            levels.add(level);
                        }
                    } catch (NumberFormatException e) {
                        getLogger().warning("Invalid enchantment level in " + path + ": " + enchantment);
                    }
                }
            }

            // Get Lore if present
            List<String> loreList = recipeConfig.getStringList(path + ".lore");
            List<String> wrappedLore = wrapText(loreList, 30); // Wrap Text to 30 characters

            if (input != null && output != null) {
                addBlastFurnaceRecipe(key, input, output, cookingTime, experience, outputAmount, inputCustomModelData, outputCustomModelData, outputName, enchantments, levels, wrappedLore);
            }
        }
    }

    // This method only loads the recipes for the smoker from smoking.yml
    private void registerSmokingRecipes(String recipeFile) {
        getLogger().info("Loading " + recipeFile + "...");
        File recipeConfigFile = new File(getDataFolder(), recipeFile);
        if (!recipeConfigFile.exists()) {
            getLogger().warning(recipeFile + " not found.");
            return;
        }

        FileConfiguration recipeConfig = YamlConfiguration.loadConfiguration(recipeConfigFile);
        Set<String> keys = recipeConfig.getKeys(false);

        for (String key : keys) {
            String path = key;  // The complete path within the file
            Material input = Material.matchMaterial(recipeConfig.getString(path + ".input"));
            Material output = Material.matchMaterial(recipeConfig.getString(path + ".output"));
            int cookingTime = recipeConfig.getInt(path + ".cookingTime", 100);
            float experience = (float) recipeConfig.getDouble(path + ".experience", 0.3);
            int outputAmount = recipeConfig.getInt(path + ".output_amount", 1);

            // Get custom output name, if present
            String outputName = recipeConfig.getString(path + ".output_name", null);

            // Get CustomModelData values, if present
            int inputCustomModelData = recipeConfig.contains(path + ".inputCustomModelData") ? recipeConfig.getInt(path + ".inputCustomModelData") : -1;
            int outputCustomModelData = recipeConfig.contains(path + ".outputCustomModelData") ? recipeConfig.getInt(path + ".outputCustomModelData") : -1;

            // Hole die Enchantments, falls vorhanden
            List<String> enchantmentsList = recipeConfig.getStringList(path + ".enchantments");
            List<Enchantment> enchantments = new ArrayList<>();
            List<Integer> levels = new ArrayList<>();
            for (String enchantment : enchantmentsList) {
                String[] parts = enchantment.split(",");
                if (parts.length == 2) {
                    Enchantment enchantmentType = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toUpperCase()));
                    try {
                        int level = Integer.parseInt(parts[1]);
                        if (enchantmentType != null) {
                            enchantments.add(enchantmentType);
                            levels.add(level);
                        }
                    } catch (NumberFormatException e) {
                        getLogger().warning("Invalid enchantment level in " + path + ": " + enchantment);
                    }
                }
            }

            // Get Lore if present
            List<String> loreList = recipeConfig.getStringList(path + ".lore");
            List<String> wrappedLore = wrapText(loreList, 30); // Wrap Text to 30 characters

            if (input != null && output != null) {
                addSmokingRecipe(key, input, output, cookingTime, experience, outputAmount, inputCustomModelData, outputCustomModelData, outputName, enchantments, levels, wrappedLore);
            }
        }
    }

    // This method adds recipes for the Blast Furnace
    private void addBlastFurnaceRecipe(String key, Material input, Material output, int cookingTime, float experience, int outputAmount, int inputCustomModelData, int outputCustomModelData, String outputName, List<Enchantment> enchantments, List<Integer> levels, List<String> lore) {
        NamespacedKey recipeKey = new NamespacedKey(this, key);

        // Create the input ItemStack with CustomModelData if provided
        ItemStack inputItemStack = new ItemStack(input);
        if (inputCustomModelData != -1) {
            inputItemStack = setCustomModelData(inputItemStack, inputCustomModelData);
        }

        // Create the output ItemStack with CustomModelData if provided
        ItemStack outputItemStack = new ItemStack(output, outputAmount);
        if (outputCustomModelData != -1) {
            outputItemStack = setCustomModelData(outputItemStack, outputCustomModelData);
        }

        // add custom output name if provided
        if (outputName != null && !outputName.isEmpty()) {
            ItemMeta meta = outputItemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(outputName);
                outputItemStack.setItemMeta(meta);
            }
        }

        // add enchantments if provided
        if (!enchantments.isEmpty()) {
            ItemMeta meta = outputItemStack.getItemMeta();
            if (meta != null) {
                for (int i = 0; i < enchantments.size(); i++) {
                    meta.addEnchant(enchantments.get(i), levels.get(i), true);
                }
                outputItemStack.setItemMeta(meta);
            }
        }

        // Put Lore if provided
        if (lore != null && !lore.isEmpty()) {
            ItemMeta meta = outputItemStack.getItemMeta();
            if (meta != null) {
                meta.setLore(lore);
                outputItemStack.setItemMeta(meta);
            }
        }

        // Create a MaterialChoice with the input Material (not ItemStack)
        RecipeChoice inputChoice = new RecipeChoice.MaterialChoice(input);

        // Add the recipe for the Blast Furnace
        BlastingRecipe recipe = new BlastingRecipe(recipeKey, outputItemStack, inputChoice, experience, cookingTime);
        getServer().addRecipe(recipe);
    }

    // This method adds recipes for the Smoker
    private void addSmokingRecipe(String key, Material input, Material output, int cookingTime, float experience, int outputAmount, int inputCustomModelData, int outputCustomModelData, String outputName, List<Enchantment> enchantments, List<Integer> levels, List<String> lore) {
        NamespacedKey recipeKey = new NamespacedKey(this, key);

        // Create the input ItemStack with CustomModelData if provided
        ItemStack inputItemStack = new ItemStack(input);
        if (inputCustomModelData != -1) {
            inputItemStack = setCustomModelData(inputItemStack, inputCustomModelData);
        }

        // Create the output ItemStack with CustomModelData if provided
        ItemStack outputItemStack = new ItemStack(output, outputAmount);
        if (outputCustomModelData != -1) {
            outputItemStack = setCustomModelData(outputItemStack, outputCustomModelData);
        }

        // add custom output name if provided
        if (outputName != null && !outputName.isEmpty()) {
            ItemMeta meta = outputItemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(outputName);
                outputItemStack.setItemMeta(meta);
            }
        }

        // add enchantments if provided
        if (!enchantments.isEmpty()) {
            ItemMeta meta = outputItemStack.getItemMeta();
            if (meta != null) {
                for (int i = 0; i < enchantments.size(); i++) {
                    meta.addEnchant(enchantments.get(i), levels.get(i), true);
                }
                outputItemStack.setItemMeta(meta);
            }
        }

        // Put Lore if provided
        if (lore != null && !lore.isEmpty()) {
            ItemMeta meta = outputItemStack.getItemMeta();
            if (meta != null) {
                meta.setLore(lore);
                outputItemStack.setItemMeta(meta);
            }
        }

        // Create a MaterialChoice with the input Material (not ItemStack)
        RecipeChoice inputChoice = new RecipeChoice.MaterialChoice(input);

        // Add the recipe for the Smoker
        SmokingRecipe recipe = new SmokingRecipe(recipeKey, outputItemStack, inputChoice, experience, cookingTime);
        getServer().addRecipe(recipe);
    }

    // Helper method to set CustomModelData
    private ItemStack setCustomModelData(ItemStack itemStack, int customModelData) {
        itemStack = itemStack.clone();  // Ensure the original stack is not modified
        itemStack.getItemMeta().setCustomModelData(customModelData);
        return itemStack;
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bas")) {
            if (!sender.hasPermission("bas.admin")) {
                sender.sendMessage("You do not have permission to run this command.");
                return false;
            }

            if (args.length == 0) {
                sender.sendMessage("Usage: /bas reload <blasting|smoking>");
                return false;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (args.length == 2) {
                    String feature = args[1].toLowerCase();
                    if (feature.equals("blasting") || feature.equals("smoking")) {
                        reloadConfigForFeature(feature);
                        sender.sendMessage("Config for " + feature + " reloaded");
                    } else {
                        sender.sendMessage("Invalid feature. Please use 'blasting' or 'smoking'.");
                    }
                } else {
                    sender.sendMessage("Usage: /bas reload <blasting|smoking>");
                }
                return true;
            } else {
                sender.sendMessage("Usage /bas reload <blasting|smoking>");
                return false;
            }
        }
        return false;
    }

    private void reloadConfigForFeature(String feature) {
        loadConfig();
        if (feature.equals("blasting") && config.getBoolean("blasting.enabled")) {
            registerBlastingRecipes("blasting.yml");
        }
        if (feature.equals("smoking") && config.getBoolean("smoking.enabled")) {
            registerSmokingRecipes("smoking.yml");
        }
    }

    // Helper method to wrap texts
    private List<String> wrapText(List<String> input, int maxLineLength) {
        List<String> wrappedText = new ArrayList<>();
        for (String line : input) {
            if (line.length() <= maxLineLength) {
                wrappedText.add(line);
            } else {
                StringBuilder sb = new StringBuilder();
                String[] words = line.split(" ");
                int lineLength = 0;
                for (String word : words) {
                    if (lineLength + word.length() > maxLineLength) {
                        wrappedText.add(sb.toString());
                        sb.setLength(0);
                        lineLength = 0;
                    }
                    if (sb.length() > 0) sb.append(" ");
                    sb.append(word);
                    lineLength += word.length() + 1;
                }
                if (sb.length() > 0) wrappedText.add(sb.toString());
            }
        }
        return wrappedText;
    }

}
