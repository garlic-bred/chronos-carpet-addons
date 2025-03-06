package chronos.commands;

import chronos.ChronosSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TotalCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("total")
                .requires((player) -> ChronosSettings.commandTotal)
                .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                        .executes(context -> execute(context.getSource(), ScoreboardObjectiveArgumentType.getObjective(context, "objective")))
                        .then(argument("bots", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), ScoreboardObjectiveArgumentType.getObjective(context, "objective"), BoolArgumentType.getBool(context, "bots")))
                        )
                );

        dispatcher.register(literalArgumentBuilder);
    }

    private static int execute(ServerCommandSource source, ScoreboardObjective objective) {
        return execute(source, objective, ChronosSettings.scoreboardIgnoresBots);
    }

    private static int execute(ServerCommandSource source, ScoreboardObjective objective, boolean bots) {
        source.sendMessage(Text.literal("[" + objective.getDisplayName() + "] Total: " + getTotal(source, objective, bots)));
        return 1;
    }

    public static int getTotal(ServerCommandSource source, ScoreboardObjective objective, boolean bots) {
        int i = 0;
        for (ScoreboardEntry score: source.getServer().getScoreboard().getScoreboardEntries(objective)) {
            if (!bots && source.getServer().getScoreboard().getScoreHolderTeam(score.name().getString()) == null)
                continue;
            i += score.value();
        }
        return i;
    }

}