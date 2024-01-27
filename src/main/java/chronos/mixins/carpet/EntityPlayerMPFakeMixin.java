package chronos.mixins.carpet;

import carpet.patches.EntityPlayerMPFake;
import chronos.ChronosSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMPFake.class)
public class EntityPlayerMPFakeMixin {

    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    public void cancelFallDamage(CallbackInfo ci) {
        if (!ChronosSettings.fakePlayerFallDamage) ci.cancel();
    }

}
