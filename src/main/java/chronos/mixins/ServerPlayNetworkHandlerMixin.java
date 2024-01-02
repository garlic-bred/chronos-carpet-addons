package chronos.mixins;

import chronos.ChronosSettings;
import chronos.util.CapturedDrops;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onPlayerAction", at = @At("HEAD"))
    public void startCapturingDrops(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (ChronosSettings.betterCarefulBreak && player.isSneaking()) {
            CapturedDrops.startCapturing(player);
        }
    }

    @Inject(method = "onPlayerAction", at = @At("RETURN"))
    public void stopCapturingDrops(PlayerActionC2SPacket packet, CallbackInfo ci) {
        CapturedDrops.clear();
    }

}
