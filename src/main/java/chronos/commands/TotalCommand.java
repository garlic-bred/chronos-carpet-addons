package chronos.commands;

import chronos.ChronosSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class TotalCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = literal("total")
                .requires((player) -> ChronosSettings.commandTotal)
                .then(argument("objective", ObjectiveArgument.objective())
                        .executes(context -> execute(context.getSource(), ObjectiveArgument.getObjective(context, "objective")))
                        .then(argument("bots", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), ObjectiveArgument.getObjective(context, "objective"), BoolArgumentType.getBool(context, "bots")))
                        )
                );

        dispatcher.register(literalArgumentBuilder);
    }

    private static int execute(CommandSourceStack source, Objective objective) {
        return execute(source, objective, ChronosSettings.scoreboardIgnoresBots);
    }

    private static int execute(CommandSourceStack source, Objective objective, boolean bots) {
        source.sendSystemMessage(Component.literal("[" + objective.getDisplayName() + "] Total: " + getTotal(source, objective, bots)));
        return 1;
    }

    public static int getTotal(CommandSourceStack source, Objective objective, boolean bots) {
        int i = 0;
        for (PlayerScoreEntry score: source.getServer().getScoreboard().listPlayerScores(objective)) {
            if (!bots && source.getServer().getScoreboard().getPlayersTeam(score.ownerName().getString()) == null)
                continue;
            i += score.value();
        }
        return i;
    }

}