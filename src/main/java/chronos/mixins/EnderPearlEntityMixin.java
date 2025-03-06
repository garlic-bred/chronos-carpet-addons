package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin {

    @Inject(method = "canTeleportEntityTo", at = @At("HEAD"), cancellable = true)
    private static void dontTeleport(Entity entity, World world, CallbackInfoReturnable<Boolean> cir) {
        if (ChronosSettings.disableEnderPearlTeleportation)
            cir.setReturnValue(false);
    }

}
