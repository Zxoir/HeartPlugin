package me.zxoir.shadowgod8s.managers;

import lombok.Getter;
import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.hearts.FlameHeart;
import me.zxoir.shadowgod8s.hearts.LockedHeart;
import me.zxoir.shadowgod8s.hearts.StrengthHeart;
import me.zxoir.shadowgod8s.hearts.VoidHeart;
import me.zxoir.shadowgod8s.utils.ItemStackBuilder;
import me.zxoir.shadowgod8s.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.zxoir.shadowgod8s.utils.HeartsUtils.colorize;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class ConfigManager {
    private static final Shadowgod8s main = Shadowgod8s.getPlugin(Shadowgod8s.class);
    private static boolean setup = false;

    @Getter
    private static boolean heartSwordEnabled;
    @Getter
    private static boolean heartSwordUnbreakable;
    @Getter
    private static boolean heartSwordDroppable;
    @Getter
    private static boolean heartSwordDespawnable;
    @Getter
    private static String heartSwordMaterial;
    @Getter
    private static String heartSwordName;
    @Getter
    private static List<String> heartSwordLore;
    @Getter
    private static List<String> heartSwordEnchants;
    @Getter
    private static List<String> heartSwordKillCommands;

    @Getter
    private static boolean flameHeartEnabled;
    @Getter
    private static int lavaSurvivalTime;
    @Getter
    private static boolean flameHeartFireballs;
    @Getter
    private static String flameHeartText;
    @Getter
    private static String flameHeartActiveText;
    private static String flameHeartCooldownText;
    @Getter
    private static int flameHeartCooldown;

    @Getter
    private static boolean lockedHeartEnabled;
    @Getter
    private static int resistanceLevel;
    @Getter
    private static int resistanceDuration;
    @Getter
    private static String requiredAchievement;
    @Getter
    private static String lockedHeartActiveText;
    private static String lockedHeartCooldownText;
    @Getter
    private static String lockedHeartText;
    @Getter
    private static int lockedHeartCooldown;

    @Getter
    private static boolean xrayHeartEnabled;
    @Getter
    private static String xrayGlowColor;
    @Getter
    private static int xrayHeartCooldown;
    @Getter
    private static int xrayVisibilityRange;
    @Getter
    private static String xrayHeartText;

    @Getter
    private static boolean strengthHeartEnabled;
    @Getter
    private static int strengthHeartCooldown;
    @Getter
    private static int strengthLevel;
    @Getter
    private static int abilityStrengthLevel;
    @Getter
    private static int abilityStrengthDuration;
    @Getter
    private static int splashPotsRequired;
    @Getter
    private static int splashPotsDuration;
    @Getter
    private static String strengthHeartText;
    @Getter
    private static String strengthHeartActiveText;
    private static String strengthHeartCooldownText;

    @Getter
    private static boolean waterHeartEnabled;
    @Getter
    private static int swimSpeedMultiplier;
    @Getter
    private static boolean infiniteWaterBreathing;
    @Getter
    private static int drownTries;
    @Getter
    private static int drownTimeLimit;
    @Getter
    private static String waterHeartText;

    @Getter
    private static boolean voidHeartEnabled;
    @Getter
    private static int voidTeleportLimit;
    @Getter
    private static int voidTeleportDuration;
    @Getter
    private static String voidHeartText;
    @Getter
    private static String voidHeartActiveText;
    private static String voidHeartActivedText;
    private static String voidHeartCooldownText;
    @Getter
    private static int voidHeartCooldown;

    @Getter
    private static String noHeartMessage;
    @Getter
    private static String heartObtainedMessage;
    @Getter
    private static String heartLostMessage;
    @Getter
    private static String noPermissionMessage;
    private static String xrayHeartCooldownMessage;
    @Getter
    private static String voidTpNoVoidHeartMessage;
    @Getter
    private static String voidTpIncorrectUsageMessage;
    @Getter
    private static String voidTpPlayerNotFoundMessage;
    @Getter
    private static String voidTpKillSelfMessage;
    @Getter
    private static String voidTpNoChargesMessage;
    private static String voidTpKillKillerMessage;
    private static String voidTpKillVictimMessage;
    private static String voidTpKillAnnounce;
    private static String absorbHeartCooldownMessage;
    private static String absorbHeartMessage;
    @Getter
    private static String absorbSameHeartErrorMessage;

    @Getter
    private static Material flameHeartMaterial;
    @Getter
    private static String flameHeartName;
    @Getter
    private static List<String> flameHeartLore;
    @Getter
    private static boolean flameHeartEnchanted;

    @Getter
    private static Material lockedHeartMaterial;
    @Getter
    private static String lockedHeartName;
    @Getter
    private static List<String> lockedHeartLore;
    @Getter
    private static boolean lockedHeartEnchanted;

    @Getter
    private static Material xrayHeartMaterial;
    @Getter
    private static String xrayHeartName;
    @Getter
    private static List<String> xrayHeartLore;
    @Getter
    private static boolean xrayHeartEnchanted;

    @Getter
    private static Material strengthHeartMaterial;
    @Getter
    private static String strengthHeartName;
    @Getter
    private static List<String> strengthHeartLore;
    @Getter
    private static boolean strengthHeartEnchanted;

    @Getter
    private static Material waterHeartMaterial;
    @Getter
    private static String waterHeartName;
    @Getter
    private static List<String> waterHeartLore;
    @Getter
    private static boolean waterHeartEnchanted;

    @Getter
    private static Material voidHeartMaterial;
    @Getter
    private static String voidHeartName;
    @Getter
    private static List<String> voidHeartLore;
    @Getter
    private static boolean voidHeartEnchanted;
    @Getter
    private static List<String> voidTopRecipe;
    @Getter
    private static List<String> voidMiddleRecipe;
    @Getter
    private static List<String> voidBottomRecipe;

    @Getter
    private static boolean dropHeart;
    @Getter
    private static boolean droppableHeart;
    @Getter
    private static boolean despawnableHeart;
    @Getter
    private static boolean stealHeart;
    @Getter
    private static int absorbHeartCooldown;
    @Getter
    private static boolean isGiveOldHeartItem;

    private static void getConfigData() {
        FileConfiguration config = main.getConfig();

        // Heart Sword
        heartSwordEnabled = config.getBoolean("hearts.heart-Sword.enabled");
        heartSwordUnbreakable = config.getBoolean("hearts.heart-Sword.unbreakable");
        heartSwordDroppable = config.getBoolean("hearts.heart-Sword.droppable");
        heartSwordDespawnable = config.getBoolean("hearts.heart-Sword.despawnable");
        heartSwordMaterial = config.getString("hearts.heart-Sword.material");
        heartSwordName = colorize(config.getString("hearts.heart-Sword.name"));
        heartSwordLore = config.getStringList("hearts.heart-Sword.lore");
        heartSwordEnchants = config.getStringList("hearts.heart-Sword.enchants");
        heartSwordKillCommands = config.getStringList("hearts.heart-Sword.kill-commands");

        // Flame Heart
        flameHeartEnabled = config.getBoolean("hearts.flame-heart.enabled");
        lavaSurvivalTime = config.getInt("hearts.flame-heart.lava-survival-time");
        flameHeartFireballs = config.getBoolean("hearts.flame-heart.effects.fireballs");
        flameHeartText = colorize(config.getString("hearts.flame-heart.action-bar.text", "&cFlame Heart"));
        flameHeartActiveText = colorize(config.getString("hearts.flame-heart.action-bar.active-text", "(Ready to be activated)"));
        flameHeartCooldownText = colorize(config.getString("hearts.flame-heart.action-bar.cooldown-text", "(You may use this again in %cooldown%s)"));
        flameHeartCooldown = config.getInt("hearts.flame-heart.ability-cooldown", 600);

        // Locked Heart
        lockedHeartEnabled = config.getBoolean("hearts.locked-heart.enabled");
        resistanceLevel = config.getInt("hearts.locked-heart.effects.resistance-level");
        resistanceDuration = config.getInt("hearts.locked-heart.effects.resistance-duration");
        requiredAchievement = config.getString("hearts.locked-heart.required-achievement");
        lockedHeartText = colorize(config.getString("hearts.locked-heart.action-bar.text", "&6Locked Heart"));
        lockedHeartActiveText = colorize(config.getString("hearts.locked-heart.action-bar.active-text", "(Active)"));
        lockedHeartCooldownText = colorize(config.getString("hearts.locked-heart.action-bar.cooldown-text", "(Cooldown: %cooldown%s)"));
        lockedHeartCooldown = config.getInt("hearts.locked-heart.ability-cooldown", 600);

        // Xray Heart
        xrayHeartEnabled = config.getBoolean("hearts.xray-heart.enabled");
        xrayGlowColor = config.getString("hearts.xray-heart.glow-color");
        xrayHeartCooldown = config.getInt("hearts.xray-heart.obtain-cooldown");
        xrayVisibilityRange = config.getInt("hearts.xray-heart.effects.xray-visibility");
        xrayHeartText = colorize(config.getString("hearts.xray-heart.action-bar.text", "&eXray Heart"));

        // Strength Heart
        strengthHeartEnabled = config.getBoolean("hearts.strength-heart.enabled");
        strengthHeartCooldown = config.getInt("hearts.strength-heart.ability-cooldown");
        strengthLevel = config.getInt("hearts.strength-heart.effects.strength-level");
        abilityStrengthLevel = config.getInt("hearts.strength-heart.effects.ability-strength-level");
        abilityStrengthDuration = config.getInt("hearts.strength-heart.effects.ability-strength-duration");
        splashPotsRequired = config.getInt("hearts.strength-heart.splash-pots-required");
        splashPotsDuration = config.getInt("hearts.strength-heart.splash-pots-duration");
        strengthHeartText = colorize(config.getString("hearts.strength-heart.action-bar.text", "&4Strength Heart"));
        strengthHeartActiveText = colorize(config.getString("hearts.strength-heart.action-bar.active-text", "(Ready to be activated)"));
        strengthHeartCooldownText = colorize(config.getString("hearts.strength-heart.action-bar.cooldown-text", "(You may use this again in %cooldown%)"));

        // Water Heart
        waterHeartEnabled = config.getBoolean("hearts.water-heart.enabled");
        swimSpeedMultiplier = config.getInt("hearts.water-heart.effects.swim-speed");
        infiniteWaterBreathing = config.getBoolean("hearts.water-heart.effects.water-breathing");
        drownTries = config.getInt("hearts.water-heart.drown-tries");
        drownTimeLimit = config.getInt("hearts.water-heart.drown-time-limit");
        waterHeartText = colorize(config.getString("hearts.water-heart.action-bar.text", "&bWater Heart"));

        // Void Heart
        voidHeartEnabled = config.getBoolean("hearts.void-heart.enabled");
        voidTeleportLimit = config.getInt("hearts.void-heart.effects.void-teleport-limit");
        voidTeleportDuration = config.getInt("hearts.void-heart.effects.void-teleport-duration");
        voidHeartText = colorize(config.getString("hearts.void-heart.action-bar.text", "&8Void Heart"));
        voidHeartActiveText = colorize(config.getString("hearts.void-heart.action-bar.active-text", "(Ready to be activated)"));
        voidHeartActivedText = colorize(config.getString("hearts.void-heart.action-bar.actived-text", "(Ability ending in %remaining_time%)"));
        voidHeartCooldownText = colorize(config.getString("hearts.void-heart.action-bar.cooldown-text", "(You may use this again in %cooldown%s)"));
        voidHeartCooldown = config.getInt("hearts.void-heart.ability-cooldown", 600);

        // Heart Items
        dropHeart = config.getBoolean("hearts.drop-heart");
        droppableHeart = config.getBoolean("hearts.droppable-heart");
        despawnableHeart = config.getBoolean("hearts.despawnable-heart");
        stealHeart = config.getBoolean("hearts.steal-heart");
        absorbHeartCooldown = config.getInt("hearts.absorb-heart-cooldown");
        isGiveOldHeartItem = config.getBoolean("hearts.give-old-heart-item");
        flameHeartMaterial = Material.valueOf(config.getString("hearts.flame-heart.item.material", "NETHER_STAR"));
        flameHeartName = colorize(config.getString("hearts.flame-heart.item.name", "&cFlame Heart"));
        flameHeartLore = config.getStringList("hearts.flame-heart.item.lore");
        flameHeartEnchanted = config.getBoolean("hearts.flame-heart.item.enchanted", true);

        lockedHeartMaterial = Material.valueOf(colorize(config.getString("hearts.locked-heart.item.material", "NETHER_STAR")));
        lockedHeartName = colorize(config.getString("hearts.locked-heart.item.name", "&6Locked Heart"));
        lockedHeartLore = config.getStringList("hearts.locked-heart.item.lore");
        lockedHeartEnchanted = config.getBoolean("hearts.locked-heart.item.enchanted", true);

        xrayHeartMaterial = Material.valueOf(colorize(config.getString("hearts.xray-heart.item.material", "NETHER_STAR")));
        xrayHeartName = colorize(config.getString("hearts.xray-heart.item.name", "&bXray Heart"));
        xrayHeartLore = config.getStringList("hearts.xray-heart.item.lore");
        xrayHeartEnchanted = config.getBoolean("hearts.xray-heart.item.enchanted", true);

        strengthHeartMaterial = Material.valueOf(colorize(config.getString("hearts.strength-heart.item.material", "NETHER_STAR")));
        strengthHeartName = colorize(config.getString("hearts.strength-heart.item.name", "&4Strength Heart"));
        strengthHeartLore = config.getStringList("hearts.strength-heart.item.lore");
        strengthHeartEnchanted = config.getBoolean("hearts.strength-heart.item.enchanted", true);

        waterHeartMaterial = Material.valueOf(colorize(config.getString("hearts.water-heart.item.material", "NETHER_STAR")));
        waterHeartName = colorize(config.getString("hearts.water-heart.item.name", "&3Water Heart"));
        waterHeartLore = config.getStringList("hearts.water-heart.item.lore");
        waterHeartEnchanted = config.getBoolean("hearts.water-heart.item.enchanted", true);

        voidHeartMaterial = Material.valueOf(colorize(config.getString("hearts.void-heart.item.material", "NETHER_STAR")));
        voidHeartName = colorize(config.getString("hearts.void-heart.item.name", "&8Void Heart"));
        voidHeartLore = config.getStringList("hearts.void-heart.item.lore");
        voidHeartEnchanted = config.getBoolean("hearts.void-heart.item.enchanted", true);
        voidTopRecipe = config.getStringList("hearts.void-heart.recipe.top");
        voidMiddleRecipe = config.getStringList("hearts.void-heart.recipe.middle");
        voidBottomRecipe = config.getStringList("hearts.void-heart.recipe.bottom");

        // Messages
        noHeartMessage = colorize(config.getString("messages.no-heart", "&cYou have no heart equipped."));
        noPermissionMessage = colorize(config.getString("messages.no-permission"));
        heartObtainedMessage = colorize(config.getString("messages.heart-obtained", "&aYou have obtained the &6%heart% &aheart!"));
        heartLostMessage = colorize(config.getString("messages.heart-lost", "&cYou have lost your heart."));
        xrayHeartCooldownMessage = colorize(config.getString("messages.xray-heart-cooldown"));
        voidTpNoVoidHeartMessage = colorize(config.getString("messages.void-tp-no-void-heart"));
        voidTpIncorrectUsageMessage = colorize(config.getString("messages.void-tp-incorrect-usage"));
        voidTpPlayerNotFoundMessage = colorize(config.getString("messages.void-tp-player-not-found"));
        voidTpKillSelfMessage = colorize(config.getString("messages.void-tp-kill-self"));
        voidTpNoChargesMessage = colorize(config.getString("messages.void-tp-no-charges"));
        voidTpKillKillerMessage = colorize(config.getString("messages.void-tp-kill-killer-message"));
        voidTpKillVictimMessage = colorize(config.getString("messages.void-tp-kill-victim-message"));
        voidTpKillAnnounce = colorize(config.getString("messages.void-tp-kill-announce"));
        absorbHeartCooldownMessage = colorize(config.getString("messages.absorb-heart-cooldown"));
        absorbHeartMessage = colorize(config.getString("messages.absorb-heart"));
        absorbSameHeartErrorMessage = colorize(config.getString("messages.absorb-same-heart-error"));
    }

    public static void setup() {
        if (setup)
            return;

        main.saveDefaultConfig();
        getConfigData();
        setup = true;
    }

    public static void reloadConfig() {
        main.reloadConfig();
        getConfigData();
    }

    public static String getAbsorbHeartMessage(String heartName) {
        return absorbHeartMessage.replace("%heart_name%", heartName);
    }

    public static String getAbsorbHeartCooldownMessage(String cooldown) {
        return absorbHeartCooldownMessage.replace("%cooldown%", cooldown);
    }

    public static String getVoidTpKillKillerMessage(String playerName) {
        return voidTpKillKillerMessage.replace("%player_name%", playerName);
    }

    public static String getVoidTpKillVictimMessage(String playerName) {
        return voidTpKillVictimMessage.replace("%player_name%", playerName);
    }

    public static String getVoidTpKillAnnounce(String killerName, String playerName) {
        return voidTpKillAnnounce.replace("%killer_name%", killerName).replace("%player_name%", playerName);
    }

    public static String getXrayHeartCooldownMessage(String cooldown) {
        return xrayHeartCooldownMessage.replace("%cooldown%", cooldown);
    }

    public static String getFlameHeartCooldownText(FlameHeart heart) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getFlameHeartCooldown() * 1000L;
        long timeLeft = (cooldownTime - (currentTime - heart.getLastUsedAbility()));
        return flameHeartCooldownText.replace("%cooldown%", TimeUtil.formatTime(timeLeft, false, true));
    }

    public static String getLockedHeartCooldownText(LockedHeart heart) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getLockedHeartCooldown() * 1000L;
        long timeLeft = (cooldownTime - (currentTime - heart.getLastAbilityUsed()));
        return lockedHeartCooldownText.replace("%cooldown%", TimeUtil.formatTime(timeLeft, false, true));
    }

    public static String getVoidHeartActivedText(VoidHeart heart) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getVoidTeleportDuration() * 1000L;
        long timeLeft = (cooldownTime - (currentTime - heart.getLastUsedAbility()));
        return voidHeartActivedText.replace("%remaining_time%", TimeUtil.formatTime(timeLeft, false, true));
    }

    public static String getVoidHeartCooldownText(VoidHeart heart) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getVoidHeartCooldown() * 1000L;
        long timeLeft = (cooldownTime - (currentTime - heart.getLastUsedAbility()));
        return voidHeartCooldownText.replace("%cooldown%", TimeUtil.formatTime(timeLeft, false, true));
    }

    public static String getStrengthHeartCooldownText(StrengthHeart heart) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getVoidHeartCooldown() * 1000L;
        long timeLeft = (cooldownTime - (currentTime - heart.getLastUsedAbility()));
        return strengthHeartCooldownText.replace("%cooldown%", TimeUtil.formatTime(timeLeft, false, true));
    }

    public static ItemStack getFlameHeartItem() {
        return new ItemStackBuilder(flameHeartMaterial)
                .withDisplayName(flameHeartName)
                .withLore(flameHeartLore)
                .withEnchantment(flameHeartEnchanted ? Enchantment.ARROW_INFINITE : null)
                .hideFlags()
                .build();
    }

    public static ItemStack getLockedHeartItem() {
        return new ItemStackBuilder(lockedHeartMaterial)
                .withDisplayName(lockedHeartName)
                .withLore(lockedHeartLore)
                .withEnchantment(lockedHeartEnchanted ? Enchantment.ARROW_INFINITE : null)
                .hideFlags()
                .build();
    }

    public static ItemStack getXrayHeartItem() {
        return new ItemStackBuilder(xrayHeartMaterial)
                .withDisplayName(xrayHeartName)
                .withLore(xrayHeartLore)
                .withEnchantment(xrayHeartEnchanted ? Enchantment.ARROW_INFINITE : null)
                .hideFlags()
                .build();
    }

    public static ItemStack getStrengthHeartItem() {
        return new ItemStackBuilder(strengthHeartMaterial)
                .withDisplayName(strengthHeartName)
                .withLore(strengthHeartLore)
                .withEnchantment(strengthHeartEnchanted ? Enchantment.ARROW_INFINITE : null)
                .hideFlags()
                .build();
    }

    public static ItemStack getWaterHeartItem() {
        return new ItemStackBuilder(waterHeartMaterial)
                .withDisplayName(waterHeartName)
                .withLore(waterHeartLore)
                .withEnchantment(waterHeartEnchanted ? Enchantment.ARROW_INFINITE : null)
                .hideFlags()
                .build();
    }

    public static ItemStack getVoidHeartItem() {
        return new ItemStackBuilder(voidHeartMaterial)
                .withDisplayName(voidHeartName)
                .withLore(voidHeartLore)
                .withEnchantment(voidHeartEnchanted ? Enchantment.ARROW_INFINITE : null)
                .hideFlags()
                .build();
    }
}
