package chronos;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.*;

public class ChronosSettings
{
    private static final String CHRONOS = "chronos";

    @Rule(
            desc = "Set a goal for a scoreboard objective to complete.",
            extra = {"This goal is for individual players and is not persistent between logins", "Taken from litetech carpet addons"},
            category = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean commandGoal = false;

    @Rule(
            desc = "Enables players without OP to change what objective is being displayed, and query a player's objectives",
            extra = {"Players with OP level 1 can also freeze or unfreeze an objective, which will stop scores for an objective from increasing", "Taken from litetech carpet addons"},
            category = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean commandSidebar = false;

    @Rule(
            desc = "Enables /total command to know the total sum of a scoreboard.",
            extra = {"Taken from johan's carpet addons"},
            category = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean commandTotal = false;

    @Rule(
            desc = "Allows players in Creative mode to kill entities in one hit",
            extra = {"If the player is sneaking, other entities around the target get killed too", "Taken from lunaar carpet addons"},
            category = { CREATIVE, CHRONOS }
    )
    public static boolean creativeOneHitKill = false;

    @Rule(
            desc = "Allows raids to start within 96 blocks of existing raids.",
            extra = {"May be useful for designing raid farms"},
            category = { CREATIVE, EXPERIMENTAL, CHRONOS}
    )
    public static boolean ignoreExistingRaids = false;

    @Rule(
            desc = "Toggle for end gateway cooldown.",
            extra = {"Taken from johan's carpet addons"},
            category = { SURVIVAL, CHRONOS }
    )
    public static boolean endGatewayCooldown = true;

    @Rule(
            desc="Deepslate can be instant mined with netherite pickaxe and haste II",
            category = { SURVIVAL, CHRONOS }
    )
    public static boolean netheritePickaxeInstantMineDeepslate = false;

    @Rule(
            desc = "Backports 1.12 flint and steel behavior. Flint and steel can be used for updating observers / buds.",
            extra = {"Taken from johan's carpet addons"},
            category = { CREATIVE, CHRONOS }
    )
    public static boolean oldFlintAndSteelBehavior = false;

    @Rule(
            desc = "Display total score on the sidebar",
            extra = {"Taken from johan's carpet addons"},
            category = { SURVIVAL, COMMAND, CHRONOS }
    )
    public static boolean totalScore = false;

    @Rule(
            desc = "Bots don't appear on scoreboards and do not count in the total if they're not in a team",
            extra = {"Real players need to be in a team!", "Taken from lunaar carpet addons", "Based on code by JohanVonElectrum"},
            category = { SURVIVAL, CHRONOS }
    )
    public static boolean scoreboardIgnoresBots = false;

}
