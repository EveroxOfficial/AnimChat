package com.yourname.animchat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AnimChat extends JavaPlugin {

    private String gradientColors;
    private String requiredPermission;
    private String fontNamespace;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPluginConfig();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AnimChatExpansion(this).register();
            getLogger().info("AnimChat Hooked successfully into PlaceholderAPI!");
        } else {
            getLogger().warning("PlaceholderAPI not found! AnimChat expansion could not register.");
        }

        getLogger().info("AnimChat Expansion (v1.0.0) loaded successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AnimChat Expansion has been unloaded.");
    }

    public void loadPluginConfig() {
        reloadConfig();
        FileConfiguration config = getConfig();
        
        this.gradientColors = config.getString("gradient-colors", "#ff0055:#00ffff:#ff0055");
        this.requiredPermission = config.getString("permission", "animchat.use");
        this.fontNamespace = config.getString("animated-font", "minecraft:uniform");
    }

    public String getGradientColors() { return gradientColors; }
    public String getRequiredPermission() { return requiredPermission; }
    public String getFontNamespace() { return fontNamespace; }
}

class AnimChatExpansion extends PlaceholderExpansion {

    private final AnimChat plugin;

    public AnimChatExpansion(AnimChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() { return "YourName"; }

    @Override
    public @NotNull String getIdentifier() { return "animchat"; }

    @Override
    public @NotNull String getVersion() { return "1.0.0"; }

    @Override
    public boolean persist() { return true; }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        if (params.equalsIgnoreCase("message")) {
            if (!player.hasPermission(plugin.getRequiredPermission())) {
                return null; 
            }

            String gradientTag = "<gradient:" + plugin.getGradientColors() + ">";
            String fontTag = "<font:" + plugin.getFontNamespace() + ">";
            
            return fontTag + gradientTag + "{message}" + "</gradient></font>";
        }

        return null;
    }
}
