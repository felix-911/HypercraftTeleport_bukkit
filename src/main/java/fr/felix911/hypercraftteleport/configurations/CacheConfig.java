package fr.felix911.hypercraftteleport.configurations;

import org.bukkit.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheConfig {
    private Configuration config;

    public Map<UUID, String> load(Configuration config) {

        this.config = config;

        Map<UUID, String> cache = new HashMap<>();
        if (config.getConfigurationSection("Cache") != null) {
            for (String uuid : config.getConfigurationSection("Cache").getKeys(false)) {
                    UUID playerUUID = UUID.fromString(uuid);
                    String location = config.getString("Cache." + uuid);
                    cache.put(playerUUID, location);
                }
            }
        return cache;
    }

}
