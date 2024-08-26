package me.zxoir.shadowgod8s.hearts;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public interface Heart {
    String getName();

    UUID getUuid();

    boolean isAbilityReady();

    void setAbilityReady(boolean abilityReady);

    void applyEffect(Player player);

    void removeEffect(Player player);

    void handleAbility(Player player);

    void stopAbility(Player player);

    void handleDeath(Player player, Player killer);

    void handleKill(Player player, Player victim);

    void handleRespawn(Player player);

    void handleMove(Player player);
}
