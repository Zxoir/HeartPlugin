package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.hearts.Heart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import me.zxoir.shadowgod8s.utils.HeartsUtils;
import me.zxoir.shadowgod8s.utils.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/16/2024
 */
public class HeartEventListener implements Listener {
    private long updateLimit = -1;
    private final HashMap<UUID, Long> absorbCooldowns = new HashMap<>();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();


        if (!HeartsUtils.isHeartItem(itemStack))
            return;

        if (!ConfigManager.isDroppableHeart())
            event.setCancelled(true);

        event.getItemDrop().setUnlimitedLifetime(!ConfigManager.isDespawnableHeart());
    }

    @EventHandler
    public void onActivateAbility(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking() || !event.getAction().name().contains("LEFT") || !HeartManager.hasHeart(player))
            return;

        Heart heart = HeartManager.getHeart(player);

        if (!heart.isAbilityReady())
            return;

        heart.handleAbility(player);
        HeartManager.sendHeartActionBarUpdate();
    }

    @EventHandler
    public void onAbsorbHeart(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if (!event.getAction().name().contains("RIGHT"))
            return;

        Heart heart = HeartsUtils.getHeartByItem(mainHandItem, player.getUniqueId());
        if (heart == null)
            return;

        long currentTime = System.currentTimeMillis();
        long cooldownTime = ConfigManager.getAbsorbHeartCooldown() * 1000L; // Convert seconds to milliseconds

        if (absorbCooldowns.containsKey(player.getUniqueId())) {
            long lastAbsorbTime = absorbCooldowns.get(player.getUniqueId());

            if ((currentTime - lastAbsorbTime) < cooldownTime) {
                long timeLeft = (cooldownTime - (currentTime - lastAbsorbTime));
                player.sendMessage(ConfigManager.getAbsorbHeartCooldownMessage(TimeUtil.formatTime(timeLeft, false, true)));
                return;
            }
        }

        Heart oldHeart = HeartManager.getHeart(player);
        if (oldHeart != null && oldHeart.getName().equals(heart.getName())) {
            player.sendMessage(ConfigManager.getAbsorbSameHeartErrorMessage());
            return;
        }

        if (mainHandItem.getAmount() > 1)
            mainHandItem.setAmount(mainHandItem.getAmount() - 1);
        else
            player.getInventory().remove(mainHandItem);

        HeartManager.setHeart(player, heart, true);
        if (cooldownTime > 0)
            absorbCooldowns.put(player.getUniqueId(), currentTime);
        player.sendMessage(ConfigManager.getAbsorbHeartMessage(heart.getName()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (updateLimit != -1 && updateLimit - System.currentTimeMillis() > 0)
            return;

        Player player = event.getPlayer();
        Heart heart = HeartManager.getHeart(player);
        long UPDATE_DELAY_MILLISECOND = 500L;
        updateLimit = System.currentTimeMillis() + UPDATE_DELAY_MILLISECOND;

        if (heart == null)
            return;

        heart.handleMove(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Heart heart = HeartManager.getHeart(player);

        if (heart == null)
            return;

        heart.handleRespawn(player);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        Player victim = event.getEntity();
        Heart victimHeart = HeartManager.getHeart(victim);

        if (victimHeart != null) {
            victimHeart.handleDeath(victim, killer);
        }

        if (killer != null) {
            HeartManager.getHeart(killer).handleKill(killer, victim);
        }
    }
}
