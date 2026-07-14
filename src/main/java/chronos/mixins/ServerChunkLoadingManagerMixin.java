package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkMap.class)
public class ServerChunkLoadingManagerMixin {

    @Redirect(method = "onChunkReadyToSend", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkTrackingView;contains(Lnet/minecraft/world/level/ChunkPos;)Z"))
    public boolean alwaysWithinDistance(ChunkTrackingView instance, ChunkPos pos) {
        if (ChronosSettings.squareViewDistance) {
            return true;
        } else {
            return instance.contains(pos);
        }
    }

}
