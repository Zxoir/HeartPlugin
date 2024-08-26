package me.zxoir.shadowgod8s.commands;

import me.zxoir.shadowgod8s.hearts.VoidHeart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MIT License Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/19/2024
 */
public class VoidTpCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!HeartManager.hasHeart(player, VoidHeart.class)) {
            player.sendMessage(ConfigManager.getVoidTpNoVoidHeartMessage());
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ConfigManager.getVoidTpIncorrectUsageMessage());
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ConfigManager.getVoidTpPlayerNotFoundMessage());
            return true;
        }

        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(ConfigManager.getVoidTpKillSelfMessage());
            return true;
        }

        VoidHeart heart = (VoidHeart) HeartManager.getHeart(player);
        if (heart.getCharges() <= 0) {
            player.sendMessage(ConfigManager.getVoidTpNoChargesMessage());
            return true;
        }

        heart.voidTeleport(player, target);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return suggestions;
    }
}