package fr.felix911.hypercraftteleport.manager;


import fr.felix911.hypercraftteleport.HypercraftTeleport;
import fr.felix911.hypercraftteleport.configurations.CacheConfig;
import fr.felix911.hypercraftteleport.configurations.GuiHomesConfig;
import fr.felix911.hypercraftteleport.configurations.GuiWarpsConfig;
import fr.felix911.hypercraftteleport.configurations.LangConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


public class ConfigurationManager {

    private final HypercraftTeleport plugin;

    private final LangConfig lang = new LangConfig();
    private final GuiHomesConfig homesConfig = new GuiHomesConfig();
    private final GuiWarpsConfig warpsConfig = new GuiWarpsConfig();
    private final CacheConfig cacheConfig = new CacheConfig();


    public ConfigurationManager(HypercraftTeleport plugin) {
        this.plugin = plugin;
        this.reloadGui(null);
        this.reloadLanguage(null);

    }

    public Map<UUID, String> checkCache() {
        File cacheFile = new File(plugin.getDataFolder(), "cacheLocation.yml");
        plugin.createDefaultConfiguration(cacheFile, "cacheLocation.yml");
        Configuration cacheConf = YamlConfiguration.loadConfiguration(cacheFile);
        return cacheConfig.load(cacheConf);
    }

    public void reloadGui(CommandSender sender) {
        String reloadLanguage = "&eChargement des fichier de Gui".replace("&", "§");
        String reloadLanguageDone = "&aChargement des fichier de Gui terminé".replace("&", "§");

        if (sender != null) {
            sender.sendMessage(reloadLanguage);
        } else {
            plugin.getLogger().info(reloadLanguage);
        }

        File guiHomeFile = new File(plugin.getDataFolder(), "guiHome.yml");
        plugin.createDefaultConfiguration(guiHomeFile, "guiHome.yml");
        Configuration guiHomesConfig = YamlConfiguration.loadConfiguration(guiHomeFile);
        homesConfig.load(guiHomesConfig);
        File guiWarpFile = new File(plugin.getDataFolder(), "guiWarps.yml");
        plugin.createDefaultConfiguration(guiWarpFile, "guiWarps.yml");
        Configuration guiWarpsConfig = YamlConfiguration.loadConfiguration(guiWarpFile);
        warpsConfig.load(guiWarpsConfig);

        if (sender != null) {

            sender.sendMessage(reloadLanguageDone);
        } else {
            plugin.getLogger().info(reloadLanguageDone);
        }
    }

    public void reloadLanguage(CommandSender sender) {

        String reloadLanguage = "&eChargement du fichier de Langue".replace("&", "§");
        String reloadLanguageDone = "&aChargement du fichier de Langue terminé".replace("&", "§");

        if (sender != null) {
            sender.sendMessage(reloadLanguage);
        } else {
            plugin.getLogger().info(reloadLanguage);
        }

        File langFile = new File(plugin.getDataFolder(), "language.yml");
        plugin.createDefaultConfiguration(langFile, "language.yml");
        Configuration langConfig = YamlConfiguration.loadConfiguration(langFile);

        lang.load(langConfig);
        if (sender != null) {

            sender.sendMessage(reloadLanguageDone);
        } else {
            plugin.getLogger().info(reloadLanguageDone);
        }

    }

    public void saveCache(UUID uuid, String location) {

        File racine = plugin.getDataFolder();
        File file = new File(racine.getAbsolutePath() + "/cacheLocation.yml");

        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);

        if (c.getConfigurationSection("Cache") == null){
            c.createSection("Cache");
        }
        c.getConfigurationSection("Cache").set(uuid.toString(), location);
        try {
            c.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCache(UUID uuid) {
        File racine = plugin.getDataFolder();
        File file = new File(racine.getAbsolutePath() + "/cacheLocation.yml");

        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);

        c.set("Cache","");
        try {
            c.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public LangConfig getLang() {
        return lang;
    }

    public GuiHomesConfig getHomesConfig() {
        return homesConfig;
    }

    public GuiWarpsConfig getWarpsConfig() {
        return warpsConfig;
    }

    public CacheConfig getCacheConfig() {return  cacheConfig;}

}
