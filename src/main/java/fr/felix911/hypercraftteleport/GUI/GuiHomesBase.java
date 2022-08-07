package fr.felix911.hypercraftteleport.GUI;

import fr.felix911.apibukkit.ApiBukkit;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import fr.felix911.hypercraftteleport.objects.InvHomesObject;
import fr.felix911.hypercraftteleport.objects.SlotObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GuiHomesBase {
    private final HypercraftTeleport pl;
    private final HashMap<UUID, InvHomesObject> invTampon;
    int guiSize;
    int limitHome;

    public GuiHomesBase(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
        invTampon = new HashMap<>();
        guiSize = pl.getConfigurationManager().getHomesConfig().getSize();
        limitHome = ((guiSize/9)-2)*7;
    }

    public void open(UUID senderuuid, UUID playeruuid, String[] values, List<String> categorie, int iCat, int start) {

        Player sender = pl.getServer().getPlayer(senderuuid);
        String playerName = ApiBukkit.getPlayerName(playeruuid);

        List<HomeObject> allHomes = pl.getHomesCache().getAllHomes().get(playeruuid);
        Map<String , List<HomeObject>> filtedHomes = pl.getHomesCache().getFiltedHomes().get(playeruuid);

        if (categorie == null){
            categorie = new ArrayList<>();
            for (HomeObject home : allHomes) {
                String server = home.getServer();
                if (!categorie.contains(server)){
                    categorie.add(server);
                }
            }
        }
        InvHomesObject inv = new InvHomesObject(senderuuid, playerName, playeruuid, values, categorie, iCat, start);
        invTampon.put(senderuuid, inv);

        Map<Integer, SlotObject> slotList = pl.getConfigurationManager().getHomesConfig().getSlotList();

        String guiName = pl.getConfigurationManager().getHomesConfig().getName().replace("{player}", playerName);

        int homesList;
        if (iCat == -1){
            homesList = allHomes.size();
        } else {
            String server = categorie.get(iCat);
            homesList = filtedHomes.get(server).size();
        }
        int homeInt = start;
        Inventory inventory = Bukkit.createInventory(null, guiSize, guiName);

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
                case "previous":
                    if (start == 0){
                        material = Material.getMaterial(slotObject.getRmaterial());
                        iS = new ItemStack(material);
                        meta = iS.getItemMeta();
                        meta.setDisplayName(slotObject.getRtitle());
                    } else {
                        material = Material.getMaterial(slotObject.getMaterial());
                        iS = new ItemStack(material);
                        meta = iS.getItemMeta();
                        meta.setDisplayName(slotObject.getTitle());
                        for (String s : slotObject.getDescripction()) {
                            s = s.replace("&", "§");
                            description.add(s);
                        }
                    }
                    meta.setLore(description);
                    iS.setItemMeta(meta);
                    break;
                case "next":
                    if (homesList > limitHome && homesList > limitHome + start ){
                        material = Material.getMaterial(slotObject.getMaterial());
                        iS = new ItemStack(material);
                        meta = iS.getItemMeta();
                        meta.setDisplayName(slotObject.getTitle());
                        for (String s : slotObject.getDescripction()) {
                            s = s.replace("&", "§");
                            description.add(s);
                        }
                    } else {
                        material = Material.getMaterial(slotObject.getRmaterial());
                        iS = new ItemStack(material);
                        meta = iS.getItemMeta();
                        meta.setDisplayName(slotObject.getRtitle());
                    }
                    meta.setLore(description);
                    iS.setItemMeta(meta);
                    break;
                case "sign":
                    material = Material.getMaterial(slotObject.getMaterial());
                    iS = new ItemStack(material);
                    meta = iS.getItemMeta();
                    meta.setDisplayName(slotObject.getTitle());
                    for (String s : slotObject.getDescripction()) {
                        s = s.replace("&", "§");
                        s = s.replace("{use}", values[0]);
                        s = s.replace("{total}", values[1]);
                        s = s.replace("{homesRank}", values[2]);
                        s = s.replace("{homesBonus}", values[3]);
                        description.add(s);
                    }
                    meta.setLore(description);
                    iS.setItemMeta(meta);
                    break;
                case "filter":
                    material = Material.getMaterial(slotObject.getMaterial());
                    iS = new ItemStack(material);
                    meta = iS.getItemMeta();
                    meta.setDisplayName(slotObject.getTitle());
                    for (String s : slotObject.getDescripction()) {
                        s = s.replace("&", "§");
                        if (iCat == -1) {
                            s = s.replace("{categorie}", "Tout les serveurs");
                        } else {
                            s = s.replace("{categorie}", categorie.get(iCat));
                            s = s.replace("game", "Survie ");
                        }
                        description.add(s);
                    }
                    meta.setLore(description);
                    iS.setItemMeta(meta);
                    break;
                case "home":
                    if (homeInt < homesList){

                        boolean checkOwnHomes = !senderuuid.toString().equalsIgnoreCase(playeruuid.toString());

                        String name;
                        String world;
                        String server;
                        String block;
                        int customModelData;


                        if (iCat == -1){
                            HomeObject home = allHomes.get(homeInt);
                            homeInt++;
                            name = home.getName();
                            world = home.getWorld();
                            server = home.getServer();
                            block = home.getBlock();
                            customModelData = home.getCustomModelData();
                        } else {
                            server = categorie.get(iCat);
                            List<HomeObject> list = filtedHomes.get(server);

                            HomeObject home = list.get(homeInt);
                            homeInt++;
                            name = home.getName();
                            world = home.getWorld();
                            block = home.getBlock();
                            customModelData = home.getCustomModelData();
                        }
                        material = Material.getMaterial(block);
                        iS = new ItemStack(material);
                        meta = iS.getItemMeta();
                        meta.setDisplayName(name);
                        if (customModelData != -1){
                            meta.setCustomModelData(customModelData);
                        }
                        for (String s : slotObject.getDescripction()) {
                            s = s.replace("&", "§");
                            s = s.replace("{world}", world);
                            s = s.replace("{server}", server);
                            s = s.replace("game", "Survie ");
                            description.add(s);
                        }
                        if (checkOwnHomes) {
                            description.remove(2);
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
    public int getLimitHome() {
        return limitHome;
    }

    public HashMap<UUID, InvHomesObject> getInvTampon() {
        return invTampon;
    }
}
