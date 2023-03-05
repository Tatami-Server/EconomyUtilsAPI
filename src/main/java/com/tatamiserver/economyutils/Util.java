package com.tatamiserver.economyutils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {

    public static String PREFIX = getColored("&6[EconomyUtils]");

    public static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void sendMessage(CommandSender sender, String message, Boolean needPrefix) {
        sendMessage((Player) sender, message, needPrefix);
    }
    public static void sendMessage(Player player, String message, Boolean needPrefix) {
        if (needPrefix) {
            player.sendMessage(PREFIX + " " + getColored(message));
        }else {
            player.sendMessage(getColored(message));
        }
    }

}
