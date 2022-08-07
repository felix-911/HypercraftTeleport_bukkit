package fr.felix911.hypercraftteleport.objects;

import fr.felix911.hypercraftteleport.HypercraftTeleport;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BoutonsObject {
    private final HypercraftTeleport pl;
    private String type;
    private String command;

    public BoutonsObject(HypercraftTeleport pl, String type, String command) {
        this.pl = pl;
        this.type = type;
        this.command = command;
    }


    public TextComponent create() {
        TextComponent btn = new TextComponent();
        btn.addExtra(pl.getConfigurationManager().getLang().getConfiguration().getString("Buttons." + this.type + ".Text").replace("&", "§"));
        btn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(pl.getConfigurationManager().getLang().getConfiguration().getString("Buttons." + this.type + ".Hover").replace("&", "§"))).create()));
        btn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.command));
        return btn;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
