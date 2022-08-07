package fr.felix911.hypercraftteleport.manager.cache;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomesCache {


    private final HypercraftTeleport plugin;
    private final Map<UUID, List<String>> homesCache;

    private final Map<UUID,Map<String , List<HomeObject>>> filtedHomes;
    private final Map<UUID , List<HomeObject>> allHomes;

    public HomesCache(HypercraftTeleport hypercraftHomes) {
        plugin = hypercraftHomes;
        homesCache = new HashMap<>();
        filtedHomes = new HashMap<>();
        allHomes = new HashMap<>();
    }

    public void reloadCache(CommandSender sender) {

        String reloadCache = "&eRechargement des Homes".replace("&", "§");
        String reloadCacheDone = "&aRechargement des Caches terminé".replace("&", "§");

        if (sender != null) {
            sender.sendMessage(reloadCache);
        } else {
            plugin.getLogger().info(reloadCache);
        }
        homesCache.clear();

        for (Player player : plugin.getServer().getOnlinePlayers()){
            load(player);
        }

        if (sender != null) {
            sender.sendMessage(reloadCacheDone);
        } else {
            plugin.getLogger().info(reloadCacheDone);
        }

    }

    public void load(Player player) {

        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("homelistcache");
            out.writeUTF(String.valueOf(player.getUniqueId()));

            player.sendPluginMessage(plugin, "hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            plugin.getLogger().info(ChatColor.RED + "error envoi" + ChatColor.GOLD + " homelistcache");
        }
    }

    public Map<UUID, List<String>> getHomesCache() {
        return homesCache;
    }

    public Map<UUID, Map<String, List<HomeObject>>> getFiltedHomes() {
        return filtedHomes;
    }

    public Map<UUID, List<HomeObject>> getAllHomes() {
        return allHomes;
    }
}
