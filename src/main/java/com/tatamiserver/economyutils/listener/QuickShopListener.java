package com.tatamiserver.economyutils.listener;

import com.tatamiserver.economyutils.EconomyUtils;
import com.tatamiserver.economyutils.Util;
import com.tatamiserver.economyutils.util.DatabaseHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.event.ShopSuccessPurchaseEvent;
import org.maxgamer.quickshop.api.shop.Shop;
import org.maxgamer.quickshop.api.shop.ShopType;

public class QuickShopListener implements Listener {

    private static final Economy eco          = EconomyUtils.getEconomy();
    private static final DatabaseHandler dh   = EconomyUtils.getDatabaseHandler();

    @EventHandler
    public void shopSuccessPurchase(ShopSuccessPurchaseEvent event) {
        Shop shop       = event.getShop();
        String action   = "";
        Double amount   = 0.0;

        if (shop.getShopType() == ShopType.SELLING) {
            action = "QuickShopでの購入";
            amount = -shop.getPrice();
        }
        if (shop.getShopType() == ShopType.BUYING) {
            action = "QuickShopでの販売";
            amount = shop.getPrice();
        }

        // ロギングに必要な情報
        Player player       = event.getPlayer();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(shop.getOwner());
        Integer shopID      = dh.getShopID(shop.getLocation().getBlockX(), shop.getLocation().getBlockY(), shop.getLocation().getBlockZ());

        Double playerMoney  = eco.getBalance(player);
        Double ownerMoney   = eco.getBalance(owner);

        String memo         = shop.getItem().getType().toString()+"("+event.getAmount()+"個)(ショップID: "+shopID+")";
        String ownerMemo    = "購入者: "+player.getName()+"("+player.getUniqueId()+")(ショップID: "+shopID+")";

        Boolean result      = dh.addTransactionLog(player, action, amount, playerMoney, memo);

        // ショップ所有者に関するロギング
        if (!shop.isUnlimited()) {
            dh.addTransactionLog(owner, "QuickShopでの売上", -amount, ownerMoney, ownerMemo);
        }

        // ロギングに失敗したら
        if (!result) {
            event.setCancelled(true);
            player.sendMessage(Util.getColored("&c取引のロギング中にエラーが発生しました！ この問題を修正するには管理者に連絡してください。"));
        }

    }

}
