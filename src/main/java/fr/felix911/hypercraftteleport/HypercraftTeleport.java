package fr.felix911.hypercraftteleport;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.felix911.hypercraftteleport.GUI.GuiHomesBase;
import fr.felix911.hypercraftteleport.GUI.GuiWarpsBase;
import fr.felix911.hypercraftteleport.command.Reload;
import fr.felix911.hypercraftteleport.command.homes.BuyHomes;
import fr.felix911.hypercraftteleport.command.homes.HomeMaterial;
import fr.felix911.hypercraftteleport.command.homes.SetHome;
import fr.felix911.hypercraftteleport.command.spawn.SetSpawn;
import fr.felix911.hypercraftteleport.command.tp.TpAccept;
import fr.felix911.hypercraftteleport.command.tp.TpaRefuse;
import fr.felix911.hypercraftteleport.command.warps.SetWarpCommand;
import fr.felix911.hypercraftteleport.command.warps.WarpMaterial;
import fr.felix911.hypercraftteleport.listener.*;
import fr.felix911.hypercraftteleport.manager.CommandCompletionsManager;
import fr.felix911.hypercraftteleport.manager.CommandManager;
import fr.felix911.hypercraftteleport.manager.ConfigurationManager;
import fr.felix911.hypercraftteleport.manager.TeleportManager;
import fr.felix911.hypercraftteleport.manager.cache.HomesCache;
import fr.felix911.hypercraftteleport.objects.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


public class HypercraftTeleport extends JavaPlugin {


    private CommandManager commandManager;
    private ConfigurationManager configurationManager;
    private TeleportManager teleportManager;
    private int info = 0;

    private GuiHomesBase guiHomesBase;
    private GuiWarpsBase guiWarpsBase;
    private static Economy economy = null;

    private HomesCache homesCache;


    @Override
    public void onEnable() {
        this.getLogger().info(ChatColor.GOLD + "Ouverture du botin");

        configurationManager = new ConfigurationManager(this);
        commandManager = new CommandManager(this);
        teleportManager = new TeleportManager(this);
        homesCache = new HomesCache(this);
        guiHomesBase = new GuiHomesBase(this);
        guiWarpsBase = new GuiWarpsBase(this);
        CommandCompletionsManager cCM = new CommandCompletionsManager(this);
        BungeeMsg bungeeMsg = new BungeeMsg(this);
        HomeObject homeObject = new HomeObject(this);
        SlotObject slotObject = new SlotObject(this);
        InvHomesObject invHomesObject = new InvHomesObject(this);
        InvWarpsObject invWarpsObject = new InvWarpsObject(this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(this), this);
        this.getServer().getPluginManager().registerEvents(new OnClickEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bungeeMsg);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "hypercraft:teleport");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "hypercraft:teleport", bungeeMsg);

        commandManager.registerCommand(new SetHome(this));
        commandManager.registerCommand(new HomeMaterial(this));
        commandManager.registerCommand(new BuyHomes(this));

        commandManager.registerCommand(new Reload(this));

        commandManager.registerCommand(new TpAccept(this));
        commandManager.registerCommand(new TpaRefuse(this));


        commandManager.registerCommand(new SetSpawn(this));

        commandManager.registerCommand(new SetWarpCommand(this));
        commandManager.registerCommand(new WarpMaterial(this));

        setupEconomy();

    }

    public void createDefaultConfiguration(File actual, String defaultName) {
        // Make parent directories
        File parent = actual.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (actual.exists()) {
            return;
        }

        InputStream input = null;
        try {
            JarFile file = new JarFile(this.getFile());
            ZipEntry copy = file.getEntry(defaultName);
            if (copy == null) throw new FileNotFoundException();
            input = file.getInputStream(copy);
        } catch (IOException e) {
            getLogger().severe("Unable to read default configuration: " + defaultName);
        }

        if (input != null) {
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[8192];
                int length;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }

                getLogger().info("Default configuration file written: " + actual.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException ignored) {
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ignored) {
                }
            }
        }


    }

    public void setupEconomy(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }


    public void sendHome(Player sender, JSONObject json) {

        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("sethome");
            out.writeUTF(json.toJSONString());

            sender.sendPluginMessage(this, "hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            getLogger().info(ChatColor.RED + "error envoi" + ChatColor.GOLD + " teleport");
        }
    }

    public void sendWarp(Player sender, WarpObject warpObject) {

        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("setwarp");
            out.writeUTF(String.valueOf(sender.getUniqueId()));
            out.writeUTF(String.valueOf(warpObject.getName()));
            out.writeUTF(String.valueOf(warpObject.getServer()));
            out.writeUTF(String.valueOf(warpObject.getWorld()));
            out.writeDouble(warpObject.getX());
            out.writeDouble(warpObject.getY());
            out.writeDouble(warpObject.getZ());
            out.writeFloat(warpObject.getPitch());
            out.writeFloat(warpObject.getYaw());
            out.writeUTF(warpObject.getBlock());
            out.writeInt(warpObject.getCustomModelData());
            out.writeBoolean(warpObject.isNeedPerm());

            sender.sendPluginMessage(this, "hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            getLogger().info(ChatColor.RED + "error envoi" + ChatColor.GOLD + " teleport");
        }
    }

    public void sendSpawn(Player sender, String server, String world, double x, double y, double z, float pitch, float yaw) {

        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("setspawn");
            out.writeUTF(String.valueOf(sender.getUniqueId()));
            out.writeUTF(server);
            out.writeUTF(world);
            out.writeDouble(x);
            out.writeDouble(y);
            out.writeDouble(z);
            out.writeFloat(pitch);
            out.writeFloat(yaw);

            sender.sendPluginMessage(this, "hypercraft:teleport", out.toByteArray());
        } catch (Exception e) {
            getLogger().info(ChatColor.RED + "error envoi" + ChatColor.GOLD + " teleport");
        }

    }

    public static JSONObject serializeHomeToJson(Player sender, UUID UUID, HomeObject home) {

        JSONObject json = new JSONObject();

        json.put("RequestedPlayerUUID", sender.getUniqueId());
        json.put("PlayerHomeUUID", UUID);

        JSONObject jSonHome = new JSONObject();

        jSonHome.put("Name", home.getName());
        jSonHome.put("Server", home.getServer());
        jSonHome.put("World", home.getWorld());
        jSonHome.put("X", home.getX());
        jSonHome.put("Y", home.getY());
        jSonHome.put("Z", home.getZ());
        jSonHome.put("Pitch", home.getPitch());
        jSonHome.put("Yaw", home.getY());
        jSonHome.put("Block", home.getBlock());
        jSonHome.put("CustomModelData", home.getCustomModelData());

        json.put("home",jSonHome);
        return json;
    }
    //Getter

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public HomesCache getHomesCache() {
        return homesCache;
    }


    public GuiHomesBase getGuiHomesBase() {
        return guiHomesBase;
    }

    public GuiWarpsBase getGuiWarpsBase() {
        return guiWarpsBase;
    }

    public static Economy getEconomy() {
        return economy;
    }


    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }
}
