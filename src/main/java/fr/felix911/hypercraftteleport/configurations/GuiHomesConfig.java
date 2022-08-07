package fr.felix911.hypercraftteleport.configurations;

import fr.felix911.hypercraftteleport.objects.SlotObject;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiHomesConfig {

    private int size;
    private String name;
    private Map<Integer,SlotObject> slotList = new HashMap<>();
    private List<Material> barrierList = new ArrayList<>();
    private List<Material> switchList = new ArrayList<>();
    private SlotObject precious = null;
    private SlotObject next = null;
    private SlotObject categorie = null;

    public void load(Configuration config) {

        size = config.getInt("gui.size");
        name = config.getString("gui.name").replace("&", "§");

        SlotObject slotObject;
        for (String slot : config.getConfigurationSection("block").getKeys(false)){

            String material = config.getString("block." + slot + ".material");
            Material m;

            switch (config.getString("block." + slot + ".utility")){
                case "barrier":
                case "sign":
                    m = Material.getMaterial(material);
                    if (!barrierList.contains(m)){
                        barrierList.add(m);
                    }
                    slotObject = new SlotObject(
                            config.getString("block." + slot + ".utility"),
                            material,
                            config.getString("block." + slot + ".title").replace("&", "§"),
                            (List<String>) config.getList("block." + slot + ".description"),
                            false,
                            null,
                            null,
                        null
                    );
                    slotList.put(Integer.valueOf(slot),slotObject);
                    break;
                case "home":
                    slotObject = new SlotObject(
                            config.getString("block." + slot + ".utility"),
                            null,
                            null,
                            (List<String>) config.getList("block." + slot + ".description"),
                            false,
                            null,
                            null,
                            null
                    );
                    slotList.put(Integer.valueOf(slot),slotObject);
                    break;
                case "filter":
                    m = Material.getMaterial(material);
                    if (!switchList.contains(m)){
                        switchList.add(m);
                    }
                    categorie = new SlotObject(
                            config.getString("block." + slot + ".utility"),
                            material,
                            config.getString("block." + slot + ".title").replace("&", "§"),
                            (List<String>) config.getList("block." + slot + ".description"),
                            false,
                            null,
                            null,
                            null
                    );
                    slotList.put(Integer.valueOf(slot),categorie);
                    break;
                case "previous":
                    m = Material.getMaterial(material);
                    if (!switchList.contains(m)){
                        switchList.add(m);
                    }
                    precious = new SlotObject(
                            config.getString("block." + slot + ".utility"),
                            config.getString("block." + slot + ".material"),
                            config.getString("block." + slot + ".title").replace("&", "§"),
                            (List<String>) config.getList("block." + slot + ".description"),
                            true,
                            config.getString("block." + slot + ".replacement.material"),
                            config.getString("block." + slot + ".replacement.title").replace("&", "§"),
                            (List<String>) config.getList("block." + slot + ".replacement.description")
                    );
                    slotList.put(Integer.valueOf(slot),precious);
                    break;
                case "next":
                    m = Material.getMaterial(material);
                    if (!switchList.contains(m)){
                        switchList.add(m);
                    }
                    next = new SlotObject(
                            config.getString("block." + slot + ".utility"),
                            config.getString("block." + slot + ".material"),
                            config.getString("block." + slot + ".title").replace("&", "§"),
                            (List<String>) config.getList("block." + slot + ".description"),
                            true,
                            config.getString("block." + slot + ".replacement.material"),
                            config.getString("block." + slot + ".replacement.title").replace("&", "§"),
                            (List<String>) config.getList("block." + slot + ".replacement.description")
                    );
                    slotList.put(Integer.valueOf(slot),next);
                    break;
            }
        }

    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, SlotObject> getSlotList() {
        return slotList;
    }

    public List<Material> getBarrierList() {
        return barrierList;
    }

    public List<Material> getSwitchList() {
        return switchList;
    }

    public SlotObject getPrecious() {
        return precious;
    }

    public SlotObject getNext() {
        return next;
    }

    public SlotObject getCategorie() {
        return categorie;
    }
}
