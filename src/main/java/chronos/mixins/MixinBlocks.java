package chronos.mixins;

import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Blocks.class)
//directly sets the hardness value of deepslate, not currently used since its not toggleable
public class MixinBlocks {
    @ModifyConstant(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=deepslate")),constant = @Constant(floatValue=3.0F, ordinal = 0))
    private static float changeHardness(float h) {
        return 1.6F;
    }
}
