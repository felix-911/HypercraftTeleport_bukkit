package fr.felix911.hypercraftteleport.manager;


import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandCompletionsManager {
    private final HypercraftTeleport plugin;


    public CommandCompletionsManager(HypercraftTeleport plugin) {
        this.plugin = plugin;
        this.registerCommandCompletions();


    }

    private void registerCommandCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = this.plugin.getCommandManager().getCommandCompletions();

        commandCompletions.registerAsyncCompletion("players", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof Player)) {
                return null;
            } else {
                    return ApiBukkit.getPlayerOnlineNoVanish();
                }
            });

        commandCompletions.registerAsyncCompletion("reload", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof Player)) {
                return null;
            } else {

                String config = "config";
                String language = "language";
                String cache = "cache";
                String all = "all";

                List<String> reload = new ArrayList<>();
                reload.add(config);
                reload.add(language);
                reload.add(cache);
                reload.add(all);

                return new ArrayList<>(reload);
            }
        });

        commandCompletions.registerAsyncCompletion("homes", (c) -> {
            CommandSender sender = c.getSender();
            if (!(sender instanceof Player)) {
                return null;
            } else {
                List<String> homes = plugin.getHomesCache().getHomesCache().get(((Player) sender).getUniqueId());
                if (homes!= null){

                    return new ArrayList<>(homes);
                }else {
                    return null;
                }
            }
        });


    }
}
