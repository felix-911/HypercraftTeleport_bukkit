package fr.felix911.hypercraftteleport.GUI;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GuiWarpsBase {
    private final HypercraftTeleport pl;
    private final HashMap<UUID, InvWarpsObject> invTampon;
    int guiSize;
    int limitHome;

    public GuiWarpsBase(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
        invTampon = new HashMap<>();
        guiSize = pl.getConfigurationManager().getWarpsConfig().getSize();
    }

    public void open(UUID senderuuid, List<WarpObject> warps, boolean edit) {

        Player sender = pl.getServer().getPlayer(senderuuid);

        InvWarpsObject inv = new InvWarpsObject(senderuuid, warps);
        invTampon.put(senderuuid, inv);

        Map<Integer, SlotObject> slotList = pl.getConfigurationManager().getWarpsConfig().getSlotList();

        String guiName = pl.getConfigurationManager().getWarpsConfig().getName();

        Inventory inventory = Bukkit.createInventory(null, guiSize, guiName);

        int intWarp = 0;
        for (int i : slotList.keySet()) {
            SlotObject slotObject = slotList.get(i);
            ItemStack iS = new ItemStack(Material.AIR);
            Material material;
            ItemMeta meta;
            List<String> description = new ArrayList<>();
            switch (slotObject.getUtility()) {
                case "barrier":
                    material = Material.getMaterial(slotObject.getMaterial());
                    iS = new ItemStack(material);
                    meta = iS.getItemMeta();
                    meta.setDisplayName(slotObject.getTitle());
                    iS.setItemMeta(meta);
                    break;

                case "warp":
                    String name;
                    String block;
                    int customModelData;

                    if (!(intWarp >= warps.size())){
                        WarpObject warp = warps.get(intWarp);
                        intWarp++;
                        name = warp.getName();
                        block = warp.getBlock();
                        customModelData = warp.getCustomModelData();

                        material = Material.getMaterial(block);
                        iS = new ItemStack(material);
                        meta = iS.getItemMeta();
                        meta.setDisplayName(name);
                        if (customModelData != -1) {
                            meta.setCustomModelData(customModelData);
                        }
                        if (edit) {
                            for (String s : slotObject.getDescripction()) {
                                s = s.replace("&", "§");
                                description.add(s);
                            }
                        }
                        meta.setLore(description);
                        iS.setItemMeta(meta);
                    }
                    break;
            }
            inventory.setItem(i, iS);
        }

        sender.openInventory(inventory);
    }

    public HashMap<UUID, InvWarpsObject> getInvTampon() {
        return invTampon;
    }
}
