package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BaseFireBlock.class)
public class AbstractFireBlockMixin {

    @Redirect(method = "canBePlacedAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canSurvive(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean canPlaceAt(BlockState blockState, LevelReader world, BlockPos pos){
        if(ChronosSettings.oldFlintAndSteelBehavior)
            return true;
        return blockState.canSurvive(world,pos);
    }

}