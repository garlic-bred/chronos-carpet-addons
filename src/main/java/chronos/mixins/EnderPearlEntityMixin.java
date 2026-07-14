package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrownEnderpearl.class)
public class EnderPearlEntityMixin {

    @Inject(method = "isAllowedToTeleportOwner", at = @At("HEAD"), cancellable = true)
    private static void dontTeleport(Entity entity, Level world, CallbackInfoReturnable<Boolean> cir) {
        if (ChronosSettings.disableEnderPearlTeleportation)
            cir.setReturnValue(false);
    }

}
