package chronos;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import johan.commands.TotalCommand;
import litetech.commands.SideBarCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandBuildContext)
    {
        SideBarCommand.register(dispatcher);
        TotalCommand.register(dispatcher);
    }

    @Override
    public SettingsManager extensionSettingsManager()
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

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        InputStream langFile = ChronosExtension.class.getClassLoader().getResourceAsStream("assets/chronos-carpet-addons/lang/%s.json".formatted(lang));
        if (langFile == null) {
            return Map.of();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Map.of();
        }
        return new Gson().fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
    }
}
