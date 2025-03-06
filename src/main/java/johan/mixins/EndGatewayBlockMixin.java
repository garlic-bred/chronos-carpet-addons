package johan.mixins;

import chronos.ChronosSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndGatewayBlock.class)
public abstract class EndGatewayBlockMixin {
    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tryUsePortal(Lnet/minecraft/block/Portal;Lnet/minecraft/util/math/BlockPos;)V"))
    private void resetEntityCooldown(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (!ChronosSettings.endGatewayCooldown) {
            entity.setPortalCooldown(0);
        }
    }
}
