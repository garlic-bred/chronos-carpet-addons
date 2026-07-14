package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalShape.class)
public class NetherPortalMixin {

    @Inject(method = "findCollisionFreePosition", at = @At("HEAD"), cancellable = true)
    private static void cancelCollisionCheck(Vec3 fallback, ServerLevel world, Entity entity, EntityDimensions dimensions, CallbackInfoReturnable<Vec3> cir) {
        if (ChronosSettings.disableNetherPortalCollisionCheck) {
            cir.setReturnValue(fallback);
        }
    }

}
