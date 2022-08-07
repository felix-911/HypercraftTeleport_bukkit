package fr.felix911.hypercraftteleport.command.homes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("BuyHomes")
public class BuyHomes extends BaseCommand {
    private final HypercraftTeleport pl;

    public BuyHomes(HypercraftTeleport hypercraftTeleport) {
        pl = hypercraftTeleport;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.buyhome")
    @CommandCompletion("@nothing")
    public void buyHomes(CommandSender sender, String playerString, @Optional String num){
        pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {
                    if (sender instanceof Player){
                        sender.sendMessage(pl.getConfigurationManager().getLang().getNoPerm());
                    } else {
                        int number = 1;

                        if (num != null){
                            try{
                                number = Integer.parseInt(num);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        Player player = pl.getServer().getPlayer(playerString);

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("buyHomes");
                        out.writeUTF(String.valueOf(player.getUniqueId()));
                        out.writeInt(number);

                        player.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());
                    }

                }
        );}
}
