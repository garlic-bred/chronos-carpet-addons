package chronos.mixins;

import chronos.ChronosSettings;
import chronos.util.SitEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends LivingEntity implements SitEntity {

    private boolean sitEntity = false;

    @Shadow
    public abstract void kill(ServerWorld world);

    @Shadow
    protected abstract void onBreak(ServerWorld world, DamageSource source);

    @Shadow
    protected abstract void breakAndDropItem(ServerWorld world, DamageSource damageSource);

    @Shadow
    protected abstract void setMarker(boolean marker);

    protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isSitEntity() {
        return sitEntity;
    }

    @Override
    public void setSitEntity(boolean isSitEntity) {
        this.sitEntity = isSitEntity;
        this.setMarker(isSitEntity);
        this.setInvisible(isSitEntity);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (this.isSitEntity()) {
            this.setPosition(this.getX(), this.getY() + 0.16, this.getZ());
            this.kill((ServerWorld) passenger.getWorld());
        }
        super.removePassenger(passenger);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void dropItem(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION) && ChronosSettings.armorStandDropsItemFromExplosion) {
            this.breakAndDropItem(world, source);
            this.onBreak(world, source);
            this.kill(world);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "RETURN"))
    private void postWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.sitEntity) {
            nbt.putBoolean("SitEntity", true);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "RETURN"))
    private void postReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("SitEntity", NbtElement.BYTE_TYPE)) {
            this.sitEntity = nbt.getBoolean("SitEntity");
        }
    }

}
