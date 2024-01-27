package chronos.mixins;

import chronos.ChronosSettings;
import chronos.util.CapturedDrops;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    public void startCapturingDrops(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ChronosSettings.betterCarefulBreak && player.isSneaking()) {
            CapturedDrops.startCapturing(player);
        }
    }

    @Inject(method = "tryBreakBlock", at = @At("RETURN"))
    public void stopCapturingDrops(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CapturedDrops.clear();
    }

}
