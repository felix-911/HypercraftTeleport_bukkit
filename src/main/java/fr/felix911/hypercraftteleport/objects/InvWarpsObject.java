package fr.felix911.hypercraftteleport.objects;

import fr.felix911.hypercraftteleport.HypercraftTeleport;

import java.util.List;
import java.util.UUID;

public class InvWarpsObject {
    private HypercraftTeleport pl;
    private UUID senderUUID;
    private List<WarpObject> warps;

    public InvWarpsObject(HypercraftTeleport hypercraftHomes) {
        this.pl = hypercraftHomes;
    }


    public InvWarpsObject(UUID senderuuid, List<WarpObject> warps) {
        this.senderUUID = senderuuid;
        this.warps = warps;
    }


    public UUID getSenderUUID() {
        return senderUUID;
    }

    public List<WarpObject> getWarps() {
        return warps;
    }
}
