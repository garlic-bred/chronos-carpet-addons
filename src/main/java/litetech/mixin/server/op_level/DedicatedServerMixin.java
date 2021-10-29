package litetech.mixin.server.op_level;

import chronos.ChronosSettings;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class DedicatedServerMixin {
    @Inject(
            method = "getOpPermissionLevel",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getOpPermissionLevel(CallbackInfoReturnable<Integer> cir) {
        int level = ChronosSettings.defaultOpLevel;

        if (level != -1)
            cir.setReturnValue(level);
    }
}