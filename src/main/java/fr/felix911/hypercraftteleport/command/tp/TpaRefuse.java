package fr.felix911.hypercraftteleport.command.tp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;


@CommandAlias("tparefuse")
public class TpaRefuse extends BaseCommand {
    private final HypercraftTeleport pl;

    public TpaRefuse(HypercraftTeleport hypercraftTeleport) {
        pl = hypercraftTeleport;
    }


    @Default
    @CommandPermission("hypercraftteleport.command.tparefuse")
    @CommandCompletion("@nothing @nothing")
    public void tparefuse(CommandSender commandSender){
        pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {

                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        Map<UUID, UUID> tpaCache = pl.getTeleportManager().getTpaDemand();
                        Map<UUID, UUID> tpaHereCache = pl.getTeleportManager().getTpaHereDemand();
                        String targetName;
                        if (!tpaCache.containsKey(player.getUniqueId()) && !tpaHereCache.containsKey(player.getUniqueId())) {
                            String nop = pl.getConfigurationManager().getLang().getTpNoRequest();
                            player.sendMessage(nop);
                            return;
                        } else if (tpaCache.containsKey(player.getUniqueId())) {
                            UUID targetUUID = tpaCache.get(player.getUniqueId());
                            targetName = ApiBukkit.getPlayerName(targetUUID);

                            if (!ApiBukkit.getAllOnlinePlayerCache().contains(targetName)) {
                                String off = pl.getConfigurationManager().getLang().getPlayerOffline();
                                player.sendMessage(off);
                                tpaCache.remove(player.getUniqueId());
                                return;

                            }
                        } else {
                            //here
                            UUID targetUUID = tpaHereCache.get(player.getUniqueId());
                            targetName = ApiBukkit.getPlayerName(targetUUID);

                            if (!ApiBukkit.getAllOnlinePlayerCache().contains(targetName)) {
                                String off = pl.getConfigurationManager().getLang().getPlayerOffline();
                                off = off.replace("{player]",targetName);
                                player.sendMessage(off);
                                tpaHereCache.remove(player.getUniqueId());
                                return;
                            }
                        }
                        String refuse = pl.getConfigurationManager().getLang().getTpRequestRefuse();
                        try {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Message");
                            out.writeUTF(targetName);
                            out.writeUTF(refuse);
                            player.sendPluginMessage(pl, "BungeeCord", out.toByteArray());
                        } catch (Exception e) {
                            pl.getLogger().info(ChatColor.RED + "error envoi" + ChatColor.GOLD +" BungeeCord.Message");
                        }
                    } else {
                        String nop = pl.getConfigurationManager().getLang().getNoConsole();
                        commandSender.sendMessage(nop);
                    }

                }
        );}
}