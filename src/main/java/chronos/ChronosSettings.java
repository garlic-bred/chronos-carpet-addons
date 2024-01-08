package chronos;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.*;

public class ChronosSettings
{
    private static final String CHRONOS = "chronos";

//    @Rule(
////            desc = "If you break a block while sneaking then the block gets put in your inventory.",
////            extra = {"Any blocks supported by that block that get broken instantly will also be put in your inventory."},
//            categories = { SURVIVAL, EXPERIMENTAL, CHRONOS }
//    )
//    public static boolean betterCarefulBreak = false;

    @Rule(
//            desc = "Enables players without OP to change what objective is being displayed, and query a player's objectives",
//            extra = {"Players with OP level 1 can also freeze or unfreeze an objective, which will stop scores for an objective from increasing", "Taken from litetech carpet addons"},
            categories = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean commandSidebar = false;

    @Rule(
//            desc = "Enables /total command to know the total sum of a scoreboard.",
//            extra = {"Taken from johan's carpet addons"},
            categories = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean commandTotal = false;

    @Rule(
//            desc = "Allows players in Creative mode to kill entities in one hit",
//            extra = {"If the player is sneaking, other entities around the target get killed too", "Taken from lunaar carpet addons"},
            categories = { CREATIVE, CHRONOS }
    )
    public static boolean creativeOneHitKill = false;

    @Rule(
//            desc = "Allows raids to start within 96 blocks of existing raids.",
//            extra = {"May be useful for designing raid farms"},
            categories = { CREATIVE, EXPERIMENTAL, CHRONOS}
    )
    public static boolean ignoreExistingRaids = false;

    @Rule(
//            desc = "Toggle for end gateway cooldown.",
//            extra = {"Taken from johan's carpet addons"},
            categories = { SURVIVAL, CHRONOS }
    )
    public static boolean endGatewayCooldown = true;

    @Rule(
//            desc="Deepslate can be instant mined with netherite pickaxe and haste II",
            categories = { SURVIVAL, CHRONOS }
    )
    public static boolean netheritePickaxeInstantMineDeepslate = false;

    @Rule(
//            desc = "Backports 1.12 flint and steel behavior. Flint and steel can be used for updating observers / buds.",
//            extra = {"Taken from johan's carpet addons"},
            categories = { CREATIVE, CHRONOS }
    )
    public static boolean oldFlintAndSteelBehavior = false;

    @Rule(
//            desc = "Display total score on the sidebar",
//            extra = {"Taken from johan's carpet addons"},
            categories = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean totalScore = false;

    @Rule(
//            desc = "Bots don't appear on scoreboards and do not count in the total if they're not in a team",
//            extra = {"Real players need to be in a team!", "Taken from lunaar carpet addons", "Based on code by JohanVonElectrum"},
            categories = { SURVIVAL, CHRONOS }
    )
    public static boolean scoreboardIgnoresBots = false;

}
