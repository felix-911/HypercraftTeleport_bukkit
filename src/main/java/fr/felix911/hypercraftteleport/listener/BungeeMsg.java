package fr.felix911.hypercraftteleport.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.objects.HomeObject;
import fr.felix911.hypercraftteleport.objects.WarpObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class BungeeMsg implements PluginMessageListener {
    private final HypercraftTeleport pl;

    public BungeeMsg(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("hypercraft:teleport")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subChannel = in.readUTF();

            if (subChannel.equalsIgnoreCase("homesListGui")) {
                UUID senderuuid = UUID.fromString(in.readUTF());
                UUID playeruuid = UUID.fromString(in.readUTF());
                String importValue = in.readUTF();
                importValue = importValue.replace("[", "");
                importValue = importValue.replace("]", "");
                String[] values = importValue.split(", ");
                String importHomes = in.readUTF();
                importHomes = importHomes.replace("[", "");
                importHomes = importHomes.replace("]", "");
                String[] homes = importHomes.split(", ");

                Map<String, List<HomeObject>> filtedHomes = new HashMap<>();
                List<HomeObject> allHomes = new ArrayList<>();

                for (String home : homes) {
                    String[] h = home.split("¤");
                    String name = h[0].replace("&", "§");
                    String block = h[1];
                    String world = h[2];
                    String server = h[3];
                    int customModelData = Integer.parseInt(h[4]);

                    HomeObject homeObject = new HomeObject(name, server, world, 0, 0, 0, 0, 0, block, customModelData);

                    if (!allHomes.contains(homeObject)) {
                        allHomes.add(homeObject);
                    }

                    if (filtedHomes.containsKey(server)) {
                        List<HomeObject> list = filtedHomes.get(server);
                        if (!list.contains(homeObject)) {
                            list.add(homeObject);
                            filtedHomes.put(server, list);
                        }
                    } else {
                        List<HomeObject> list = new ArrayList<>();
                        list.add(homeObject);
                        filtedHomes.put(server, list);
                    }
                }

                pl.getHomesCache().getAllHomes().put(playeruuid, allHomes);
                pl.getHomesCache().getFiltedHomes().put(playeruuid, filtedHomes);
                pl.getGuiHomesBase().open(senderuuid, playeruuid, values, null, -1, 0);


            }
            if (subChannel.equalsIgnoreCase("BuyHomes")) {
                UUID uuid = UUID.fromString(in.readUTF());
                int cost = in.readInt();

                OfflinePlayer offlinePlayer = pl.getServer().getOfflinePlayer(uuid);
            }
            if (subChannel.equalsIgnoreCase("homelistcache")) {
                UUID playeruuid = UUID.fromString(in.readUTF());
                String list = in.readUTF();
                list = list.replace("[", "");
                list = list.replace("]", "");
                List<String> homelist = Arrays.asList(list.split(", "));
                pl.getHomesCache().getHomesCache().put(playeruuid, homelist);
            }
            if (subChannel.equalsIgnoreCase("warpListGui")) {
                String jsonString = in.readUTF();
                JSONObject json = null;
                try {
                    json = (JSONObject) new JSONParser().parse(jsonString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                UUID senderUUID = UUID.fromString(json.get("RequestedPlayerUUID").toString().replace("\"",""));
                boolean edit = Boolean.parseBoolean(json.get("Editor").toString().replace("\"",""));
                JSONObject jsonWarpList = (JSONObject) json.get("WarpList");

                Set<String> warpList = jsonWarpList.keySet();
                List<WarpObject> allWarps = new ArrayList<>();


                for (String warpName : warpList) {

                    List<String> warpCache = pl.getWarpList();
                    if (!warpCache.contains(warpName)){
                        warpCache.add(warpName);
                    }

                    JSONObject warp = (JSONObject) jsonWarpList.get(warpName);

                    String server = warp.get("Server").toString().replace("\"", "");
                    String world = warp.get("World").toString().replace("\"", "");
                    double x = Double.parseDouble(warp.get("X").toString().replace("\"", ""));
                    double y = Double.parseDouble(warp.get("Y").toString().replace("\"", ""));
                    double z = Double.parseDouble(warp.get("Z").toString().replace("\"", ""));
                    float pitch = Float.parseFloat(warp.get("Pitch").toString().replace("\"", ""));
                    float yaw = Float.parseFloat(warp.get("Yaw").toString().replace("\"", ""));
                    String block = warp.get("Block").toString().replace("\"", "");
                    int customModelData = Integer.parseInt(warp.get("CustomModelData").toString().replace("\"", ""));
                    boolean perm = Boolean.parseBoolean(warp.get("NeedPerm").toString().replace("\"", ""));

                    WarpObject warpObject = new WarpObject(warpName, server, world, x, y, z, pitch, yaw, block, customModelData, perm);
                    allWarps.add(warpObject);
                }

                   pl.getGuiWarpsBase().open(senderUUID, allWarps, edit);
            }
            if (subChannel.equalsIgnoreCase("tpaDemand")) {
                UUID senderUUID = UUID.fromString(in.readUTF());
                UUID playerUUID = UUID.fromString(in.readUTF());

                pl.getTeleportManager().tpaDemand(senderUUID, playerUUID);
            }
            if (subChannel.equalsIgnoreCase("tpaHereDemand")) {
                UUID senderUUID = UUID.fromString(in.readUTF());
                UUID playerUUID = UUID.fromString(in.readUTF());

                pl.getTeleportManager().tpaHereDemand(senderUUID, playerUUID);
            }

        }
    }
}

