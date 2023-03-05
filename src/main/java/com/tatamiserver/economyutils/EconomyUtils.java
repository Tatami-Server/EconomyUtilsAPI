package com.tatamiserver.economyutils;

import com.tatamiserver.economyutils.listener.PlayerCommandPreprocess;
import com.tatamiserver.economyutils.listener.QuickShopListener;
import com.tatamiserver.economyutils.util.DatabaseHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.api.QuickShopAPI;

public final class EconomyUtils extends JavaPlugin {

    private static Economy eco;
    private static Plugin plugin;
    private static QuickShopAPI quickShopAPI;


    public static Economy getEconomy() { return eco; }
    public static Plugin getPlugin() { return plugin; }

    private static DatabaseHandler databaseHandler = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        // config のセットアップ
        reload();

        databaseHandler = new DatabaseHandler();

        setupEconomy();
        setupQuickShop();

        getServer().getPluginManager().registerEvents(new QuickShopListener(),          this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(),    this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        DatabaseHandler.reloadDatabaseSettings();
    }

    private void setupQuickShop() {
        Plugin quickShop    = Bukkit.getPluginManager().getPlugin("QuickShop");
        if (quickShop.isEnabled()) quickShopAPI = ((QuickShopAPI) quickShop);
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        eco = rsp.getProvider();
        return eco != null;
    }

    public static DatabaseHandler getDatabaseHandler() { return databaseHandler; }
}
