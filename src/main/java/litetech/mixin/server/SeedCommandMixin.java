package litetech.mixin.server;

import chronos.ChronosSettings;
import net.minecraft.server.command.SeedCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(SeedCommand.class)
public abstract class SeedCommandMixin {
    @ModifyConstant(method = "register", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=seed")), constant = @Constant(intValue=2, ordinal = 0))
    private static int changePermissionLevel(int p) {
        return ChronosSettings.seedPermissionLevel;
    }
    /*
    @ModifyArg(method = "register(Lcom/mojang/brigadier/CommandDispatcher;B)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;hasPermissionLevel(I)Z"))
    private static int register(int original) {
        return ChronosSettings.seedPermissionLevel;
    }
    @ModifyArg(method = "r", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/command/ServerCommandSource;hasPermissionLevel(I)Z"))
    private static int register(int original) {
        return ChronosSettings.seedPermissionLevel;
    }
    */
}
