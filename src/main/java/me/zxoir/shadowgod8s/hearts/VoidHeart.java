package me.zxoir.shadowgod8s.hearts;

import lombok.Getter;
import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class VoidHeart extends AbstractHeart {
    private final Shadowgod8s main = Shadowgod8s.getPlugin(Shadowgod8s.class);
    @Getter
    int charges = ConfigManager.getVoidTeleportLimit();
    BukkitTask task;
    BukkitTask task2;
    @Getter
    long lastUsedAbility;

    public VoidHeart(UUID uuid) {
        super("Void", uuid, true);
    }

    @Override
    public void handleAbility(Player player) {
        long currentTime = System.currentTimeMillis();

        if (!isAbilityReady())
            return;

        if (task != null && !task.isCancelled())
            task.cancel();
        if (task2 != null && !task2.isCancelled())
            task2.cancel();

        task = Bukkit.getScheduler().runTaskLater(main, () -> charges = 0, ConfigManager.getVoidTeleportDuration() * 20L);
        task2 = Bukkit.getScheduler().runTaskLater(main, this::resetCharges, ConfigManager.getVoidHeartCooldown() * 20L);

        lastUsedAbility = currentTime;
        setAbilityReady(false);
    }

    public void voidTeleport(Player player, Player victim) {
        if (charges <= 0)
            return;

        String voidTpKillKillerMessage = ConfigManager.getVoidTpKillKillerMessage(victim.getName());
        String voidTpKillVictimMessage = ConfigManager.getVoidTpKillVictimMessage(player.getName());
        String voidTpKillAnnounce = ConfigManager.getVoidTpKillAnnounce(player.getName(), victim.getName());

        if (!voidTpKillVictimMessage.isBlank())
            victim.sendMessage(voidTpKillVictimMessage);
        if (!voidTpKillKillerMessage.isBlank())
            player.sendMessage(voidTpKillKillerMessage);
        if (!voidTpKillAnnounce.isBlank())
            Bukkit.broadcastMessage(voidTpKillAnnounce);

        Location voidLocation = victim.getLocation();
        voidLocation.setY(-200);
        victim.teleport(voidLocation);
        charges--;
    }

    private void resetCharges() {
        setAbilityReady(true);
        charges = ConfigManager.getVoidTeleportLimit();
        if (task != null && !task.isCancelled())
            task.cancel();
        if (task2 != null && !task2.isCancelled())
            task2.cancel();
    }

    @Override
    public void stopAbility(Player player) {
        if (task != null && !task.isCancelled())
            task.cancel();
        if (task2 != null && !task2.isCancelled())
            task2.cancel();
    }
}
