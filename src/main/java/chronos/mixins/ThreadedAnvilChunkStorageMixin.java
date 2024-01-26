package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {

    @Inject(method = "isWithinDistance", at = @At("HEAD"), cancellable = true)
    private static void renderSquareChunks(int x1, int z1, int x2, int z2, int distance, CallbackInfoReturnable<Boolean> cir) {
        if (ChronosSettings.squareViewDistance) cir.setReturnValue(true);
    }

}
