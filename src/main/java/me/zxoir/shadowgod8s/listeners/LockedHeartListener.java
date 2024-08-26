package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.hearts.LockedHeart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class LockedHeartListener implements Listener {
    @EventHandler
    public void onAchievement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();
        NamespacedKey advancementKey = advancement.getKey();

        String requiredAdvancementKey = ConfigManager.getRequiredAchievement();

        if (!advancementKey.toString().equals(requiredAdvancementKey))
            return;

        HeartManager.setHeart(player, new LockedHeart(player.getUniqueId()), false);
    }
}
