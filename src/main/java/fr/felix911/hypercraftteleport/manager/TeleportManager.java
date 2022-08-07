package fr.felix911.hypercraftteleport.manager;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.BoutonsObject;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private final HypercraftTeleport pl;

    private Map<UUID, UUID> tpaDemand;
    private Map<UUID, UUID> tpaHereDemand;

    public TeleportManager(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
        tpaDemand = new HashMap<>();
        tpaHereDemand = new HashMap<>();
    }

    public void requestTeleport(Player player, UUID playerHome, String name){

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("RequestTeleport");
        out.writeUTF(String.valueOf(player.getUniqueId()));
        out.writeUTF(playerHome.toString());
        out.writeUTF(name);
        player.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());
    }

    public void sendLogoutLocation(Player e) {

        Location l = e.getPlayer().getLocation();

        String server = ApiBukkit.getServerName();
        String world = l.getWorld().getName();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        double x = Double.parseDouble(df.format(l.getX()).replace(",","."));
        double y = Double.parseDouble(df.format(l.getY()).replace(",","."));
        double z = Double.parseDouble(df.format(l.getZ()).replace(",","."));
        float pitch = Float.parseFloat(df.format(l.getPitch()).replace(",","."));
        float yaw = Float.parseFloat(df.format(l.getYaw()).replace(",","."));

        String location = server + "¤" + world + "¤" + x + "¤" + y + "¤" + z + "¤" + pitch + "¤" + yaw;

        if (pl.getServer().getOnlinePlayers().size() == 1){
            pl.getConfigurationManager().saveCache(e.getPlayer().getUniqueId(), location);

        } else {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("LogoutLocation");
            out.writeUTF(String.valueOf(e.getPlayer().getUniqueId()));
            out.writeUTF(location);
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            player.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());
        }

    }

    public void sendLogoutLocation(UUID uuid, String location) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("LogoutLocation");
        out.writeUTF(String.valueOf(uuid));
        out.writeUTF(location);

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());

    }

    public void sendDeathLocation(UUID uuid, String location) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        System.out.println(location.length());

        out.writeUTF("DeathLocation");
        out.writeUTF(String.valueOf(uuid));
        out.writeUTF(location);

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());

    }

    public void tpaDemand(UUID senderUUID, UUID playerUUID) {
        String senderName = ApiBukkit.getPlayerName(senderUUID);
        Player player = pl.getServer().getPlayer(playerUUID);
        if (tpaDemand.containsKey(playerUUID)){
            tpaDemand.remove(player.getUniqueId());
            String expired = pl.getConfigurationManager().getLang().getRequestTpaExpired();
            player.sendMessage(expired);
        }
        if (tpaHereDemand.containsKey(playerUUID)){
            tpaHereDemand.remove(player.getUniqueId());
            String expired = pl.getConfigurationManager().getLang().getRequestTpaExpired();
            player.sendMessage(expired);
        }


        tpaDemand.put(playerUUID,senderUUID);

        String request = pl.getConfigurationManager().getLang().getRequestTpa();
        request = request.replace("{player}",senderName);

        player.sendMessage(request);

            TextComponent buttons = new TextComponent();
            buttons.addExtra(new BoutonsObject(pl, "Accept","/tpaccept").create());
            buttons.addExtra(new BoutonsObject(pl, "Deny", "/tparefuse").create());
            player.sendMessage(buttons);


        pl.getServer().getScheduler().runTaskLaterAsynchronously(pl, () -> {
            tpaDemand.remove(playerUUID);
            String expired = pl.getConfigurationManager().getLang().getRequestTpaExpired();
            player.sendMessage(expired);
        },1200);

    }
    public void tpaHereDemand(UUID senderUUID, UUID playerUUID) {
        String senderName = ApiBukkit.getPlayerName(senderUUID);
        Player player = pl.getServer().getPlayer(playerUUID);
        if (tpaDemand.containsKey(playerUUID)){
            tpaDemand.remove(player.getUniqueId());
            String expired = pl.getConfigurationManager().getLang().getRequestTpaExpired();
            player.sendMessage(expired);
        }
        if (tpaHereDemand.containsKey(playerUUID)){
            tpaHereDemand.remove(player.getUniqueId());
            String expired = pl.getConfigurationManager().getLang().getRequestTpaExpired();
            player.sendMessage(expired);
        }
        tpaHereDemand.put(playerUUID,senderUUID);

        String request = pl.getConfigurationManager().getLang().getRequestTpaHere();
        request = request.replace("{player}",senderName);
        player.sendMessage(request);
        TextComponent buttons = new TextComponent();
        buttons.addExtra(new BoutonsObject(pl, "Accept","/tpaccept").create());
        buttons.addExtra(new BoutonsObject(pl, "Deny", "/tparefuse").create());
        player.sendMessage(buttons);



        pl.getServer().getScheduler().runTaskLaterAsynchronously(pl, () -> {
            tpaHereDemand.remove(playerUUID);
            String expired = pl.getConfigurationManager().getLang().getRequestTpaExpired();
            player.sendMessage(expired);
        },300000);
    }

    public Map<UUID, UUID> getTpaDemand() {
        return tpaDemand;
    }

    public Map<UUID, UUID> getTpaHereDemand() {
        return tpaHereDemand;
    }
}
