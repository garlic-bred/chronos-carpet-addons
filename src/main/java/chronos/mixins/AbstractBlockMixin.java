package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class AbstractBlockMixin {
    @Inject(at = @At("HEAD"), method = "getDestroyProgress(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", cancellable = true)
    public void checkDeepslate(BlockState state, Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> ci){
        if(ChronosSettings.netheritePickaxeInstantMineDeepslate && state.getBlock().equals(Blocks.DEEPSLATE)){
            int i = player.hasCorrectToolForDrops(state) ? 30 : 100;
            ci.setReturnValue(player.getDestroySpeed(state) / 1.6F / (float)i);
        }
    }
}
