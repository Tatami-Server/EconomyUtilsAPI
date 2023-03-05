package com.tatamiserver.economyutils.listener;

import com.tatamiserver.economyutils.EconomyUtils;
import com.tatamiserver.economyutils.util.DatabaseHandler;
import com.tatamiserver.economyutils.util.TransactionHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class PlayerCommandPreprocess implements Listener {

    private static final Economy eco              = EconomyUtils.getEconomy();
    private static final Plugin plugin            = EconomyUtils.getPlugin();
    private static final DatabaseHandler dbh      = EconomyUtils.getDatabaseHandler();

    @EventHandler
    public void playerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        String[] args = event.getMessage().split(" ");

        if (!Objects.equals(args[0], "/pay")) return;

        Player player               = event.getPlayer();
        Double beforeMoney          = eco.getBalance(player);
        Double amount               = Double.parseDouble(args[2]);
        OfflinePlayer toPlayer      = Bukkit.getOfflinePlayer(args[1]);
        Double toPlayerBeforeMoney  = eco.getBalance(toPlayer);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            double afterMoney           = eco.getBalance(player);
            double toPlayerAfterMoney   = eco.getBalance(toPlayer);
            if ((beforeMoney - amount) == afterMoney
                && (toPlayerBeforeMoney + amount) == toPlayerAfterMoney) {
                // 振込したプレイヤー
                dbh.addTransactionLog(Bukkit.getOfflinePlayer(player.getUniqueId()), "振込", amount, afterMoney, "(/pay)振込先: "+toPlayer.getName()+"("+toPlayer.getUniqueId().toString()+")");

                // 振込先のプレイヤー
                dbh.addTransactionLog(Bukkit.getOfflinePlayer(toPlayer.getUniqueId()), "入金", amount, toPlayerAfterMoney, "(/pay)振込者: "+player.getName()+"("+player.getUniqueId().toString()+")");
            }
        }, 2L);



    }

}
