package fr.felix911.hypercraftteleport.command.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.SpawnObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;

@CommandAlias("setspawn")
public class SetSpawn extends BaseCommand {
    private final HypercraftTeleport pl;

    public SetSpawn(HypercraftTeleport hypercraftTeleport) {this.pl = hypercraftTeleport;
    }

    @CommandAlias("setspawn")
    @CommandPermission("hypercraftteleport.command.setspawn")
    public void setspawn(CommandSender commandSender){

        BaseComponent b;
        if (commandSender instanceof Player){
            pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {
                    Player sender = (Player) commandSender;

                SpawnObject spawnObject = createSpawnObject(sender);

                JSONObject json = pl.serializeSpawnToJson(sender, spawnObject);
                pl.sendSpawn(sender, json);

            });

        } else {
            String nop = pl.getConfigurationManager().getLang().getNoConsole();
            b = new TextComponent(nop);
            commandSender.sendMessage(b);
        }
    }

    public SpawnObject createSpawnObject(Player sender){

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        String server = ApiBukkit.getServerName();
        String world = sender.getWorld().getName();
        double x = Double.parseDouble(df.format(sender.getLocation().getX()).replace(",","."));
        double y = Double.parseDouble(df.format(sender.getLocation().getY()).replace(",","."));
        double z = Double.parseDouble(df.format(sender.getLocation().getZ()).replace(",","."));
        float pitch = Float.parseFloat(df.format(sender.getLocation().getPitch()).replace(",","."));
        float yaw = Float.parseFloat(df.format(sender.getLocation().getYaw()).replace(",","."));

        return new SpawnObject(server, world, x, y, z, pitch, yaw);
    }
}
