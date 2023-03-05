package com.tatamiserver.economyutils.util;

import com.tatamiserver.economyutils.EconomyUtils;
import com.tatamiserver.economyutils.TransactionLog;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.UUID;

public class DatabaseHandler {

    private static final Plugin plugin            = EconomyUtils.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    // Main Database
    private static String URL       = "";
    private static String USER      = "";
    private static String PASS      = "";

    // QuickShopDatabase
    private static String QS_URL    = "";
    private static String QS_USER   = "";
    private static String QS_PASS   = "";


    public static void reloadDatabaseSettings() {
        ConfigurationSection secMain    = config.getConfigurationSection("databases.main");
        URL     = secMain.getString("url", "jdbc:mysql://localhost:3306/economyutils");
        USER    = secMain.getString("user", "economyUtils");
        PASS    = secMain.getString("pass", "");

        // QuickShopDatabase
        ConfigurationSection secQuickShop   = config.getConfigurationSection("databases.quickshop");
        QS_URL     = secQuickShop.getString("url", "jdbc:mysql://localhost:3306/quickshop");
        QS_USER    = secQuickShop.getString("user", "quickshop");
        QS_PASS    = secQuickShop.getString("pass", "");
    }

    // Main
    private Connection getConnection() {
        Connection con  = null;
        try {
            con  = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    // QuickShop
    private Connection getQSConnection() {
        Connection con  = null;
        try {
            con  = DriverManager.getConnection(QS_URL, QS_USER, QS_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public Integer getShopID(Integer x, Integer y, Integer z) {
        Connection con  = getQSConnection();
        if (con == null) return null;
        try {
            PreparedStatement ps    = con.prepareStatement("SELECT `id` FROM `qs_shops` WHERE `x` = ? AND `y` = ? AND `z` = ?;");
            ps.setInt(1, x);
            ps.setInt(2, y);
            ps.setInt(3, z);
            ResultSet result = ps.executeQuery();

            if (result.next()) return result.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean addTransactionLog(OfflinePlayer player, String action, Double amount, Double afterMoney, String memo) {
        UUID uuid   = player.getUniqueId();
        String mcid = player.getName();

        try {
            Connection con          = getConnection();
            PreparedStatement ps    = con.prepareStatement("INSERT INTO `eco_economyLogs` (`id`, `uuid`, `mcid`, `action`, `amount`, `afterMoney`, `memo`, `time`) VALUES (NULL, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)");
            ps.setString(1, uuid.toString());
            ps.setString(2, mcid);
            ps.setString(3, action);
            ps.setDouble(4, amount);
            ps.setDouble(5, afterMoney);
            ps.setString(6, memo);
            ps.executeUpdate();

            con.close();

            con = getConnection();

            PreparedStatement ps_sel    = con.prepareStatement("SELECT * FROM `eco_economyLogs` WHERE uuid = ? AND action = ? AND amount = ? AND afterMoney = ? AND memo = ? ORDER BY id desc");
            ps_sel.setString(1, uuid.toString());
            ps_sel.setString(2, action);
            ps_sel.setDouble(3, amount);
            ps_sel.setDouble(4, afterMoney);
            ps_sel.setString(5, memo);
            ResultSet result    = ps_sel.executeQuery();

            if (result.next()) {
                Integer id          = result.getInt("id");
                ((Player) player).sendMessage(id+"");
                Timestamp timestamp = result.getTimestamp("time");
                TransactionLog tlog = new TransactionLog(player, action, amount, afterMoney, memo, timestamp);
                TransactionHandler.registerLocalLog(id, tlog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
