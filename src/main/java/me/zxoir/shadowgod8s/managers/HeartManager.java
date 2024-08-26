package me.zxoir.shadowgod8s.managers;

import lombok.Getter;
import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.hearts.AbstractHeart;
import me.zxoir.shadowgod8s.hearts.Heart;
import me.zxoir.shadowgod8s.utils.HeartActionBarTask;
import me.zxoir.shadowgod8s.utils.HeartsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class HeartManager {
    @Getter
    private static final Map<UUID, Heart> activeHearts = new HashMap<>();
    private static BukkitTask heartActionBarTask;
    private static final Shadowgod8s main = Shadowgod8s.getPlugin(Shadowgod8s.class);

    /**
     * Gives the player a specific heart item.
     *
     * @param player The player to give the heart to.
     * @param heart  The heart to give the player.
     */
    public static void giveHeartItem(@NotNull Player player, Heart heart) {
        if (!ConfigManager.isStealHeart() || heart == null)
            return;

        ItemStack heartItem = HeartsUtils.getHeartItem(heart);
        if (!player.getInventory().addItem(heartItem).isEmpty() && ConfigManager.isDropHeart())
            player.getWorld().dropItem(player.getLocation(), heartItem);
    }

    /**
     * Sets the heart for a player and applies its effects.
     *
     * @param player The player to assign the heart to.
     * @param heart  The heart to apply.
     * @param force  Forces the heart to apply
     */
    public static void setHeart(@NotNull Player player, Heart heart, boolean force) {
        Heart oldHeart = activeHearts.get(player.getUniqueId());

        if (oldHeart != null && oldHeart.getName().equals(heart.getName()))
            return;

        if (oldHeart != null && oldHeart.getName().equals(heart.getName()) && !force) {
            giveHeartItem(player, heart);
            return;
        } else if (oldHeart != null && force && ConfigManager.isGiveOldHeartItem()) {
            giveHeartItem(player, oldHeart);
        }

        if (oldHeart != null) {
            oldHeart.removeEffect(player);
            oldHeart.stopAbility(player);
        }

        activeHearts.put(player.getUniqueId(), heart);
        heart.applyEffect(player);
        sendHeartActionBarUpdate();
    }

    /**
     * Removes the heart from a player and its effects.
     *
     * @param player The player to remove the heart from.
     */
    public static void removeHeart(@NotNull Player player) {
        Heart heart = activeHearts.remove(player.getUniqueId());

        if (heart == null)
            return;

        heart.removeEffect(player);

        if (activeHearts.isEmpty() && heartActionBarTask != null) {
            heartActionBarTask.cancel();
            heartActionBarTask = null;
        }
    }

    /**
     * Retrieves the current heart of a player.
     *
     * @param player The player to check.
     * @return The heart currently assigned to the player.
     */
    public static Heart getHeart(@NotNull Player player) {
        return activeHearts.get(player.getUniqueId());
    }

    /**
     * Checks if a player has a specific heart.
     *
     * @param player     The player to check.
     * @param heartClass The class of the heart to check for.
     * @return true if the player has the specified heart, false otherwise.
     */
    public static boolean hasHeart(Player player, Class<? extends AbstractHeart> heartClass) {
        if (heartClass == null || !hasHeart(player))
            return false;

        Heart heart = getHeart(player);
        return heartClass.isInstance(heart);
    }

    /**
     * Checks if a player has a heart.
     *
     * @param player The player to check.
     * @return true if the player has a heart, false otherwise.
     */
    public static boolean hasHeart(@NotNull Player player) {
        return activeHearts.containsKey(player.getUniqueId());
    }

    /**
     * Starts the Heart Action Bar task, if already started then it updates existing task
     */
    public static void sendHeartActionBarUpdate() {
        if (heartActionBarTask == null) {
            heartActionBarTask = new HeartActionBarTask().runTaskTimer(main, 0L, 20L);
            return;
        }

        heartActionBarTask.cancel();
        heartActionBarTask = new HeartActionBarTask().runTaskTimer(main, 0L, 20L);
    }
}
