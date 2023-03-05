package com.tatamiserver.economyutils.util;

import com.tatamiserver.economyutils.EconomyUtils;
import com.tatamiserver.economyutils.TransactionLog;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionHandler {

    private static HashMap<Integer, TransactionLog> logs                = new HashMap<>();
    private static HashMap<OfflinePlayer, List<Integer>> logsByPlayer   = new HashMap<>();


    public static HashMap<Integer, TransactionLog> getAllLogsByPlayer(OfflinePlayer player) {
        HashMap<Integer, TransactionLog> logs_  = new HashMap<>();
        if (!logsByPlayer.containsKey(player)) return logs_;

        for (Integer id : logsByPlayer.get(player)) {
            logs_.put(id, logs.get(id));
        }
        return logs_;
    }

    public static void registerLocalLog(Integer id, TransactionLog transactionLog) {
        OfflinePlayer player    = transactionLog.getPlayer();

        logs.put(id, transactionLog);

        if (logsByPlayer.containsKey(player)) {
            List<Integer> logIds = logsByPlayer.get(player);
            logIds.add(id);
            logsByPlayer.replace(player, logIds);
        }else {
            List<Integer> logIds = new ArrayList<>();
            logIds.add(id);
            logsByPlayer.put(player, logIds);
        }

    }

}
