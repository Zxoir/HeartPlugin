package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.hearts.Heart;
import me.zxoir.shadowgod8s.hearts.XrayHeart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import me.zxoir.shadowgod8s.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/20/2024
 */
public class XrayHeartListener implements Listener {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!event.isSneaking())
            return;

        long currentTime = System.currentTimeMillis();
        long cooldownEnd = cooldowns.getOrDefault(player.getUniqueId(), 0L);

        if (currentTime < cooldownEnd) {
            long remainingTime = (cooldownEnd - currentTime);
            player.sendMessage(ConfigManager.getXrayHeartCooldownMessage(TimeUtil.formatTime(remainingTime, false, true)));
            return;
        }

        // Check if player is at the center of a 5x5 area
        Location center = player.getLocation();
        if (!isAt5x5Center(center))
            return;

        if (!areCornersOccupied(center))
            return;

        HeartManager.setHeart(player, new XrayHeart(player.getUniqueId()), false);
        cooldowns.put(player.getUniqueId(), currentTime + (ConfigManager.getXrayHeartCooldown() * 1000L));
        Bukkit.getScheduler().runTaskLater(Shadowgod8s.getPlugin(Shadowgod8s.class), () -> cooldowns.remove(player.getUniqueId()), ConfigManager.getXrayHeartCooldown() * 20L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!HeartManager.hasHeart(player, XrayHeart.class))
            return;

        Heart heart = HeartManager.getHeart(player);
        heart.applyEffect(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.getPersistentDataContainer().has(XrayHeart.getNamespacedKey(), PersistentDataType.STRING)) {
            String key = player.getPersistentDataContainer().get(XrayHeart.getNamespacedKey(), PersistentDataType.STRING);
            List<UUID> uuids = new ArrayList<>();
            if (key.contains("@")) {
                String[] split = key.split("@");
                for (String uuid : split) {
                    uuids.add(UUID.fromString(uuid));
                }
            } else {
                uuids.add(UUID.fromString(key));
            }

            for (UUID uuid : uuids) {
                Player xrayHeartOwner = Bukkit.getPlayer(uuid);
                if (xrayHeartOwner == null)
                    continue;

                if (!HeartManager.hasHeart(player, XrayHeart.class))
                    continue;
                Heart heart = HeartManager.getHeart(player);
                ((XrayHeart) heart).removeGlow(xrayHeartOwner, player);
            }
        }

        if (!HeartManager.hasHeart(player, XrayHeart.class))
            return;

        Heart heart = HeartManager.getHeart(player);
        heart.removeEffect(player);
    }

    private boolean isAt5x5Center(Location location) {
        int radius = 3; // Since 5x5 area has a radius of 2 blocks from the center

        // Check if the player is at least 2 blocks above the ground to be within a building
        if (location.getBlockY() < 2) {
            return false;
        }

        // Check if the area is enclosed (walls) by examining blocks around the player
        boolean isEnclosed = true;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x == -radius || x == radius || z == -radius || z == radius) {
                    // Check walls around the perimeter
                    if (location.clone().add(x, 0, z).getBlock().getType() == Material.AIR) {
                        isEnclosed = false;
                        break;
                    }
                }
            }
            if (!isEnclosed) {
                break;
            }
        }

        // Additional check for the ground
        Location ground = location.clone().subtract(0, 1, 0);
        return isEnclosed && ground.getBlock().getType() != Material.AIR;
    }


    private boolean areCornersOccupied(Location center) {
        int radius = 2; // 5x5 area has a radius of 2 from the center
        Location[] corners = new Location[]{
                center.clone().add(radius, 0, radius), // Top-right corner
                center.clone().add(radius, 0, -radius), // Top-left corner
                center.clone().add(-radius, 0, radius), // Bottom-right corner
                center.clone().add(-radius, 0, -radius) // Bottom-left corner
        };

        Set<Entity> entitiesInCorners = new HashSet<>();
        for (Location corner : corners) {
            if (corner.getBlock().getType() == Material.AIR) {
                if (corner.getWorld() == null)
                    return false;

                for (Entity entity : corner.getWorld().getNearbyEntities(center, 5, 5, 5)) {
                    if (!entity.getType().equals(EntityType.PLAYER))
                        continue;

                    if (entity.getLocation().distance(corner) < 1.0) {
                        entitiesInCorners.add(entity);
                    }
                }
            }
        }

        return entitiesInCorners.size() == 4;
    }
}
