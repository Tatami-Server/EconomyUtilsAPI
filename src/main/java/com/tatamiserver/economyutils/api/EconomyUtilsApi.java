package com.tatamiserver.economyutils.api;

import com.tatamiserver.economyutils.EconomyUtils;
import com.tatamiserver.economyutils.util.DatabaseHandler;
import org.bukkit.OfflinePlayer;

class EconomyUtilsApi {

    private static final DatabaseHandler databaseHandler  = EconomyUtils.getDatabaseHandler();

    public static Boolean addVoteRewardLog(OfflinePlayer player, Double amount, Double afterMoney, String voteSite) {
        return addTransactionLog(player, "入金", amount, afterMoney, voteSite+"での投票報酬");
    }

    public static Boolean addTransactionLog(OfflinePlayer player, String action, Double amount, Double afterMoney, String memo) {
        return databaseHandler.addTransactionLog(player, action, amount, afterMoney, memo);
    }

}
