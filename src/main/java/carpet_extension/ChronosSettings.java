package carpet_extension;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.block.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import static carpet.settings.RuleCategory.CREATIVE;
import static carpet.settings.RuleCategory.SURVIVAL;

public class ChronosSettings
{
    @Rule(desc="Deepslate can be instant mined with netherite pickaxe", category = {SURVIVAL, "chronos"})
    public static boolean netheritePickaxeInstantMineDeepslate = false;
}
