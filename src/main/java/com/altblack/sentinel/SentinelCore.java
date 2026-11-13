package com.altblack.sentinel;

import com.altblack.sentinel.listeners.ExploitListener;
import com.altblack.sentinel.utils.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class SentinelCore extends JavaPlugin {

    private DiscordWebhook discordWebhook;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String webhookUrl = getConfig().getString("discord.webhook-url", "");
        String botName = getConfig().getString("discord.bot-name", "SentinelCore Watchdog");
        this.discordWebhook = new DiscordWebhook(webhookUrl, botName, getLogger());

        // Register Crash Watchdog Uncaught Exception Handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            getLogger().severe("Critical uncaught exception detected in thread " + thread.getName() + ": " + throwable.getMessage());
            sendCrashReportToDiscord("Uncaught Exception in Thread: " + thread.getName(), throwable);
        });

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ExploitListener(this), this);

        getLogger().info("SentinelCore v" + getDescription().getVersion() + " by altblack has been successfully enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SentinelCore has been disabled safely.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sentinelcore")) {
            if (!sender.hasPermission("sentinelcore.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                String webhookUrl = getConfig().getString("discord.webhook-url", "");
                String botName = getConfig().getString("discord.bot-name", "SentinelCore Watchdog");
                this.discordWebhook = new DiscordWebhook(webhookUrl, botName, getLogger());

                sender.sendMessage(ChatColor.GREEN + "[SentinelCore] Configuration reloaded successfully.");
                return true;
            }

            sender.sendMessage(ChatColor.GOLD + "=== SentinelCore Security ===");
            sender.sendMessage(ChatColor.YELLOW + "Author: altblack");
            sender.sendMessage(ChatColor.YELLOW + "Status: Active & Protecting");
            sender.sendMessage(ChatColor.GRAY + "Use `/sentinelcore reload` to reload configuration.");
            return true;
        }
        return false;
    }

    public void sendCrashReportToDiscord(String title, Throwable throwable) {
        if (discordWebhook == null) return;
        
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        String serverVersion = Bukkit.getVersion() + " (Paper API 1.21.11)";
        discordWebhook.sendCrashAlert(title, sb.toString(), serverVersion);
    }
}