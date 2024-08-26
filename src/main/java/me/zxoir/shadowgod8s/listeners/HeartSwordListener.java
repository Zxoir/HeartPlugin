package me.zxoir.shadowgod8s.listeners;

import me.zxoir.shadowgod8s.Shadowgod8s;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/24/2024
 */
public class HeartSwordListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();

        if (!Shadowgod8s.getHeartSword().isSimilar(itemStack))
            return;

        if (!ConfigManager.isHeartSwordDroppable())
            event.setCancelled(true);

        event.getItemDrop().setUnlimitedLifetime(!ConfigManager.isHeartSwordDespawnable());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        if (victim.getKiller() == null)
            return;

        Player killer = victim.getKiller();
        if (!killer.getInventory().getItemInMainHand().isSimilar(Shadowgod8s.getHeartSword()))
            return;

        for (String command : ConfigManager.getHeartSwordKillCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%killed_name%", victim.getName()).replace("%player_name%", killer.getName()));
        }
    }
}
