package fr.felix911.hypercraftteleport.command.warps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;

@CommandAlias("setwarp")
public class SetWarp extends BaseCommand {
    private final HypercraftTeleport pl;

    public SetWarp(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.setwarp")
    @CommandCompletion("@warps @nothing")
    public void setwarp(CommandSender commandSender, String warp){

        if (commandSender instanceof Player){
            pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {
                String name = warp;
                Player sender = (Player) commandSender;

                char[] array = name.toCharArray();
                array[0] = Character.toUpperCase(array[0]);
                name = new String(array);

                WarpObject warpObject = createWarpObject(sender, name);

                JSONObject json = HypercraftTeleport.serializeWarpToJson(sender, warpObject);
                pl.sendWarp(sender, json);

            });
        } else {
            commandSender.sendMessage(pl.getConfigurationManager().getLang().getNoConsole());
        }
    }

    private WarpObject createWarpObject(Player sender, String warp) {
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

        return new WarpObject(warp, server, world, x, y, z, pitch, yaw, block, -1,false);
    }
}
