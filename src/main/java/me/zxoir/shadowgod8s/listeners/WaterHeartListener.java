package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.hearts.WaterHeart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/18/2024
 */
public class WaterHeartListener implements Listener {
    private final Shadowgod8s plugin = Shadowgod8s.getPlugin(Shadowgod8s.class);
    private final Map<UUID, Integer> drownCountMap = new HashMap<>();
    private final Map<UUID, BukkitRunnable> drownResetTasks = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.getLastDamageCause() == null || !player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
            resetDrownCount(player.getUniqueId());
            return;
        }

        int drownCount = drownCountMap.getOrDefault(player.getUniqueId(), 0) + 1;

        drownCountMap.put(player.getUniqueId(), drownCount);
        resetDrownTask(player);

        if (drownCount >= ConfigManager.getDrownTries()) {
            HeartManager.setHeart(player, new WaterHeart(player.getUniqueId()), false);
            resetDrownCount(player.getUniqueId());
        }
    }

    private void resetDrownTask(Player player) {
        UUID playerId = player.getUniqueId();

        if (drownResetTasks.containsKey(playerId)) {
            drownResetTasks.get(playerId).cancel();
        }

        BukkitRunnable resetTask = new BukkitRunnable() {
            @Override
            public void run() {
                resetDrownCount(playerId);
            }
        };

        resetTask.runTaskLater(plugin, ConfigManager.getDrownTimeLimit() * 20L);
        drownResetTasks.put(playerId, resetTask);
    }

    private void resetDrownCount(UUID playerId) {
        drownCountMap.remove(playerId);
        drownResetTasks.remove(playerId);
    }
}
