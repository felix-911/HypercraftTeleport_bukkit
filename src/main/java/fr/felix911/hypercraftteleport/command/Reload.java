package fr.felix911.hypercraftteleport.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandAlias("homesReloadBukkit")
public class Reload extends BaseCommand {
    private final HypercraftTeleport plugin;

    public Reload(HypercraftTeleport hypercraftHomes) {
        this.plugin = hypercraftHomes;
    }

    @Default
    @CommandPermission("hypercraftteleport.command.reload")
    @CommandCompletion("@reload @nothing")
    public void homesreload(CommandSender sender, String args) {

        if (sender instanceof Player) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                switch (args){

                    case "gui" :
                        plugin.getConfigurationManager().reloadGui(sender);
                        break;

                    case "language" :
                        plugin.getConfigurationManager().reloadLanguage(sender);
                        break;

                    case "cache" :
                        plugin.getHomesCache().reloadCache(sender);
                        break;

                    case "all" :
                        plugin.getConfigurationManager().reloadLanguage(sender);
                        plugin.getHomesCache().reloadCache(sender);
                        break;

                    default:
                        String msg = plugin.getConfigurationManager().getLang().getWhatReload();
                        sender.sendMessage(msg);
                }
            });
        }
    }
}