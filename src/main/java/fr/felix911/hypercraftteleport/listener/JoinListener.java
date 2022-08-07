package fr.felix911.hypercraftteleport.listener;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

public class JoinListener implements Listener {
    private final HypercraftTeleport pl;

    public JoinListener(HypercraftTeleport hypercraftHomes) {pl = hypercraftHomes;
    }

    @EventHandler
    public void Join(PlayerJoinEvent e){
        Player player = e.getPlayer();

        pl.getHomesCache().load(player);

        Map<UUID, String> cacheLogout = pl.getConfigurationManager().checkCache();
        if (!cacheLogout.isEmpty()){
            for (UUID uuid : cacheLogout.keySet()){
                pl.getTeleportManager().sendLogoutLocation(uuid,cacheLogout.get(uuid));
                pl.getConfigurationManager().deleteCache(uuid);
            }
        }

    }



}
