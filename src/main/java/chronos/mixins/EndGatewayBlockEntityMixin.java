package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {

    @Inject(method="needsCooldownBeforeTeleporting", at=@At("RETURN"), cancellable = true)
    private void needsCooldownBeforeTeleporting(CallbackInfoReturnable<Boolean> cir) {
        if (!ChronosSettings.endGatewayCooldown)
            cir.setReturnValue(false);// Returns target method early
    }
}
