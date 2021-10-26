package carpet_extension;

import ca.weblite.objc.Client;
import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.network.CarpetClient;
import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
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
