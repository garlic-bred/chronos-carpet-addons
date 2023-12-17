package chronos;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import johan.commands.TotalCommand;
import litetech.commands.GoalCommand;
import litetech.commands.SideBarCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ChronosExtension implements CarpetExtension, ModInitializer {
    public static final int SETTING = 69420;
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
        SideBarCommand.register(dispatcher);
        GoalCommand.register(dispatcher);
        TotalCommand.register(dispatcher);
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
