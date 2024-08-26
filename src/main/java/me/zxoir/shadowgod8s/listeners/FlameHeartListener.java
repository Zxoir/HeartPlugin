package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.hearts.FlameHeart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class FlameHeartListener implements Listener {
    private final Map<Player, Long> lavaStartTimes = new HashMap<>();
    private final Map<Player, BukkitRunnable> heartGrantTasks = new HashMap<>();
    private long updateLimit = -1;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (updateLimit != -1 && updateLimit - System.currentTimeMillis() > 0)
            return;

        Player player = event.getPlayer();
        long UPDATE_DELAY_MILLISECOND = 500L;
        updateLimit = System.currentTimeMillis() + UPDATE_DELAY_MILLISECOND;

        if (isInLava(player)) {
            startLavaChallenge(player);
        } else {
            stopLavaChallenge(player);
        }
    }

    private boolean isInLava(Player player) {
        return player.getLocation().getBlock().getType() == Material.LAVA;
    }

    private void startLavaChallenge(Player player) {
        if (lavaStartTimes.containsKey(player)) {
            if (hasCompletedChallenge(player)) {
                grantFlameHeart(player);
            }
            return;
        }

        if (HeartManager.hasHeart(player, FlameHeart.class))
            return;

        lavaStartTimes.put(player, System.currentTimeMillis());
        scheduleHeartGrantTask(player);
    }

    private void stopLavaChallenge(Player player) {
        if (!lavaStartTimes.containsKey(player))
            return;

        hasCompletedChallenge(player);
        cancelHeartGrantTask(player);  // Stop the task when the challenge is stopped
        lavaStartTimes.remove(player);
    }

    private boolean hasCompletedChallenge(Player player) {
        long startTime = lavaStartTimes.getOrDefault(player, 0L);
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime >= ConfigManager.getLavaSurvivalTime() * 1000L;
    }

    private void grantFlameHeart(Player player) {
        HeartManager.setHeart(player, new FlameHeart(player.getUniqueId()), false);
        cancelHeartGrantTask(player);
        lavaStartTimes.remove(player);
    }

    private void scheduleHeartGrantTask(Player player) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isInLava(player) && lavaStartTimes.containsKey(player)) {
                    grantFlameHeart(player);
                }
            }
        };

        task.runTaskLater(Shadowgod8s.getPlugin(Shadowgod8s.class), ConfigManager.getLavaSurvivalTime() * 20L); // Convert seconds to ticks (20 ticks = 1 second)
        heartGrantTasks.put(player, task);
    }

    private void cancelHeartGrantTask(Player player) {
        BukkitRunnable task = heartGrantTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        stopLavaChallenge(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        stopLavaChallenge(event.getPlayer());
    }

}
