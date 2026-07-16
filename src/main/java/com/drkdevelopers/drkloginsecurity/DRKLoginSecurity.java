package com.drkdevelopers.drkloginsecurity;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DRKLoginSecurity extends JavaPlugin implements Listener {

    private Connection connection;
    private final Set<UUID> loggedInPlayers = new HashSet<>();

    private String color(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

    @Override
    public void onEnable() {
        try {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(dataFolder, "database.db").getAbsolutePath());
            try (Statement s = connection.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS users (uuid TEXT PRIMARY KEY, username TEXT, password TEXT);");
            }
        } catch (SQLException e) { getLogger().severe("DB Error: " + e.getMessage()); }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        loggedInPlayers.remove(p.getUniqueId());

        try (PreparedStatement st = connection.prepareStatement("SELECT password FROM users WHERE uuid = ?;")) {
            st.setString(1, p.getUniqueId().toString());
            if (st.executeQuery().next()) {
                // Returning Player
                p.sendMessage(color("&8&m----------------------------------------\n&b&lDRKLoginSecurity\n&7Welcome back, &f" + p.getName() + "&7!\n&7Please use &b/login <password> &7to continue.\n&8&m----------------------------------------"));
                p.sendTitle(color("&c&lLOGIN"), color("&fPlease use &b/login <password>"), 10, 70, 20);
            } else {
                // First-time Player
                p.sendMessage(color("&8&m----------------------------------------\n&b&lDRKLoginSecurity\n&7Welcome, &f" + p.getName() + "&7!\n&7Please register using\n&b/register <password> <password>\n&8&m----------------------------------------"));
                p.sendTitle(color("&a&lREGISTER"), color("&fUse &b/register <password> <password>"), 10, 70, 20);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) { loggedInPlayers.remove(e.getPlayer().getUniqueId()); }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!loggedInPlayers.contains(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!loggedInPlayers.contains(e.getPlayer().getUniqueId())) {
            e.getPlayer().sendMessage(color("&cPlease log in to continue!"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!loggedInPlayers.contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("drkauth")) {
            if (!sender.isOp()) return true;
            if (args.length >= 2 && args[0].equalsIgnoreCase("reset")) {
                try (PreparedStatement st = connection.prepareStatement("DELETE FROM users WHERE username = ?;")) {
                    st.setString(1, args[1]);
                    sender.sendMessage(st.executeUpdate() > 0 ? "Reset " + args[1] : "Not found.");
                } catch (SQLException e) { sender.sendMessage("Error."); }
            }
            return true;
        }

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (command.getName().equalsIgnoreCase("register")) {
            if (args.length < 2 || !args[0].equals(args[1])) { p.sendMessage(color("&cPasswords do not match!")); return true; }
            try (PreparedStatement st = connection.prepareStatement("INSERT INTO users (uuid, username, password) VALUES (?, ?, ?);")) {
                st.setString(1, p.getUniqueId().toString());
                st.setString(2, p.getName());
                st.setString(3, args[0]);
                st.executeUpdate();
                p.sendTitle(color("&a&lREGISTERED"), color("&fYour account has been created"), 10, 70, 20);
                loggedInPlayers.add(p.getUniqueId());
            } catch (SQLException e) { p.sendMessage(color("&cAlready registered!")); }
        } else if (command.getName().equalsIgnoreCase("login")) {
            try (PreparedStatement st = connection.prepareStatement("SELECT password FROM users WHERE uuid = ?;")) {
                st.setString(1, p.getUniqueId().toString());
                ResultSet rs = st.executeQuery();
                if (rs.next() && rs.getString("password").equals(args[0])) {
                    p.sendTitle(color("&a&lSUCCESS"), color("&fYou have successfully logged in."), 10, 70, 20);
                    loggedInPlayers.add(p.getUniqueId());
                } else { p.sendMessage(color("&cIncorrect password!")); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return true;
    }
}