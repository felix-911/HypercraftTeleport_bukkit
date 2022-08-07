package fr.felix911.hypercraftteleport.command.warps;

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

@CommandAlias("WarpMaterial")
public class WarpMaterial extends BaseCommand {
    private final HypercraftTeleport pl;

    public WarpMaterial(HypercraftTeleport hypercraftHomes) {
        pl=  hypercraftHomes;
    }
    @Default
    @CommandPermission("hypercraftteleport.command.warpmaterial")
    @CommandCompletion("@homes")
    public void material(CommandSender commandSender, String warp) {
        pl.getServer().getScheduler().runTaskAsynchronously(pl, () -> {

            if (commandSender instanceof Player) {
                Player sender = (Player) commandSender;

                ItemStack item = sender.getInventory().getItemInMainHand();

                String material = item.getType().toString();
                Material m = Material.getMaterial(material);

                if (material.equalsIgnoreCase("AIR")){
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

                if (barrierList.contains(m) || switchList.contains(m)){
                    String b = pl.getConfigurationManager().getLang().getNoUsable();
                    commandSender.sendMessage(b);
                    return;
                }


                ByteArrayDataOutput out = ByteStreams.newDataOutput();

                out.writeUTF("warpMaterial");
                out.writeUTF(String.valueOf(sender.getUniqueId()));
                out.writeUTF(warp);
                out.writeUTF(material);
                out.writeInt(customModelData);

                sender.sendPluginMessage(pl, "hypercraft:teleport", out.toByteArray());

            } else {
                String b = pl.getConfigurationManager().getLang().getNoConsole();
                commandSender.sendMessage(b);
            }
        });
    }
}
