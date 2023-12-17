package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RaidManager.class)
public class RaidManagerMixin {

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "getOrCreateRaid", ordinal = 0)
    private Raid disableNearbyRaidCheck(Raid value)
    {
        return ChronosSettings.ignoreExistingRaids ? null : value;
    }
}
