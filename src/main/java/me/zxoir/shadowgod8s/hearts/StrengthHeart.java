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
public class StrengthHeart extends AbstractHeart {
    @Getter
    long lastUsedAbility;
    BukkitTask task;

    public StrengthHeart(UUID uuid) {
        super("Strength", uuid, true);
    }

    @Override
    public void handleAbility(Player player) {
        long currentTime = System.currentTimeMillis();

        if (!isAbilityReady())
            return;

        lastUsedAbility = currentTime;
        setAbilityReady(false);
        task = Bukkit.getScheduler().runTaskLater(Shadowgod8s.getPlugin(Shadowgod8s.class), () -> abilityEnd(player), ConfigManager.getStrengthHeartCooldown() * 20L);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (ConfigManager.getAbilityStrengthDuration() * 20L), ConfigManager.getAbilityStrengthLevel() - 1));
    }

    private void abilityEnd(Player player) {
        setAbilityReady(true);
        if (player == null || !player.isOnline())
            return;
        applyEffect(player);
    }

    @Override
    public void stopAbility(Player player) {
        if (task != null && !task.isCancelled())
            task.cancel();
    }

    @Override
    public void applyEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, PotionEffect.INFINITE_DURATION, ConfigManager.getStrengthLevel() - 1));
    }

    @Override
    public void removeEffect(Player player) {
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }
}
