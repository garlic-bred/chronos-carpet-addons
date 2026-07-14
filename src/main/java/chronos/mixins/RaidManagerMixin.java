package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Raids.class)
public class RaidManagerMixin {

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "getOrCreateRaid", ordinal = 0)
    private Raid disableNearbyRaidCheck(Raid value)
    {
        return ChronosSettings.ignoreExistingRaids ? null : value;
    }
}
