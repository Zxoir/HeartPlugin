package me.zxoir.shadowgod8s;

import lombok.Getter;
import me.zxoir.shadowgod8s.commands.HeartCommand;
import me.zxoir.shadowgod8s.commands.VoidTpCommand;
import me.zxoir.shadowgod8s.data.DataFile;
import me.zxoir.shadowgod8s.hearts.*;
import me.zxoir.shadowgod8s.listeners.*;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import me.zxoir.shadowgod8s.utils.GlowingEntities;
import me.zxoir.shadowgod8s.utils.HeartsUtils;
import me.zxoir.shadowgod8s.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Shadowgod8s extends JavaPlugin {
    @Getter
    private static GlowingEntities glowingEntities;
    private static ItemStack heartSword;
    private static DataFile dataFile;

    @Override
    public void onEnable() {
        Logger.info("======================================================================\nStarting plugin load process...");
        long initializeTime = System.currentTimeMillis();

        glowingEntities = new GlowingEntities(this);
        ConfigManager.setup();
        initializeHeartUtil();
        dataFile = new DataFile();
        dataFile.setup();

        registerCommands();
        registerListeners();

        loadVoidHeartRecipe();
        if (ConfigManager.isHeartSwordEnabled()) {
            heartSword = createHeartSword();
            loadHeartSwordRecipe();
        }

        Logger.info("Plugin has been loaded successfully (toke {}ms)", (System.currentTimeMillis() - initializeTime));
        Logger.info("======================================================================");
    }

    private void initializeHeartUtil() {
        HeartsUtils.getHeartItemMap().put(FlameHeart.class, ConfigManager.getFlameHeartItem());
        HeartsUtils.getHeartItemMap().put(LockedHeart.class, ConfigManager.getLockedHeartItem());
        HeartsUtils.getHeartItemMap().put(StrengthHeart.class, ConfigManager.getStrengthHeartItem());
        HeartsUtils.getHeartItemMap().put(VoidHeart.class, ConfigManager.getVoidHeartItem());
        HeartsUtils.getHeartItemMap().put(WaterHeart.class, ConfigManager.getWaterHeartItem());
        HeartsUtils.getHeartItemMap().put(XrayHeart.class, ConfigManager.getXrayHeartItem());
    }

    @Override
    public void onDisable() {
        glowingEntities.disable();
        glowingEntities = null;

        if (HeartManager.getActiveHearts().isEmpty())
            return;

        for (UUID uuid : HeartManager.getActiveHearts().keySet()) {
            Heart heart = HeartManager.getActiveHearts().get(uuid);

            if (heart == null)
                continue;

            dataFile.getConfig().set("activeHearts." + uuid.toString() + ".name", heart.getName());
            dataFile.saveConfig();
        }
    }

    private void registerCommands() {
        getCommand("heart").setExecutor(new HeartCommand());
        getCommand("heart").setTabCompleter(new HeartCommand());
        getCommand("voidtp").setExecutor(new VoidTpCommand());
        getCommand("voidtp").setTabCompleter(new VoidTpCommand());
    }

    private void registerListeners() {
        if (ConfigManager.isFlameHeartEnabled())
            getServer().getPluginManager().registerEvents(new FlameHeartListener(), this);
        if (ConfigManager.isLockedHeartEnabled())
            getServer().getPluginManager().registerEvents(new LockedHeartListener(), this);
        if (ConfigManager.isStrengthHeartEnabled())
            getServer().getPluginManager().registerEvents(new StrengthHeartListener(), this);
        if (ConfigManager.isWaterHeartEnabled())
            getServer().getPluginManager().registerEvents(new WaterHeartListener(), this);
        if (ConfigManager.isXrayHeartEnabled())
            getServer().getPluginManager().registerEvents(new XrayHeartListener(), this);
        if (ConfigManager.isHeartSwordEnabled())
            getServer().getPluginManager().registerEvents(new HeartSwordListener(), this);
        getServer().getPluginManager().registerEvents(new HeartEventListener(), this);
    }

    public ItemStack createHeartSword() {
        ItemStack itemStack = new ItemStackBuilder(Material.valueOf(ConfigManager.getHeartSwordMaterial()))
                .withDisplayName(ConfigManager.getHeartSwordName())
                .withLore(ConfigManager.getHeartSwordLore())
                .isUnbreakable(ConfigManager.isHeartSwordUnbreakable()).build();

        for (String enchant : ConfigManager.getHeartSwordEnchants()) {
            if (enchant.isBlank())
                continue;

            int level = 0;
            if (enchant.contains("@")) {
                String[] enchantSplit = enchant.split("@");
                enchant = enchantSplit[0];
                level = Integer.parseInt(enchantSplit[1]);
            }

            Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(enchant));
            if (enchantment == null)
                continue;

            itemStack.addUnsafeEnchantment(enchantment, level);
        }

        return itemStack;
    }

    public static ItemStack getHeartSword() {
        return heartSword.clone();
    }

    public void loadHeartSwordRecipe() {
        NamespacedKey namespacedKey = new NamespacedKey(this, "heartswordcrafting");
        ShapelessRecipe recipe = new ShapelessRecipe(namespacedKey, getHeartSword());
        HeartsUtils.getHeartItemMap().values().forEach(heartItem -> recipe.addIngredient(new RecipeChoice.ExactChoice(heartItem)));
        getServer().addRecipe(recipe);
    }

    public void loadVoidHeartRecipe() {
        // Get the recipe parts from the config
        List<String> top = ConfigManager.getVoidTopRecipe();
        List<String> middle = ConfigManager.getVoidMiddleRecipe();
        List<String> bottom = ConfigManager.getVoidBottomRecipe();

        // Combine all the parts into a single list
        List<String> recipeRows = List.of(
                top.get(0), top.get(1), top.get(2),
                middle.get(0), middle.get(1), middle.get(2),
                bottom.get(0), bottom.get(1), bottom.get(2)
        );

        // Map to store the material-to-character mapping
        Map<Material, Character> materialToCharMap = new HashMap<>();
        char currentChar = 'A';

        // StringBuilder to construct the shape
        StringBuilder shapeBuilder = new StringBuilder();

        // Iterate over each row and map materials to characters
        for (String materialName : recipeRows) {
            if (materialName.isEmpty()) {
                shapeBuilder.append(" ");
            } else {
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    if (!materialToCharMap.containsKey(material)) {
                        materialToCharMap.put(material, currentChar);
                        currentChar++;
                    }
                    shapeBuilder.append(materialToCharMap.get(material));
                } else {
                    shapeBuilder.append(" ");
                }
            }
        }

        // Split the shape into rows
        String shapeRow1 = shapeBuilder.substring(0, 3);
        String shapeRow2 = shapeBuilder.substring(3, 6);
        String shapeRow3 = shapeBuilder.substring(6, 9);

        // Create the ShapedRecipe
        ItemStack result = ConfigManager.getVoidHeartItem();
        NamespacedKey namespacedKey = new NamespacedKey(this, "voidheartcrafting");
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
        recipe.shape(shapeRow1, shapeRow2, shapeRow3);

        // Add the ingredients to the recipe
        for (Map.Entry<Material, Character> entry : materialToCharMap.entrySet()) {
            recipe.setIngredient(entry.getValue(), entry.getKey());
        }

        // Register the recipe
        getServer().addRecipe(recipe);
    }
}
