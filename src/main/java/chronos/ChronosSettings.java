package chronos;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.CREATIVE;
import static carpet.settings.RuleCategory.SURVIVAL;

public class ChronosSettings
{
    @Rule(desc="Deepslate can be instant mined with netherite pickaxe", category = {SURVIVAL, "chronos"})
    public static boolean netheritePickaxeInstantMineDeepslate = false;

    @Rule(
            desc = "Enables players without OP to change what objective is being displayed, and query a player's objectives",
            extra = {"Players with OP level 1 can also freeze or unfreeze an objective, which will stop scores for an objective from increasing"},
            category = {SURVIVAL, "chronos"}
    )
    public static boolean commandSidebar = true;

    @Rule(
            desc = "Set a goal for a scoreboard objective to complete.",
            extra = {"This goal is for individual players and is not persistent between logins"},
            category = {SURVIVAL, "chronos"}
    )
    public static boolean commandGoal = true;

    @Rule(
            desc = "Display total score on the sidebar",
            category = {SURVIVAL, "chronos"}
    )
    public static boolean totalScore = false;

    @Rule(
            desc = "Player's bedrock \"mined\" statistic is increased when a bedrock block is broken 2 game ticks after a piston is placed",
            category = {SURVIVAL, "chronos"}
    )
    public static boolean bedrockBrokenStatistics = false;
}
