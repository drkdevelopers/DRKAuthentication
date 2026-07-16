package com.drkdevelopers.drkloginsecurity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;

public class PlayerJoinListener implements Listener {

    private final DRKLoginSecurity plugin;

    public PlayerJoinListener(DRKLoginSecurity plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.sendMessage(Component.text("§8§m----------------------------------------"));
        player.sendMessage(Component.text("§b§lDRKLoginSecurity"));
        player.sendMessage(Component.text("§7Welcome, §f" + player.getName() + "§7!"));
        player.sendMessage(Component.text("§7Please register or login to continue."));
        player.sendMessage(Component.text("§b/register <password> <password>"));
        player.sendMessage(Component.text("§bor"));
        player.sendMessage(Component.text("§b/login <password>"));
        player.sendMessage(Component.text("§8§m----------------------------------------"));

        player.showTitle(
                Title.title(
                        Component.text("§c§lLOGIN"),
                        Component.text("§fPlease use §b/login §for §b/register"),
                        Title.Times.times(
                                Duration.ofMillis(500),
                                Duration.ofSeconds(4),
                                Duration.ofSeconds(1)
                        )
                )
        );
    }
}