package epsilon.mixins;

import chronos.ChronosSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = { "net/minecraft/entity/mob/EndermanEntity$PickUpBlockGoal"})
public abstract class EndermanEntityMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/Tag;)Z"))
    public boolean IsHoldableMelon(BlockState instance, Tag tag) {
        return ChronosSettings.antiEnderGriefExceptMelon ?
                instance.getBlock() == Blocks.MELON
                : instance.isIn(tag);
    }
}
