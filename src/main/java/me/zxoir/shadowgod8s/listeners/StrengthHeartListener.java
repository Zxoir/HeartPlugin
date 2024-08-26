package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.hearts.StrengthHeart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/17/2024
 */
public class StrengthHeartListener implements Listener {
    private final Map<UUID, Integer> strengthPotionCount = new HashMap<>();
    private final Map<UUID, Long> potionStartTimes = new HashMap<>();

    @EventHandler
    public void onPotionSplash(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() != Material.SPLASH_POTION || !event.getAction().name().contains("RIGHT") || event.useItemInHand().equals(Event.Result.DENY))
            return;

        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta == null || potionMeta.getBasePotionData().getType().getEffectType() == null)
            return;

        if (!potionMeta.getBasePotionData().getType().getEffectType().equals(PotionEffectType.INCREASE_DAMAGE))
            return;

        // Check if the player failed the challenge (include the reward in case he didn't)
        if (hasCompletedChallenge(player)) {
            grantStrengthHeart(player);
            return;
        }

        potionStartTimes.putIfAbsent(player.getUniqueId(), System.currentTimeMillis());
        strengthPotionCount.put(player.getUniqueId(), strengthPotionCount.getOrDefault(player.getUniqueId(), 0) + 1);

        // Check if the player has splashed the required number of potions within the time limit
        if (hasCompletedChallenge(player)) {
            grantStrengthHeart(player);
        }
    }

    private boolean hasCompletedChallenge(Player player) {
        long startTime = potionStartTimes.getOrDefault(player.getUniqueId(), 0L);
        long elapsedTime = System.currentTimeMillis() - startTime;

        boolean passedTimeCheck = elapsedTime <= (ConfigManager.getSplashPotsDuration() * 1000L);
        boolean passedCountCheck = strengthPotionCount.getOrDefault(player.getUniqueId(), 0) >= ConfigManager.getSplashPotsRequired();

        if (!passedCountCheck && !passedTimeCheck) {
            resetPlayerData(player);
            return false;
        }

        return passedTimeCheck && passedCountCheck;
    }

    private void grantStrengthHeart(Player player) {
        HeartManager.setHeart(player, new StrengthHeart(player.getUniqueId()), false);
        resetPlayerData(player);
    }

    private void resetPlayerData(Player player) {
        strengthPotionCount.remove(player.getUniqueId());
        potionStartTimes.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        resetPlayerData(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        resetPlayerData(event.getPlayer());
    }
}
