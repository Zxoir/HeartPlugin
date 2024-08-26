package me.zxoir.shadowgod8s.hearts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */

@Getter
@AllArgsConstructor
public abstract class AbstractHeart implements Heart {
    private final String name;
    private final UUID uuid;
    @Setter
    private boolean abilityReady;

    @Override
    public void handleDeath(Player player, Player killer) {
        HeartManager.removeHeart(player);
    }

    @Override
    public void handleKill(Player player, Player victim) {
        if (!HeartManager.hasHeart(victim))
            return;

        Heart heart = HeartManager.getHeart(victim);
        HeartManager.giveHeartItem(player, heart);
        HeartManager.removeHeart(victim);
    }

    @Override
    public void applyEffect(Player player) {

    }

    @Override
    public void removeEffect(Player player) {

    }

    @Override
    public void handleAbility(Player player) {

    }

    @Override
    public void stopAbility(Player player) {

    }

    @Override
    public void handleRespawn(Player player) {

    }

    @Override
    public void handleMove(Player player) {

    }
}
