package fr.felix911.hypercraftteleport.objects;

import fr.felix911.hypercraftteleport.HypercraftTeleport;

import java.util.List;
import java.util.UUID;

public class InvHomesObject {
    private HypercraftTeleport pl;
    private UUID senderUUID;
    private String playerName;
    private UUID playerUUID;
    private String[] statsPlayer;
    private String[] listHomes;
    private List<String> categories;
    private int intCat;
    private int start;

    public InvHomesObject(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }


    public InvHomesObject(UUID senderuuid, String playerName, UUID playeruuid, String[] values, List<String> categorie, int iCat, int start) {
        this.senderUUID = senderuuid;
        this.playerName = playerName;
        this.playerUUID = playeruuid;
        this.statsPlayer = values;
        this.categories = categorie;
        this.intCat = iCat;
        this.start = start;
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String[] getStatsPlayer() {
        return statsPlayer;
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getIntCat() {
        return intCat;
    }

    public int getStart() {
        return start;
    }
}
