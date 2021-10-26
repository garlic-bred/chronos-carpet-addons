package carpet_extension.mixins;

import carpet.CarpetServer;
import carpet.network.CarpetClient;
import carpet.network.ClientNetworkHandler;
import carpet.script.language.Sys;
import carpet_extension.ChronosSettings;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Shadow private final MinecraftClient client;
    @Shadow private BlockPos currentBreakingPos = new BlockPos(-1, -1, -1);
    @Shadow private float currentBreakingProgress;
    @Shadow private float blockBreakingSoundCooldown;
    @Shadow private int blockBreakingCooldown;
    @Shadow private GameMode gameMode;
    @Shadow private boolean breakingBlock;
    @Shadow private ItemStack selectedStack;
    public MixinClientPlayerInteractionManager(MinecraftClient client, GameMode gameMode) {
        this.client = client;
        this.gameMode = gameMode;
        this.breakingBlock = false;
    }
    @Overwrite
    public boolean attackBlock(BlockPos pos, Direction direction) {
        if (this.client.player.isBlockBreakingRestricted(this.client.world, pos, this.gameMode)) {
            return false;
        } else if (!this.client.world.getWorldBorder().contains(pos)) {
            return false;
        } else {
            BlockState blockState2;
            if (this.gameMode.isCreative()) {
                blockState2 = this.client.world.getBlockState(pos);
                this.client.getTutorialManager().onBlockBreaking(this.client.world, pos, blockState2, 1.0F);
                this.sendPlayerAction(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction);
                this.breakBlock(pos);
                this.blockBreakingCooldown = 5;
            } else if (!this.breakingBlock || !this.isCurrentlyBreaking(pos)) {
                if (this.breakingBlock) {
                    this.sendPlayerAction(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, this.currentBreakingPos, direction);
                }

                blockState2 = this.client.world.getBlockState(pos);
                this.client.getTutorialManager().onBlockBreaking(this.client.world, pos, blockState2, 0.0F);
                this.sendPlayerAction(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction);
                boolean bl = !blockState2.isAir();
                if (bl && this.currentBreakingProgress == 0.0F) {
                    blockState2.onBlockBreakStart(this.client.world, pos, this.client.player);
                }

                float delta = blockState2.calcBlockBreakingDelta(this.client.player, this.client.player.world, pos);
                if(ChronosSettings.netheritePickaxeInstantMineDeepslate) {
                    if (EnchantmentHelper.getEfficiency(this.client.player) == 5 && (StatusEffectUtil.hasHaste(this.client.player) ? StatusEffectUtil.getHasteAmplifier(this.client.player) : 0) >= 1 && this.client.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.NETHERITE_PICKAXE && blockState2.getBlock() == Blocks.DEEPSLATE){
                        delta = 1.0F;
                    }
                }
                if (bl && delta >= 1.0F) {
                    this.breakBlock(pos);
                } else {
                    this.breakingBlock = true;
                    this.currentBreakingPos = pos;
                    this.selectedStack = this.client.player.getMainHandStack();
                    this.currentBreakingProgress = 0.0F;
                    this.blockBreakingSoundCooldown = 0.0F;
                    this.client.world.setBlockBreakingInfo(this.client.player.getId(), this.currentBreakingPos, (int)(this.currentBreakingProgress * 10.0F) - 1);
                }
            }

            return true;
        }
    }

    @Shadow public boolean breakBlock(BlockPos pos) { return true; }
    @Shadow private void sendPlayerAction(PlayerActionC2SPacket.Action action, BlockPos pos, Direction direction) {}
    @Shadow private boolean isCurrentlyBreaking(BlockPos pos) { return false; }
}
