package litetech.mixin.server;

import chronos.ChronosSettings;
import litetech.helpers.ServerPlayerEntityBedrockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class WorldMixin extends World {
    @Shadow @Final private MinecraftServer server;

    protected WorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Inject(method = "onBlockChanged", at = @At("RETURN"))
    public void blockChange(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
        if (!ChronosSettings.bedrockBrokenStatistics) return;

        if (oldBlock.getBlock().equals(Blocks.BEDROCK)) {
            for (ServerPlayerEntity player : this.server.getPlayerManager().getPlayerList()) {
                if (((ServerPlayerEntityBedrockHelper) player).hasPlacedPiston()) {
                    player.incrementStat(Stats.MINED.getOrCreateStat(Blocks.BEDROCK));
                }
            }
        }
    }

}
