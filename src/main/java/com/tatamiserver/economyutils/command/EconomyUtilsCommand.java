package com.tatamiserver.economyutils.command;

import com.tatamiserver.economyutils.EconomyUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

class EconomyUtilsCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 権限を持っていなかったら
        if (!sender.hasPermission("economyutils.admin")) {
            sender.sendMessage("");
        }

        if (args.length == 0) {
            return true;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "reload" -> EconomyUtils.reload();
            }
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].startsWith("reload")) tab.add("reload");
        }

        return tab;
    }
}
