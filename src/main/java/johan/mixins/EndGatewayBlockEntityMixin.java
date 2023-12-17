package johan.mixins;

import chronos.ChronosSettings;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
    @Redirect(method="tryTeleportingEntity", at=@At(value="INVOKE", target = "Lnet/minecraft/entity/Entity;resetNetherPortalCooldown()V"))
    private static void resetNetherPortalCooldown(Entity entity) {
        if (ChronosSettings.endGatewayCooldown)
            entity.resetNetherPortalCooldown();
    }

    @Inject(method="needsCooldownBeforeTeleporting", at=@At("RETURN"), cancellable = true)
    private void needsCooldownBeforeTeleporting(CallbackInfoReturnable<Boolean> cir) {
        if (!ChronosSettings.endGatewayCooldown)
            cir.setReturnValue(false);// Returns target method early
    }
}
