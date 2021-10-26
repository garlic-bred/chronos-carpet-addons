package carpet_extension;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.SURVIVAL;

public class ChronosSettings
{
    @Rule(desc="Deepslate can be instant mined with netherite pickaxe", category = {SURVIVAL, "chronos"})
    public static boolean netheritePickaxeInstantMineDeepslate = false;
}
