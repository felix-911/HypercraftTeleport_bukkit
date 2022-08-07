package fr.felix911.hypercraftteleport.command.homes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.UUID;

@CommandAlias("sethome")
public class SetHome extends BaseCommand {
    private final HypercraftTeleport pl;

    public SetHome(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.sethome")
    @CommandCompletion("@homes @nothing")
    public void sethome(CommandSender commandSender, String homeName){
        pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {

                    if (commandSender instanceof Player){
                        Player sender = (Player) commandSender;

                        String home = homeName;
                        if (home == null){
                            home = "home";
                        }
                        home = home.toLowerCase().replace("¤","");

                        if (home.length() > 32){
                            String msg = pl.getConfigurationManager().getLang().getMaxCara();
                            commandSender.sendMessage(msg);
                        } else {
                            HomeObject homeObject = createHomeObject(sender, home);

                            JSONObject json = HypercraftTeleport.serializeHomeToJson(sender, sender.getUniqueId(), homeObject);
                            pl.sendHome(sender, json);
                        }
                    } else {
                        commandSender.sendMessage(pl.getConfigurationManager().getLang().getNoConsole());
                    }
                }
        );}

    @Default
    @CommandPermission("hypercraftteleport.command.sethome.other")
    @CommandCompletion("@homes @players")
    public void sethome(CommandSender commandSender, String homeName, String playerName){
        pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {
            if (commandSender instanceof Player){
                Player sender = (Player) commandSender;

                UUID playerUUID = ApiBukkit.getPlayerUUID(playerName);
                if (playerUUID == null){
                    String out = pl.getConfigurationManager().getLang().getNotFound();
                    out = out.replace("{player}", playerName);
                    sender.sendMessage(out);
                } else {
                    String home = homeName;
                    home = home.toLowerCase().replace("¤","");
                    if (home.length() > 32){
                        String msg = pl.getConfigurationManager().getLang().getMaxCara();
                        commandSender.sendMessage(msg);
                    } else {
                        HomeObject homeObject = createHomeObject(sender, home);
                        JSONObject json = HypercraftTeleport.serializeHomeToJson(sender, sender.getUniqueId(), homeObject);
                        pl.sendHome(sender, json);
                    }
                }
            } else {
                commandSender.sendMessage(pl.getConfigurationManager().getLang().getNoConsole());
            }
        });
    }

    private HomeObject createHomeObject(Player sender, String home){

        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        String server = ApiBukkit.getServerName();
        String world = sender.getWorld().getName();
        double x = Double.parseDouble(df.format(sender.getLocation().getX()).replace(",","."));
        double y = Double.parseDouble(df.format(sender.getLocation().getY()).replace(",","."));
        double z = Double.parseDouble(df.format(sender.getLocation().getZ()).replace(",","."));
        float pitch = Float.parseFloat(df.format(sender.getLocation().getPitch()).replace(",","."));
        float yaw = Float.parseFloat(df.format(sender.getLocation().getYaw()).replace(",","."));
        String block = "WHITE_WOOL";


        return new HomeObject(home, server, world, x, y, z, pitch, yaw, block, -1);
    }
}
