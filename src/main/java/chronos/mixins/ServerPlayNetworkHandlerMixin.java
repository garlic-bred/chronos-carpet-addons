package chronos.mixins;

import chronos.ChronosSettings;
import chronos.util.CapturedDrops;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
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
        if (ChronosSettings.chronosCarefulBreak && player.isSneaking()) {
            CapturedDrops.startCapturing();
        }
    }

    @Inject(method = "onPlayerAction", at = @At("TAIL"))
    public void stopCapturingDrops(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (ChronosSettings.chronosCarefulBreak && player.isSneaking()) {
//            String debug = "";
            for (ItemStack stack : CapturedDrops.getDrops()) {
//                debug += "[" + stack.getCount() + "x " + stack.getTranslationKey() + "] ";
                if (!player.getInventory().insertStack(stack)) {
                    player.sendSystemMessage(new LiteralText("Inventory full"), Util.NIL_UUID);
                }
            }
//            player.sendSystemMessage(new LiteralText("captured " + debug), Util.NIL_UUID);
            CapturedDrops.clear();
        }
    }

}
