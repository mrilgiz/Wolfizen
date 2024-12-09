package net.mrwogiz.wolfizen.bukkit;

import com.denizenscript.denizencore.DenizenCore;

import net.mrwogiz.wolfizen.bukkit.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Wolfizen extends JavaPlugin {

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        // Регистрация команд
        DenizenCore.commandRegistry.registerCommand(ZipCommand.class);
        DenizenCore.commandRegistry.registerCommand(UnzipCommand.class);
        Bukkit.getScheduler().runTaskAsynchronously(this,() -> {
            DenizenCore.commandRegistry.registerCommand(MineskinCommand.class);
        });
        plugin = this;
        getLogger().info("========================================");
        getLogger().info("  Welcome to the Wolfizen Plugin!     ");
        getLogger().info("  Zip and Unzip commands are now       ");
        getLogger().info("  available for your server.           ");
        getLogger().info("========================================");
    }

    public static JavaPlugin getPluginI() {
        return plugin;
    }

    @Override
    public void onDisable() {
        // Очистка, если необходимо
        getLogger().info("========================================");
        getLogger().info("  Wolfizen Plugin is now disabled.     ");
        getLogger().info("  Thank you for using our plugin!      ");
        getLogger().info("========================================");
    }
}
