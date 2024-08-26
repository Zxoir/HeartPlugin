package me.zxoir.shadowgod8s.hearts;

import lombok.Getter;
import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class LockedHeart extends AbstractHeart {
    private final Shadowgod8s main = Shadowgod8s.getPlugin(Shadowgod8s.class);
    BukkitTask task;
    BukkitTask task2;
    long endTime = -1;
    long timeLeftSeconds = -1;
    @Getter
    long lastAbilityUsed;

    public LockedHeart(UUID uuid) {
        super("Locked", uuid, true);
    }

    @Override
    public void handleAbility(Player player) {
        long duration_ticks = ConfigManager.getResistanceDuration() * 20L;
        long time_left_ticks = timeLeftSeconds * 20L;
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, endTime == -1 ? (int) duration_ticks : (int) time_left_ticks, ConfigManager.getResistanceLevel() - 1));

        if (!isAbilityReady()) {
            startCooldownTask(player, time_left_ticks);
            return;
        }

        setAbilityReady(false);
        lastAbilityUsed = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + (ConfigManager.getResistanceDuration() * 1000L);
        startCooldownTask(player, duration_ticks);
    }

    private void startCooldownTask(Player player, long duration_ticks) {
        if (task != null && !task.isCancelled())
            task.cancel();

        if (task2 != null && !task2.isCancelled())
            task2.cancel();

        task = Bukkit.getScheduler().runTaskLater(main, () -> {
            endTime = -1;
            if (player == null || !player.isOnline())
                return;
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }, duration_ticks);
        task2 = Bukkit.getScheduler().runTaskLater(main, () -> setAbilityReady(true), ConfigManager.getLockedHeartCooldown() * 20L);
    }

    @Override
    public void stopAbility(Player player) {
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

        if (task != null && !task.isCancelled())
            task.cancel();
        if (task2 != null && !task2.isCancelled())
            task2.cancel();
    }

    @Override
    public void handleDeath(Player player, Player killer) {
        if (endTime == -1 || !player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
            return;

        task.cancel();
        timeLeftSeconds = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() / 20L;
    }

    @Override
    public void handleRespawn(Player player) {
        Bukkit.getScheduler().runTaskLater(main, () -> handleAbility(player), 1L);
    }
}
