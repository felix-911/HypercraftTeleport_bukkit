package fr.felix911.hypercraftteleport.configurations;

import fr.felix911.hypercraftteleport.objects.SlotObject;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiWarpsConfig {

    private int size;
    private String name;
    private Map<Integer,SlotObject> slotList = new HashMap<>();
    private List<Material> barrierList = new ArrayList<>();

    public void load(Configuration config) {

        size = config.getInt("gui.size");
        name = config.getString("gui.name").replace("&", "§");

        SlotObject slotObject;
        for (String slot : config.getConfigurationSection("block").getKeys(false)){

            String material = config.getString("block." + slot + ".material");
            Material m;

            switch (config.getString("block." + slot + ".utility")){
                case "barrier":
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
                case "warp":
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
}
