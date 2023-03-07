package xyz.localhost.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.localhost.bungee.commands.Helpop;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class Main extends Plugin{
    public static Configuration config;
    public static Plugin plugin;
//    public static  plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.getLogger().log(Level.INFO, ChatColor.YELLOW + "\n-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n" + ChatColor.AQUA + "        [Webhook Helpop for Bungee]\n" + ChatColor.GREEN + "           Enabled!\n" + ChatColor.DARK_AQUA + "    Server: " + ChatColor.BLUE + getProxy().getVersion() + "\n" + ChatColor.DARK_AQUA + "    Author: " + ChatColor.BLUE + "Antonq" + " v1.0 " + ChatColor.YELLOW + "\n-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Helpop());
        config();
    }
    public static void config() {
        try {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            plugin.getLogger().log(Level.WARNING, String.valueOf(configFile.exists()));
            // Copy default config if it doesn't exist
            if (!configFile.exists()) {
                if (!plugin.getDataFolder().exists()) {
                    File configF = Files.createDirectory(Paths.get(plugin.getDataFolder().getPath())).toFile();
                }
                plugin.getLogger().log(Level.WARNING, String.valueOf(plugin.getDataFolder().exists()));
                FileOutputStream outputStream = new FileOutputStream(configFile); // Throws IOException
                InputStream in = plugin.getResourceAsStream("config.yml"); // This file must exist in the jar resources folder
                in.transferTo(outputStream); // Throws IOException
//                File configF = Files.createFile(Paths.get(getDataFolder().toPath() +  "/config.yml")).toFile();
//                BufferedWriter writer = new BufferedWriter(new FileWriter(configF));
//                writer.write("# Configuration for webhooks");
//                writer.write("webhook_url: \"https://canary.discord.com/api/webhooks/1234567891234567809/exampleEXAMPLE3x4mp13_exampleEXAMPLE3x4mp13_exampleEXAMPLE3x4mp13_exampleEXAMPLE3x4mp13\"");
//                writer.close();
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Config is not accessable\n" + e);
        }
    }
}
