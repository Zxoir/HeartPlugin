package me.zxoir.shadowgod8s.hearts;

import lombok.Getter;
import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class XrayHeart extends AbstractHeart {
    @Getter
    private static final NamespacedKey namespacedKey = new NamespacedKey(Shadowgod8s.getPlugin(Shadowgod8s.class), "isGlowing");
    private final Set<Player> glowingPlayers = ConcurrentHashMap.newKeySet();
    private boolean disable = false;

    public XrayHeart(UUID uuid) {
        super("Xray", uuid, true);
    }

    @Override
    public void applyEffect(Player player) {
        for (Player target : player.getWorld().getPlayers()) {
            if (target.getUniqueId().equals(player.getUniqueId()))
                continue;

            double distance = target.getLocation().distance(player.getLocation());
            boolean shouldGlow = distance <= ConfigManager.getXrayVisibilityRange();

            if (shouldGlow && !glowingPlayers.contains(target)) {
                addGlow(player, target);
            } else if (!shouldGlow && glowingPlayers.contains(target)) {
                removeGlow(player, target);
            }
        }
    }

    public void addGlow(Player viewer, Player target) {
        try {
            Shadowgod8s.getGlowingEntities().setGlowing(target, viewer, ChatColor.valueOf(ConfigManager.getXrayGlowColor()));
            glowingPlayers.add(target);
            if (!target.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING))
                target.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, viewer.getUniqueId().toString());
            else
            {
                String key = target.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
                key += "@" + viewer.getUniqueId();
                target.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeGlow(Player viewer, Player target) {
        try {
            Shadowgod8s.getGlowingEntities().unsetGlowing(target, viewer);
            glowingPlayers.remove(target);
            target.getPersistentDataContainer().remove(namespacedKey);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeEffect(Player player) {
        disable = true;
        for (Player target : glowingPlayers) {
            removeGlow(player, target);
        }
        glowingPlayers.clear();
    }

    @Override
    public void handleMove(Player player) {
        if (disable)
            return;

        applyEffect(player);
    }
}