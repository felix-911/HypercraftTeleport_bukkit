package fr.felix911.hypercraftteleport.objects;

import fr.felix911.hypercraftteleport.HypercraftTeleport;

import java.util.List;

public class SlotObject {
    private HypercraftTeleport pl;
    private String utility;
    private String material;
    private String title;
    private List<String> descripction;
    private boolean remplacement;
    private String rmaterial;
    private String rtitle;
    private List<String> rdescripction;

    public SlotObject(HypercraftTeleport hypercraftHomes) {
        pl = hypercraftHomes;
    }

    public SlotObject(String utility, String material, String title, List<String> description, boolean remplacement, String rMaterial, String rTitle, List<String> rDescription){

        this.utility = utility;
        this.material = material;
        this.title = title;
        this.descripction = description;
        this.remplacement = remplacement;
        this.rmaterial = rMaterial;
        this.rtitle = rTitle;
        this.rdescripction = rDescription;
    }

    public String getUtility() {
        return utility;
    }

    public String getMaterial() {
        return material;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDescripction() {
        return descripction;
    }

    public boolean isRemplacement() {
        return remplacement;
    }

    public String getRmaterial() {
        return rmaterial;
    }

    public String getRtitle() {
        return rtitle;
    }

    public List<String> getRdescripction() {
        return rdescripction;
    }
}
