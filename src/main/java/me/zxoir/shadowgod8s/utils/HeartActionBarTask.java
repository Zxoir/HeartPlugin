package me.zxoir.shadowgod8s.utils;

import me.zxoir.shadowgod8s.hearts.Heart;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/18/2024
 */
public class HeartActionBarTask extends BukkitRunnable {
    @Override
    public void run() {
        // Iterate only through players who have hearts
        if (HeartManager.getActiveHearts().isEmpty())
            return;

        for (Map.Entry<UUID, Heart> entry : HeartManager.getActiveHearts().entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());

            if (player == null || !player.isOnline())
                continue;

            Heart heart = entry.getValue();
            HeartsUtils.displayHeartBar(player, heart);
        }
    }
}
