package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class MixinAbstractBlock {
    @Inject(at = @At("HEAD"), method = "calcBlockBreakingDelta(Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F", cancellable = true)
    public void checkDeepslate(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> ci){
        if(ChronosSettings.netheritePickaxeInstantMineDeepslate && state.getBlock().equals(Blocks.DEEPSLATE)){
            int i = player.canHarvest(state) ? 30 : 100;
            ci.setReturnValue(player.getBlockBreakingSpeed(state) / 1.6F / (float)i);
        }
    }
}
