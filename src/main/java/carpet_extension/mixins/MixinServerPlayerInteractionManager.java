package carpet_extension.mixins;

import carpet.script.language.Sys;
import carpet_extension.ChronosSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Objects;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
    @Shadow private static final Logger LOGGER = LogManager.getLogger();
    @Shadow private int tickCounter;
    @Shadow private int blockBreakingProgress;
    @Shadow protected ServerWorld world;
    @Shadow protected ServerPlayerEntity player;
    @Shadow private boolean failedToMine;
    @Shadow private BlockPos failedMiningPos;
    @Shadow private int failedStartMiningTime;
    @Shadow private BlockPos miningPos;
    @Shadow private boolean mining;
    @Shadow private int startMiningTime;
    @Shadow private GameMode gameMode;

    @Overwrite
    public void processBlockBreakingAction(BlockPos pos, net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action action, Direction direction, int worldHeight) {
        double d = this.player.getX() - ((double)pos.getX() + 0.5D);
        double e = this.player.getY() - ((double)pos.getY() + 0.5D) + 1.5D;
        double f = this.player.getZ() - ((double)pos.getZ() + 0.5D);
        double g = d * d + e * e + f * f;
        if (g > 36.0D) {
            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "too far"));
        } else if (pos.getY() >= worldHeight) {
            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "too high"));
        } else {
            BlockState blockState;
            if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                if (!this.world.canPlayerModifyAt(this.player, pos)) {
                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "may not interact"));
                    return;
                }

                if (this.isCreative()) {
                    this.finishMining(pos, action, "creative destroy");
                    return;
                }

                if (this.player.isBlockBreakingRestricted(this.world, pos, this.gameMode)) {
                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "block action restricted"));
                    return;
                }

                this.startMiningTime = this.tickCounter;
                float h = 1.0F;
                blockState = this.world.getBlockState(pos);
                if (!blockState.isAir()) {
                    blockState.onBlockBreakStart(this.world, pos, this.player);
                    h = blockState.calcBlockBreakingDelta(this.player, this.player.world, pos);
                    if(ChronosSettings.netheritePickaxeInstantMineDeepslate) {
                        if (EnchantmentHelper.getEfficiency(this.player) == 5 && (StatusEffectUtil.hasHaste(this.player) ? StatusEffectUtil.getHasteAmplifier(this.player) : 0) >= 1 && this.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.NETHERITE_PICKAXE && blockState.getBlock() == Blocks.DEEPSLATE){
                            h = 1.0F;
                        }
                    }
                }

                if (!blockState.isAir() && h >= 1.0F) {
                    this.finishMining(pos, action, "insta mine");
                } else {
                    if (this.mining) {
                        this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos), net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)"));
                    }

                    this.mining = true;
                    this.miningPos = pos.toImmutable();
                    int i = (int)(h * 10.0F);
                    this.world.setBlockBreakingInfo(this.player.getId(), pos, i);
                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "actual start of destroying"));
                    this.blockBreakingProgress = i;
                }
            } else if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
                if (pos.equals(this.miningPos)) {
                    int j = this.tickCounter - this.startMiningTime;
                    blockState = this.world.getBlockState(pos);
                    if (!blockState.isAir()) {
                        float k = blockState.calcBlockBreakingDelta(this.player, this.player.world, pos) * (float)(j + 1);
                        if (k >= 0.7F) {
                            this.mining = false;
                            this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
                            this.finishMining(pos, action, "destroyed");
                            return;
                        }

                        if (!this.failedToMine) {
                            this.mining = false;
                            this.failedToMine = true;
                            this.failedMiningPos = pos;
                            this.failedStartMiningTime = this.startMiningTime;
                        }
                    }
                }

                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "stopped destroying"));
            } else if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
                this.mining = false;
                if (!Objects.equals(this.miningPos, pos)) {
                    LOGGER.warn("Mismatch in destroy block pos: {} {}", this.miningPos, pos);
                    this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos), action, true, "aborted mismatched destroying"));
                }

                this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "aborted destroying"));
            }

        }
    }
    @Shadow
    public void finishMining(BlockPos pos, net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action action, String reason) {

    }

    @Shadow
    public boolean isCreative() {
        return false;
    }

    @Shadow
    public void update() {
    }

    @Shadow
    public boolean tryBreakBlock(BlockPos pos) {
        return true;
    }

    @Shadow
    private float continueMining(BlockState state, BlockPos pos, int i) {
        return 0;
    }
}
