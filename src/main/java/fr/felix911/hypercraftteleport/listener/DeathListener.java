package fr.felix911.hypercraftteleport.listener;

import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.LocationObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.text.DecimalFormat;

public class DeathListener implements Listener {
    private final HypercraftTeleport pl;

    public DeathListener(HypercraftTeleport hypercraftHomes) {pl = hypercraftHomes;
    }

    @EventHandler
    public void DeathListener(PlayerDeathEvent e){
        Player player = e.getEntity();
        String server = ApiBukkit.getServerName();
        String world = player.getLocation().getWorld().getName();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        double x = Double.parseDouble(df.format(player.getLocation().getX()).replace(",","."));
        double y = Double.parseDouble(df.format(player.getLocation().getY()).replace(",","."));
        double z = Double.parseDouble(df.format(player.getLocation().getZ()).replace(",","."));
        float pitch = Float.parseFloat(df.format(player.getLocation().getPitch()).replace(",","."));
        float yaw = Float.parseFloat(df.format(player.getLocation().getYaw()).replace(",","."));

        LocationObject location = new LocationObject(server,world,x,y,z,pitch,yaw);
        String json = HypercraftTeleport.locationToJson(location);
        pl.getTeleportManager().sendDeathLocation(player.getUniqueId(), json);
    }

}
