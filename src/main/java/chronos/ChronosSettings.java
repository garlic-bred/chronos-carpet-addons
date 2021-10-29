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

    //Litetech stuff

    public static final String LITETECH = "LiteTech";

    @Rule(
            desc = "Enables players without OP to change what objective is being displayed, and query a player's objectives",
            extra = {"Players with OP level 1 can also freeze or unfreeze an objective, which will stop scores for an objective from increasing"},
            category = {LITETECH, SURVIVAL}
    )
    public static boolean sidebarCommand = true;

    @Rule(
            desc = "Set a goal for a scoreboard objective to complete.",
            extra = {"This goal is for individual players and is not persistent between logins"},
            category = {LITETECH, SURVIVAL}
    )
    public static boolean goalCommand = true;

    @Rule(
            desc = "Allows you to set OP level when OPing players.",
            category = {LITETECH, SURVIVAL, CREATIVE}
    )
    public static boolean betterOPCommand = true;

    @Rule(
            desc = "Permission Level required for the seed command",
            validate = ChronosSettings.ValidatorPermissionLevel.class,
            options = {"0", "1", "2", "3", "4"},
            category = {LITETECH, SURVIVAL}
    )
    public static int seedPermissionLevel = 2;

    public static class ValidatorPermissionLevel extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string) {
            return (newValue >= 0 && newValue <= 4) ? newValue : null;
        }

        @Override
        public String description() {
            return "Choose a value between 0 to 4";
        }
    }

    @Rule(
            desc = "The OP level used by default when OP-ing a player",
            extra = "-1 will use the default",
            validate = ChronosSettings.ValidatorOpLevel.class,
            options = {"-1", "1", "2", "3", "4"},
            category = {LITETECH, CREATIVE}
    )
    public static int defaultOpLevel = -1;

    public static class ValidatorOpLevel extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, ParsedRule<Integer> rule, Integer newValue, String string) {
            return (newValue == -1) || (newValue > 0 && newValue <= 4) ? newValue : null;
        }

        @Override
        public String description() { return "Choose a value between 1 to 4 (or -1)"; }
    }

    @Rule(
            desc = "Display total score on the sidebar",
            category = {LITETECH, SURVIVAL}
    )
    public static boolean totalScore = false;

    @Rule(
            desc = "Player's bedrock \"mined\" statistic is increased when a bedrock block is broken 2 game ticks after a piston is placed",
            category = {LITETECH, SURVIVAL}
    )
    public static boolean bedrockBrokenStatistics = false;
}
