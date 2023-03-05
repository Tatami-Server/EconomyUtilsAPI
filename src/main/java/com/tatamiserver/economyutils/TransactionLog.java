package com.tatamiserver.economyutils;

import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;

public class TransactionLog {

    private static OfflinePlayer player;
    private static String action;
    private static Double amount;
    private static Double afterMoney;
    private static String memo;
    private static Timestamp timestamp;

    public TransactionLog(OfflinePlayer player_, String action_, Double amount_, Double afterMoney_, String memo_, Timestamp timestamp_) {
        player      = player_;
        action      = action_;
        amount      = amount_;
        afterMoney  = afterMoney_;
        memo        = memo_;
        timestamp   = timestamp_;
    }

    public OfflinePlayer getPlayer() { return player; }
    public String getAction() { return action; }
    public Double getAmount() { return amount; }
    public Double getAfterMoney() { return afterMoney; }
    public String getMemo() { return memo; }
    public Timestamp getTimestamp() { return timestamp; }

}
