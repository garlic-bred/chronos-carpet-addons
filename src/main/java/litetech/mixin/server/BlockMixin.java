package litetech.mixin.server;

import litetech.helpers.ServerPlayerEntityBedrockHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockMixin {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"))
    public void afterPlacement(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!(context.getPlayer() instanceof ServerPlayerEntity)) return;
        if (!context.getStack().getItem().getTranslationKey().equals("block.minecraft.piston")) return;

        ((ServerPlayerEntityBedrockHelper) context.getPlayer()).toggleHasPlacedPiston(true);
    }
}
