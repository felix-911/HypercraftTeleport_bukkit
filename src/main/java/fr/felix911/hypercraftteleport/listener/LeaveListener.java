package fr.felix911.hypercraftteleport.listener;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {
    private final HypercraftTeleport pl;

    public LeaveListener(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        pl.getHomesCache().getHomesCache().remove(e.getPlayer().getUniqueId());
        pl.getHomesCache().getAllHomes().remove(e.getPlayer().getUniqueId());
        pl.getHomesCache().getFiltedHomes().remove(e.getPlayer().getUniqueId());

        pl.getTeleportManager().sendLogoutLocation(e.getPlayer());
    }
}
