package me.zxoir.shadowgod8s.hearts;

import me.zxoir.shadowgod8s.managers.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class WaterHeart extends AbstractHeart {
    public WaterHeart(UUID uuid) {
        super("Water", uuid, true);
    }

    @Override
    public void applyEffect(Player player) {
        if (!ConfigManager.isInfiniteWaterBreathing())
            return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, PotionEffect.INFINITE_DURATION, 0));
    }

    @Override
    public void handleMove(Player player) {
        if (!player.isSwimming() || !isAbilityReady())
            return;

        player.setVelocity(player.getLocation().getDirection().multiply(ConfigManager.getSwimSpeedMultiplier()));
    }

    @Override
    public void removeEffect(Player player) {
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
    }
}
