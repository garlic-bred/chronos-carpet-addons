package carpet_extension;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.CarpetSettings;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.logging.Logger;

public class ChronosExtension implements CarpetExtension, ModInitializer {
    public static void noop() { }
    static
    {
        CarpetServer.manageExtension(new ChronosExtension());
    }

    @Override
    public void onGameStarted()
    {
        System.out.println("Initializing Chronos Carpet Extension");
        CarpetServer.settingsManager.parseSettingsClass(ChronosSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
    }

    @Override
    public void onTick(MinecraftServer server)
    {
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher)
    {
    }

    @Override
    public SettingsManager customSettingsManager()
    {
        return null;
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player)
    {
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
    }

    @Override
    public void onInitialize() {
    }
}
