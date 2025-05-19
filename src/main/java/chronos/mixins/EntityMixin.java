package chronos.mixins;

import carpet.CarpetSettings;
import chronos.ChronosSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "queueBlockCollisionCheck", at = @At("HEAD"), cancellable = true)
    private void shithead(Vec3d vec3, Vec3d vec32, CallbackInfo ci) {
        if (ChronosSettings.noClipCollisionFix && (Entity)(Object)this instanceof PlayerEntity playerEntity && (playerEntity.isSpectator() || (CarpetSettings.creativeNoClip && playerEntity.getAbilities().flying)))
            ci.cancel();
    }
}
