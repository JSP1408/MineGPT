package io.papermc.minegpt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

@SuppressWarnings({"unused", "DataFlowIssue"})
public class MineGPT extends JavaPlugin implements Listener {
    public FileConfiguration configuration = getConfig();


    @Override
    public void onEnable() {
        configuration.addDefault("token", "insert_token_here");
        configuration.addDefault("maxTokens", 512);
        configuration.addDefault("timeout", 60);
        configuration.addDefault("model", "gpt-3.5-turbo");
        configuration.addDefault("rules", new ArrayList<>());

        configuration.options().copyDefaults(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        this.saveDefaultConfig();
        this.getCommand("askGPT").setExecutor(new AskGptCommand(this));
    }
}