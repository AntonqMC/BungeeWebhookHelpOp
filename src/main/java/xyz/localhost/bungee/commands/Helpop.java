package xyz.localhost.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.protocol.packet.Chat;
import xyz.localhost.bungee.Main;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static xyz.localhost.bungee.Main.config;
import static xyz.localhost.bungee.Main.plugin;

public class Helpop extends Command {
    public Helpop() {
        super("helpop", "antonq.helpop");
    }
    public boolean bungeeCast (String msg){
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if(player.hasPermission("antonq.helpop.read")) {
                player.sendMessage(new TextComponent(ChatColor.GRAY + msg));
            }
        }
        return true;
    }
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Main.config();
        if (commandSender instanceof ProxiedPlayer player) {
            if (args.length > 0) {

                StringBuilder message = new StringBuilder();
                for (String arg : args) {
                    message.append(arg + " ");
                }

                String json = "{ \"embeds\": [ { \"author\":{ \"name\": \"" + player.getName() +"\"}, \"description\":\"" + message + "\", \"color\":112342, \"thumbnail\": { \"url\": \"https://minotar.net/helm/" + player.getName() + "/512.png\" }, \"fields\": [ { \"name\":\"Serwer\", \"value\":\"" + player.getServer().getInfo().getName() + "\", \"inline\":false } ] } ] }";

                message = new StringBuilder(message.toString().replaceAll("&[0-9]", ""));
                message = new StringBuilder(message.toString().replaceAll("&[A-F]", ""));
                message = new StringBuilder(message.toString().replaceAll("&[a-f]", ""));
                message = new StringBuilder(message.toString().replaceAll("<@[0-9]+>", ""));
                message = new StringBuilder(message.toString().replaceAll("<!@[0-9]+>", ""));

                if (message.toString().trim().equalsIgnoreCase("")) {
                    return;
                }
                try {
                    URL url = new URL(Main.config.getString("webhook_url"));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode >= 400) {
                        plugin.getLogger().log(Level.WARNING, "HelpOp msg sending failed!");
                        plugin.getLogger().log(Level.WARNING, json);
                        plugin.getLogger().log(Level.WARNING, connection.getResponseMessage());
                        commandSender.sendMessage(ChatColor.RED + "Coś poszło nie tak...");
                        return;
                    }
                    commandSender.sendMessage(ChatColor.GREEN + "Wysłano wiadomość do administracji!");
                    commandSender.sendMessage( ChatColor.translateAlternateColorCodes('&', "&c&lHELPOP&8> &7" + message));
                    bungeeCast(ChatColor.translateAlternateColorCodes('&', config.getString("syntax").replaceAll("%player%", player.getName()).replaceAll("%server%", player.getServer().getInfo().getName()).replaceAll("%msg%", message.toString())));
                } catch (IOException e) {
                    plugin.getLogger().warning("Failed to POST json to URL: " + e.getMessage());
                }
            } else {
                commandSender.sendMessage(ChatColor.YELLOW + "Poprawne użycie: " + ChatColor.GOLD + "/helpop <wiadomość>");
            }
        }
    }
//    @Override
//    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
//        if (args.length == 1) {
//            List<String> servers = new ArrayList<>();
//            ProxyServer.getInstance().getServers().forEach((s, info) -> {
//                servers.add(info.getName());
//            });
//            return servers;
//        }
//        List<String> none = new ArrayList<>();
//        return none;
//    }
}
