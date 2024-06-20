package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;canInteractWithBlockAt(Lnet/minecraft/util/math/BlockPos;D)Z"))
    private boolean modifyReachDistance(ServerPlayerEntity player, BlockPos pos, double additionalRange) {
        if (ChronosSettings.maxBlockReachDistance != 6.0) {
            return new Box(pos).squaredMagnitude(player.getEyePos()) < ChronosSettings.maxBlockReachDistance * ChronosSettings.maxBlockReachDistance;
        }
        return player.canInteractWithBlockAt(pos, additionalRange);
    }

}
