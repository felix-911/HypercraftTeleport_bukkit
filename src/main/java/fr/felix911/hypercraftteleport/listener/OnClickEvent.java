package fr.felix911.hypercraftteleport.listener;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.InvHomesObject;
import fr.felix911.hypercraftteleport.objects.InvWarpsObject;
import fr.felix911.hypercraftteleport.objects.SlotObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class OnClickEvent implements Listener {
    private final HypercraftTeleport pl;

    public OnClickEvent(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    @EventHandler
    public void OnClick(InventoryClickEvent e){
        Inventory inv = e.getInventory();
        Player player = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();

        if (current == null){
            return;
        }

        InvHomesObject invHomes = pl.getGuiHomesBase().getInvTampon().get(player.getUniqueId());
        InvWarpsObject invWarps = pl.getGuiWarpsBase().getInvTampon().get(player.getUniqueId());

        if (invHomes != null){
            String playerHomeName = invHomes.getPlayerName();
            UUID playerHomeUUID = invHomes.getPlayerUUID();

            if (e.getView().getTitle().equalsIgnoreCase(pl.getConfigurationManager().getHomesConfig().getName().replace("{player}",playerHomeName))){
                e.setCancelled(true);

                List<Material> barrierList = pl.getConfigurationManager().getHomesConfig().getBarrierList();
                List<Material> switchList = pl.getConfigurationManager().getHomesConfig().getSwitchList();

                if (e.getClick().isKeyboardClick() || e.getClick().isShiftClick() || e.getClick().isCreativeAction()){
                    return;
                }

                if (barrierList.contains(current.getType()) || e.getRawSlot() > pl.getConfigurationManager().getHomesConfig().getSize()){
                    return;
                }

                if (switchList.contains(current.getType())){
                    SlotObject previous = pl.getConfigurationManager().getHomesConfig().getPrecious();
                    SlotObject next = pl.getConfigurationManager().getHomesConfig().getNext();
                    SlotObject categorie = pl.getConfigurationManager().getHomesConfig().getCategorie();

                    int limit = pl.getGuiHomesBase().getLimitHome();
                    String[] values = invHomes.getStatsPlayer();
                    List<String> categorieList = invHomes.getCategories();
                    int iCat = invHomes.getIntCat();
                    int start = invHomes.getStart();

                    String button = current.getItemMeta().getDisplayName();

                    if (button.equalsIgnoreCase(previous.getTitle())){
                        pl.getGuiHomesBase().open(player.getUniqueId(),playerHomeUUID,values,categorieList,iCat,0);
                    }else if (button.equalsIgnoreCase(next.getTitle())){
                        if (start == 0){
                            pl.getGuiHomesBase().open(player.getUniqueId(),playerHomeUUID,values,categorieList,iCat,limit);
                        } else {
                            pl.getGuiHomesBase().open(player.getUniqueId(),playerHomeUUID,values,categorieList,iCat,start + limit);
                        }

                    } else if (button.equalsIgnoreCase(categorie.getTitle())){
                        if (e.getClick().isLeftClick()){
                            if (iCat == -1){
                                iCat = categorieList.size()-1;
                            } else {
                                iCat--;
                            }

                            pl.getGuiHomesBase().open(player.getUniqueId(),playerHomeUUID,values,categorieList,iCat,0);
                        } else if (e.getClick().isRightClick()){
                            if (iCat == categorieList.size()-1){
                                iCat = -1;
                            } else {
                                iCat++;
                            }
                            pl.getGuiHomesBase().open(player.getUniqueId(),playerHomeUUID,values,categorieList,iCat,0);
                        }
                        return;
                    }
                    return;
                } else {

                    if (e.getClick().isLeftClick()){
                        String home = current.getItemMeta().getDisplayName().replace("§", "&");

                        pl.getTeleportManager().requestTeleport(player,playerHomeUUID, "home",home);
                        player.closeInventory();
                    } else if (e.getClick().isRightClick()){
                        if (player.getUniqueId().equals(playerHomeUUID)){
                            String home = current.getItemMeta().getDisplayName().replace("§", "&");
                            Bukkit.dispatchCommand(player,"HomeMaterial " + home);
                            player.closeInventory();
                        }else {
                            return;
                        }
                    }
                }

                pl.getGuiHomesBase().getInvTampon().remove(player.getUniqueId());

            }
        }

        if (invWarps != null){
            UUID playerWarpUUID = invWarps.getSenderUUID();
            if (e.getView().getTitle().equalsIgnoreCase(pl.getConfigurationManager().getWarpsConfig().getName())){
                e.setCancelled(true);

                List<Material> barrierList = pl.getConfigurationManager().getHomesConfig().getBarrierList();

                if (e.getClick().isKeyboardClick() || e.getClick().isShiftClick() || e.getClick().isCreativeAction()){
                    return;
                }

                if (barrierList.contains(current.getType())){
                    return;
                }else {
                    if (e.getClick().isLeftClick()){
                        String warp = current.getItemMeta().getDisplayName().replace("§", "&");
                        pl.getTeleportManager().requestTeleport(player,playerWarpUUID,"warp",warp);
                        player.closeInventory();
                    } else {
                            return;
                        }
                    }
                }

                pl.getGuiHomesBase().getInvTampon().remove(player.getUniqueId());

            }
        }
}
