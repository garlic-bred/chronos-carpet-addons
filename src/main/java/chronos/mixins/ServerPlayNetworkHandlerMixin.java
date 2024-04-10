package chronos.mixins;

import carpet.CarpetSettings;
import chronos.ChronosSettings;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow @Final public static double MAX_BREAK_SQUARED_DISTANCE;

    @Redirect(method = "onPlayerInteractBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
    private double modifyReachDistance() {
        if (CarpetSettings.antiCheatDisabled)
            return ChronosSettings.maxBlockReachDistance * ChronosSettings.maxBlockReachDistance;
        return MAX_BREAK_SQUARED_DISTANCE;
    }

    @ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 64.0))
    private double modifyPlacementDistance(double constant) {
        if (CarpetSettings.antiCheatDisabled)
            return ChronosSettings.maxBlockReachDistance * ChronosSettings.maxBlockReachDistance;
        return constant;
    }

}
