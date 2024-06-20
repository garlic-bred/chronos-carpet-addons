package chronos.mixins;

import carpet.CarpetSettings;
import chronos.ChronosSettings;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Redirect(method = "onPlayerInteractBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;canInteractWithBlockAt(Lnet/minecraft/util/math/BlockPos;D)Z"))
    private boolean modifyReachDistance(ServerPlayerEntity player, BlockPos pos, double additionalRange) {
        if (CarpetSettings.antiCheatDisabled)
            return new Box(pos).squaredMagnitude(player.getEyePos()) < ChronosSettings.maxBlockReachDistance * ChronosSettings.maxBlockReachDistance;

        return player.canInteractWithBlockAt(pos, additionalRange);
    }
}
