package me.zxoir.shadowgod8s.hearts;

import lombok.Getter;
import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
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
public class FlameHeart extends AbstractHeart {
    @Getter
    private long lastUsedAbility;
    private BukkitTask task;

    public FlameHeart(UUID uuid) {
        super("Flame", uuid, true);
    }

    @Override
    public void handleAbility(Player player) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getFlameHeartCooldown() * 1000L;

        if (cooldownTime > 0) {
            if ((currentTime - lastUsedAbility) < cooldownTime) {
                return;
            }

            setAbilityReady(false);
        }

        lastUsedAbility = System.currentTimeMillis();
        player.getWorld().spawnEntity(player.getLocation().add(0, 0.5, 0), EntityType.FIREBALL);
        task = Bukkit.getScheduler().runTaskLater(Shadowgod8s.getPlugin(Shadowgod8s.class), this::resetCooldown, ConfigManager.getFlameHeartCooldown() * 20L);
    }

    private void resetCooldown() {
        if (task != null && !task.isCancelled())
            task.cancel();

        setAbilityReady(true);
    }

    @Override
    public void applyEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0));
    }

    @Override
    public void removeEffect(Player player) {
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        resetCooldown();
    }
}
