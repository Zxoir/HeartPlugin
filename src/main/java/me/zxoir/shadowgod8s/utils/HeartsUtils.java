package me.zxoir.shadowgod8s.utils;

import lombok.Getter;
import me.zxoir.shadowgod8s.hearts.*;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class HeartsUtils {
    @Getter
    private static final Map<Class<? extends Heart>, ItemStack> heartItemMap = new HashMap<>();

    public static @NotNull String colorize(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] characters = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char character : characters) {
                builder.append("&").append(character);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void displayHeartBar(Player player, @NotNull Heart heart) {
        switch (heart.getName()) {
            case "Flame" ->
                    sendActionBar(player, ConfigManager.getFlameHeartText() + " " + (heart.isAbilityReady() ? ConfigManager.getFlameHeartActiveText() : ConfigManager.getFlameHeartCooldownText((FlameHeart) heart)));

            case "Void" ->
                    sendActionBar(player, ConfigManager.getVoidHeartText() + " " + (heart.isAbilityReady() ? ConfigManager.getVoidHeartActiveText() : ((VoidHeart) heart).getCharges() <= 0 ? ConfigManager.getVoidHeartCooldownText((VoidHeart) heart) : ConfigManager.getVoidHeartActivedText((VoidHeart) heart)));

            case "Locked" ->
                    sendActionBar(player, ConfigManager.getLockedHeartText() + " " + (heart.isAbilityReady() ? ConfigManager.getLockedHeartActiveText() : ConfigManager.getLockedHeartCooldownText((LockedHeart) heart)));

            case "Strength" ->
                    sendActionBar(player, ConfigManager.getStrengthHeartText() + " " + (heart.isAbilityReady() ? ConfigManager.getStrengthHeartActiveText() : ConfigManager.getStrengthHeartCooldownText((StrengthHeart) heart)));

            case "Xray" -> sendActionBar(player, ConfigManager.getXrayHeartText());

            case "Water" -> sendActionBar(player, ConfigManager.getWaterHeartText());
        }
    }

    public static boolean isHeartItem(@NotNull ItemStack itemStack) {
        return itemStack.isSimilar(ConfigManager.getFlameHeartItem()) || itemStack.isSimilar(ConfigManager.getLockedHeartItem())
                || itemStack.isSimilar(ConfigManager.getStrengthHeartItem()) || itemStack.isSimilar(ConfigManager.getVoidHeartItem())
                || itemStack.isSimilar(ConfigManager.getWaterHeartItem()) || itemStack.isSimilar(ConfigManager.getXrayHeartItem());
    }

    public static Heart getHeartByItem(@NotNull ItemStack itemStack, UUID uuid) {
        if (itemStack.isSimilar(ConfigManager.getFlameHeartItem()))
            return new FlameHeart(uuid);

        if (itemStack.isSimilar(ConfigManager.getLockedHeartItem()))
            return new LockedHeart(uuid);

        if (itemStack.isSimilar(ConfigManager.getStrengthHeartItem()))
            return new StrengthHeart(uuid);

        if (itemStack.isSimilar(ConfigManager.getVoidHeartItem()))
            return new VoidHeart(uuid);

        if (itemStack.isSimilar(ConfigManager.getWaterHeartItem()))
            return new WaterHeart(uuid);

        if (itemStack.isSimilar(ConfigManager.getXrayHeartItem()))
            return new XrayHeart(uuid);

        return null;
    }

    public static @Nullable Heart getHeartByName(String heart, UUID uuid) {
        return switch (heart.toLowerCase()) {
            case "flame" -> new FlameHeart(uuid);
            case "locked" -> new LockedHeart(uuid);
            case "xray" -> new XrayHeart(uuid);
            case "strength" -> new StrengthHeart(uuid);
            case "water" -> new WaterHeart(uuid);
            case "void" -> new VoidHeart(uuid);
            default -> null;
        };
    }

    public static void sendActionBar(@NotNull Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static ItemStack getHeartItem(@NotNull Heart heart) {
        return heartItemMap.get(heart.getClass()).clone();
    }
}
