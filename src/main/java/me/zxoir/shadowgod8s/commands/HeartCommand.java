package me.zxoir.shadowgod8s.commands;

import me.zxoir.shadowgod8s.hearts.Heart;
import me.zxoir.shadowgod8s.managers.ConfigManager;
import me.zxoir.shadowgod8s.managers.HeartManager;
import me.zxoir.shadowgod8s.utils.HeartsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.zxoir.shadowgod8s.utils.HeartsUtils.colorize;
import static me.zxoir.shadowgod8s.utils.HeartsUtils.getHeartByName;

/**
 * MIT License
 * Copyright (c) 2024 Zxoir
 *
 * @author Zxoir
 * @since 8/19/2024
 */
public class HeartCommand implements CommandExecutor, TabCompleter {
    List<String> hearts = Arrays.asList("Flame", "Locked", "Xray", "Strength", "Water", "Void", "None");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("shadowgod8s.admin") || sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ConfigManager.getNoPermissionMessage());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /heart <setheart|giveHeartItem|reload> <Player> <Heart>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            ConfigManager.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (args[0].equalsIgnoreCase("setheart")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /heart setheart <Player> <Heart>");
                return true;
            }

            if (args[2].equalsIgnoreCase("none")) {
                HeartManager.removeHeart(target);
                sender.sendMessage(ChatColor.GREEN + "Heart removed for " + target.getName());
                return true;
            }

            Heart heart = getHeartByName(args[2].toLowerCase(), target.getUniqueId());
            if (heart == null) {
                sender.sendMessage(colorize("&cInvalid heart type!"));
                return true;
            }
            HeartManager.setHeart(target, heart, true);
            sender.sendMessage(ChatColor.GREEN + heart.getName() + " Heart set for " + target.getName());
        } else if (args[0].equalsIgnoreCase("giveheartitem")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /heart giveHeartItem <Player> <Heart>");
                return true;
            }

            Heart heart = getHeartByName(args[2].toLowerCase(), target.getUniqueId());
            if (heart == null) {
                sender.sendMessage(colorize("&cInvalid heart type!"));
                return true;
            }
            target.getInventory().addItem(HeartsUtils.getHeartItem(heart));
            sender.sendMessage(ChatColor.GREEN + heart.getName() + " Heart item given to " + target.getName());
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /heart <setheart|giveHeartItem|reload> <Player> <Heart>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("setheart");
            suggestions.add("giveheartitem");
            suggestions.add("reload");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("setheart") || args[0].equalsIgnoreCase("giveheartitem"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("setheart") || args[0].equalsIgnoreCase("giveheartitem"))) {
            suggestions.addAll(hearts.stream()
                    .filter(heart -> heart.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList());
        }

        return suggestions;
    }
}