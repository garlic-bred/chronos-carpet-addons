package litetech.mixin.server.op_level;

import chronos.ChronosSettings;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import litetech.helpers.PlayerManagerOPWithLevelHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.OpCommand;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(OpCommand.class)
public class OpCommandMixin {
    private static final SimpleCommandExceptionType ALREADY_OPPED_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("commands.op.failed"));

    private static int op(ServerCommandSource source, Collection<GameProfile> targets, int level) throws CommandSyntaxException {
        PlayerManager playerManager = source.getServer().getPlayerManager();
        int i = 0;

        for (GameProfile gameProfile : targets) {
            if (!playerManager.isOperator(gameProfile)) {
                ((PlayerManagerOPWithLevelHelper) playerManager).addToOperators(gameProfile, level, true);

                ++i;
                source.sendFeedback(new LiteralText(String.format("Made %s a level %d server operator", gameProfile.getName(), level)), true);
            }
        }

        if (i == 0) {
            throw ALREADY_OPPED_EXCEPTION.create();
        } else {
            return i;
        }
    }

    @Inject(
            method = "register",
            cancellable = true,
            at = @At("HEAD")
    )
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        if (!ChronosSettings.betterOPCommand) return;

        ci.cancel();

        dispatcher.register(CommandManager.literal("op")
                .requires((s) -> s.hasPermissionLevel(3))
                .then(CommandManager.argument("targets", GameProfileArgumentType.gameProfile())
                        .suggests((context, suggestionsBuilder) -> {
                            PlayerManager manager = context.getSource().getServer().getPlayerManager();

                            return CommandSource.suggestMatching(
                                    manager.getPlayerList().stream()
                                            .filter((player) -> !manager.isOperator(player.getGameProfile()))
                                            .map((player) -> player.getGameProfile().getName()),
                                    suggestionsBuilder
                            );
                        })
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1, 4))
                                .executes(
                                        (context) -> op(
                                                context.getSource(),
                                                GameProfileArgumentType.getProfileArgument(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "level")
                                        )
                                )
                        )
                        .executes(
                                (context) -> op(
                                        context.getSource(),
                                        GameProfileArgumentType.getProfileArgument(context, "targets"),
                                        context.getSource().getServer().getOpPermissionLevel()
                                )
                        )
                ));

    }
}
