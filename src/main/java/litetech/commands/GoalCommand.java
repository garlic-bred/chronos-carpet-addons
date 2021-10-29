package litetech.commands;

import carpet.settings.SettingsManager;
import chronos.ChronosSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GoalCommand {
    public static final HashMap<ServerPlayerEntity, HashMap<ScoreboardObjective, Goal>> GOALS = new HashMap<>();
    public static final HashMap<ServerPlayerEntity, Goal> CURRENT_GOALS = new HashMap<>();

    private static final SimpleCommandExceptionType NO_GOALS_SET_FOR_PLAYER = new SimpleCommandExceptionType(
            new LiteralText("No goals have been set for this player!"));
    private static final SimpleCommandExceptionType GOAL_NOT_SET_FOR_OBJECTIVE = new SimpleCommandExceptionType(
            new LiteralText("No goals have been set for this objective!"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("goal")
                        .requires(player -> SettingsManager.canUseCommand(player, ChronosSettings.goalCommand))
                        .then(literal("set")
                                .then(argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                                        .executes(context -> displayGoal(
                                                context.getSource(),
                                                ScoreboardObjectiveArgumentType.getObjective(context, "objective")
                                        ))
                                        .then(argument("goal", IntegerArgumentType.integer())
                                                .executes(context -> setGoal(
                                                        context.getSource(),
                                                        ScoreboardObjectiveArgumentType.getObjective(context, "objective"),
                                                        IntegerArgumentType.getInteger(context, "goal")
                                                )))))
                        .then(literal("clear")
                                .executes(context -> {
                                    CURRENT_GOALS.remove(context.getSource().getPlayer());
                                    context.getSource().getPlayer().networkHandler.sendPacket(new ClearTitleS2CPacket(true));
                                    return 0;
                                }))
        );
    }

    private static int displayGoal(ServerCommandSource source, ScoreboardObjective objective) throws CommandSyntaxException {
        if (!GOALS.containsKey(source.getPlayer())) throw NO_GOALS_SET_FOR_PLAYER.create();
        HashMap<ScoreboardObjective, Goal> goals = GOALS.get(source.getPlayer());
        if (!goals.containsKey(objective)) throw GOAL_NOT_SET_FOR_OBJECTIVE.create();
        Goal playerGoal = goals.get(objective);
        CURRENT_GOALS.put(source.getPlayer(), playerGoal);

        setDisplay(source.getPlayer(), playerGoal);
        source.sendFeedback(new LiteralText(String.format("Displaying goal for %s",
                objective.getDisplayName().getString())), false);
        return 0;
    }

    private static int setGoal(ServerCommandSource source, ScoreboardObjective objective, int goal) throws CommandSyntaxException {
        if (!GOALS.containsKey(source.getPlayer())) GOALS.put(source.getPlayer(), new HashMap<>());
        HashMap<ScoreboardObjective, Goal> goals = GOALS.get(source.getPlayer());
        if (!goals.containsKey(objective)) goals.put(objective, new Goal(objective));
        Goal playerGoal = goals.get(objective);
        CURRENT_GOALS.put(source.getPlayer(), playerGoal);

        playerGoal.setGoal(goal);
        setDisplay(source.getPlayer(), playerGoal);

        source.sendFeedback(new LiteralText(String.format("Set goal to %s for objective: %s", goal,
                objective.getDisplayName().getString())), false);
        return 0;
    }


    public static void tick() {
        for (ServerPlayerEntity player : GOALS.keySet()) {
            Goal playerGoal = CURRENT_GOALS.get(player);
            if (playerGoal == null) continue;

            setDisplay(player, playerGoal);
        }
    }

    private static void setDisplay(ServerPlayerEntity player, Goal playerGoal) {
        player.networkHandler.sendPacket(new TitleS2CPacket(
                new LiteralText(playerGoal.getScore() + "/" + playerGoal.getGoal()).styled(style -> style
                        .withColor(Formatting.GOLD)).styled(style -> style.withBold(true))
        ));
    }

    public static class Goal {
        private final ScoreboardObjective objective;
        private int goal = 0;
        private int score = 0;

        Goal(ScoreboardObjective objective) {
            this.objective = objective;
        }

        public void incrementScore() {
            this.score += 1;
        }

        public void incrementScore(int amount) {
            this.score += amount;
        }

        public int getGoal() {
            return goal;
        }

        public void setGoal(int goal) {
            this.goal = goal;
        }

        public int getScore() {
            return score;
        }
    }
}
