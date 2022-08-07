package fr.felix911.hypercraftteleport.configurations;

import org.bukkit.configuration.Configuration;
public class LangConfig {

    private Configuration configuration;

    private String noConsole;
    private String noPerm;
    private String notFound;
    private String whatReload;
    private String maxCara;
    private String failTP;
    private String playerOffline;

    private String teleport;
    private String teleportPlayer;
    private String teleportPlayerHere;
    private String requestTpa;
    private String requestTpaHere;
    private String requestTpaExpired;
    private String tpNoRequest;
    private String tpRequestAccept;
    private String tpRequestRefuse;

    private String itemHand;
    private String noUsable;


    public void load(Configuration language) {
        configuration = language;
        noConsole = language.getString("Error.NoConsole").replace("&", "§");
        noPerm = language.getString("Error.NoPerm").replace("&", "§");
        notFound = language.getString("Error.NotFound").replace("&", "§");
        whatReload = language.getString("Error.WhatReload").replace("&", "§");
        maxCara = language.getString("Error.NameLenght").replace("&", "§");
        failTP = language.getString("Error.FailTp").replace("&", "§");
        playerOffline = language.getString("Error.PlayerOffline").replace("&", "§");

        teleport = language.getString("Teleport.Teleport").replace("&", "§");
        teleportPlayer = language.getString("Teleport.ToPlayer").replace("&", "§");
        teleportPlayerHere = language.getString("Teleport.ToPlayerHere").replace("&", "§");
        requestTpa = language.getString("Teleport.RequestTPa").replace("&", "§");
        requestTpaHere = language.getString("Teleport.RequestTPaHere").replace("&", "§");
        requestTpaExpired = language.getString("Teleport.RequestTPaExpired").replace("&", "§");
        tpNoRequest = language.getString("Teleport.NoRequest").replace("&", "§");
        tpRequestAccept = language.getString("Teleport.RequestAccept").replace("&", "§");
        tpRequestRefuse = language.getString("Teleport.RequestRefuse").replace("&", "§");

        itemHand = language.getString("Items.Hand").replace("&", "§");
        noUsable = language.getString("Items.NotUsable").replace("&", "§");
    }


    public Configuration getConfiguration() {
        return configuration;
    }


    public String getNoConsole() {
        return noConsole;
    }

    public String getNoPerm() {
        return noPerm;
    }

    public String getNotFound() {
        return notFound;
    }

    public String getWhatReload() {
        return whatReload;
    }

    public String getMaxCara() {
        return maxCara;
    }

    public String getFailTP() {
        return failTP;
    }

    public String getPlayerOffline() {
        return playerOffline;
    }



    public String getTeleport() {
        return teleport;
    }

    public String getTeleportPlayer() {
        return teleportPlayer;
    }

    public String getTeleportPlayerHere() {
        return teleportPlayerHere;
    }

    public String getRequestTpa() {
        return requestTpa;
    }

    public String getRequestTpaHere() {
        return requestTpaHere;
    }

    public String getRequestTpaExpired() {
        return requestTpaExpired;
    }

    public String getTpNoRequest() {
        return tpNoRequest;
    }

    public String getTpRequestAccept() {
        return tpRequestAccept;
    }

    public String getTpRequestRefuse() {
        return tpRequestRefuse;
    }


    public String getItemHand() {
        return itemHand;
    }

    public String getNoUsable() {
        return noUsable;
    }
}


