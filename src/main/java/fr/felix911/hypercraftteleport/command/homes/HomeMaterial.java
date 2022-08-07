package fr.felix911.hypercraftteleport.command.homes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@CommandAlias("HomeMaterial")
public class HomeMaterial extends BaseCommand {
    private final HypercraftTeleport pl;

    public HomeMaterial(HypercraftTeleport hypercraftHomes) {
        pl=  hypercraftHomes;
    }
    @Default
    @CommandPermission("hypercraftteleport.command.homematerial")
    @CommandCompletion("@homes")
    public void material(CommandSender commandSender, String home) {
        pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {

            if (commandSender instanceof Player) {
                Player sender = (Player) commandSender;

                ItemStack item = sender.getInventory().getItemInMainHand();
                Material material = item.getType();

                if (material.equals(Material.AIR)){
                    String b = pl.getConfigurationManager().getLang().getItemHand();
                    commandSender.sendMessage(b);
                    return;
                }

                int customModelData = -1;
                if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()){
                    customModelData = item.getItemMeta().getCustomModelData();
                }

                List<Material> barrierList = pl.getConfigurationManager().getHomesConfig().getBarrierList();
                List<Material> switchList = pl.getConfigurationManager().getHomesConfig().getSwitchList();

                if (barrierList.contains(material) || switchList.contains(material)){
                    String b = pl.getConfigurationManager().getLang().getNoUsable();
                    commandSender.sendMessage(b);
                } else {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("homeMaterial");
                    out.writeUTF(String.valueOf(sender.getUniqueId()));
                    out.writeUTF(home);
                    out.writeUTF(material.toString());
                    out.writeInt(customModelData);

                    sender.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());
                }

            } else {
                String b = pl.getConfigurationManager().getLang().getNoConsole();
                commandSender.sendMessage(b);
            }
        });
    }
}
