package litetech.commands;

import carpet.utils.CommandHelper;
import chronos.ChronosSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import litetech.helpers.ScoreboardObjectiveHelper;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.command.argument.ScoreboardSlotArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SideBarCommand {
    private static final SimpleCommandExceptionType OBJECTIVES_DISPLAY_ALREADY_SET_EXCEPTION = new SimpleCommandExceptionType(
            Text.translatable("commands.scoreboard.objectives.display.alreadySet"));
    private static final Dynamic2CommandExceptionType PLAYERS_GET_NULL_EXCEPTION = new Dynamic2CommandExceptionType(
            (object, object2) -> Text.translatable("commands.scoreboard.players.get.null", object, object2));


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("sidebar")
                        .requires(player -> CommandHelper.canUseCommand(player, ChronosSettings.commandSidebar))
                        .then(CommandManager.literal("show")
                                .then(CommandManager.argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                        .executes(context -> executeSetDisplay(
                                                context.getSource(),
                                                ScoreboardObjectiveArgumentType.getObjective(context, "objective"),
                                                null
                                        ))
                                        .then(CommandManager.argument("slot", ScoreboardSlotArgumentType.scoreboardSlot())
                                                .executes(context -> executeSetDisplay(
                                                        context.getSource(),
                                                        ScoreboardObjectiveArgumentType.getObjective(context, "objective"),
                                                        ScoreboardSlotArgumentType.getScoreboardSlot(context, "slot")
                                                )))))
                        .then(CommandManager.literal("query")
                                .then(CommandManager.argument("player", EntityArgumentType.players())
                                        .then(CommandManager.argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                                .executes(context -> executeGet(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayer(context, "player"),
                                                        ScoreboardObjectiveArgumentType.getObjective(context, "objective")
                                                )))))
                        .then(CommandManager.literal("clear")
                                .executes(context -> executeClearDisplay(context.getSource())))
                        .then(CommandManager.literal("freeze")
                                .requires(source -> source.hasPermissionLevel(1))
                                .then(CommandManager.argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                        .executes(context -> {
                                            ScoreboardObjective objective = ScoreboardObjectiveArgumentType.getObjective(
                                                    context, "objective");

                                            ((ScoreboardObjectiveHelper) objective).setFrozen(true);
                                            context.getSource().sendMessage(Text.literal(
                                                    "Froze scores for objective: " + objective.getName()));
                                            return 0;
                                        })))
                        .then(CommandManager.literal("unfreeze")
                                .requires(source -> source.hasPermissionLevel(1))
                                .then(CommandManager.argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                        .executes(context -> {
                                            ScoreboardObjective objective = ScoreboardObjectiveArgumentType.getObjective(
                                                    context, "objective");

                                            ((ScoreboardObjectiveHelper) objective).setFrozen(false);
                                            context.getSource().sendMessage(Text.literal(
                                                    "Unfroze scores for objective: " + objective.getName()));
                                            return 0;
                                        })))

        );
    }

    private static int executeClearDisplay(ServerCommandSource source) throws CommandSyntaxException {
        Scoreboard scoreboard = source.getServer().getScoreboard();
        if (scoreboard.getObjectiveForSlot(null) == null) {
            throw OBJECTIVES_DISPLAY_ALREADY_SET_EXCEPTION.create();
        } else {
            scoreboard.setObjectiveSlot(null, (ScoreboardObjective) null);
            source.sendMessage(Text.translatable("commands.scoreboard.objectives.display.cleared"));
            return 0;
        }
    }

    private static int executeSetDisplay(ServerCommandSource source, ScoreboardObjective objective, ScoreboardDisplaySlot slot) throws CommandSyntaxException {
        Scoreboard scoreboard = source.getServer().getScoreboard();
        if (scoreboard.getObjectiveForSlot(slot) == objective) {
            throw OBJECTIVES_DISPLAY_ALREADY_SET_EXCEPTION.create();
        } else {
            scoreboard.setObjectiveSlot(slot, objective);
            source.sendMessage(Text.translatable("commands.scoreboard.objectives.display.set", objective.getDisplayName(), objective.getDisplayName()));
            return 0;
        }
    }

    private static int executeGet(ServerCommandSource source, PlayerEntity player, ScoreboardObjective objective) throws CommandSyntaxException {
        Scoreboard scoreboard = source.getServer().getScoreboard();
        ScoreHolder holder = ScoreHolder.fromName(player.getDisplayName().getString());
        if (scoreboard.getScore(holder, objective) == null)
            throw PLAYERS_GET_NULL_EXCEPTION.create(objective.getName(), player.getDisplayName().getString());
        else {
            int score = scoreboard.getScore(holder, objective).getScore();
            source.sendMessage(Text.translatable("commands.scoreboard.players.get.success", player.getDisplayName().getString(), score, objective.toHoverableText()));
            return score;
        }
    }
}
