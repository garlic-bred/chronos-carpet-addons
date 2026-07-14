package chronos.mixins;

import chronos.ChronosSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import chronos.util.SitEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {
    @Shadow
    public ServerGamePacketListenerImpl connection;
    private int sneakTimes = 0;
    private long lastSneakTime = 0;

    public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public void setShiftKeyDown(boolean sneaking) {
        if (!ChronosSettings.playerSit || (sneaking && this.isShiftKeyDown())) {
            super.setShiftKeyDown(sneaking);
            return;
        }

        if (sneaking) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastSneakTime < 300 && sneakTimes == 0) {
                return;
            }
            super.setShiftKeyDown(true);
            if (this.onGround() && nowTime - lastSneakTime < 300) {
                sneakTimes += 1;
                if (sneakTimes == 3) {
                    Level world = super.level();
                    ArmorStand armorStandEntity = new ArmorStand(world, this.getX(), this.getY(), this.getZ());
                    ((SitEntity) armorStandEntity).setSitEntity(true);
                    armorStandEntity.setYRot(this.getRotationVector().y);
                    world.addFreshEntity(armorStandEntity);
                    this.setShiftKeyDown(false);
                    this.startRiding(armorStandEntity);
                    sneakTimes = 0;
                }
            } else {
                sneakTimes = 1;
            }
            lastSneakTime = nowTime;
        } else {
            super.setShiftKeyDown(false);
            // 同步潜行状态到客户端
            // 如果不同步的话客户端会认为仍在潜行，从而碰撞箱的高度会计算错误
            // Synchronize the sneak state with the client
            // If you don't synchronize, the client will think you're still sneaking, and the height of the collision box will be miscalculated.
            if (sneakTimes == 0 && this.connection != null) {
                this.connection.send(new ClientboundSetEntityDataPacket(this.getId(), this.getEntityData().getNonDefaultValues()));
            }
        }
    }

}
