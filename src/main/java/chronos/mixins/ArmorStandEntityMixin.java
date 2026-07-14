package chronos.mixins;

import chronos.ChronosSettings;
import chronos.util.SitEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStand.class)
public abstract class ArmorStandEntityMixin extends LivingEntity implements SitEntity {

    private boolean sitEntity = false;

    @Shadow
    public abstract void kill(ServerLevel world);

    @Shadow
    protected abstract void brokenByAnything(ServerLevel world, DamageSource source);

    @Shadow
    protected abstract void brokenByPlayer(ServerLevel world, DamageSource damageSource);

    @Shadow
    protected abstract void setMarker(boolean marker);

    protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
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
            this.setPos(this.getX(), this.getY() + 0.16, this.getZ());
            this.kill((ServerLevel) passenger.level());
        }
        super.removePassenger(passenger);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void dropItem(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.is(DamageTypeTags.IS_EXPLOSION) && ChronosSettings.armorStandDropsItemFromExplosion) {
            this.brokenByPlayer(world, source);
            this.brokenByAnything(world, source);
            this.kill(world);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    private void postWriteCustomDataToNbt(ValueOutput view, CallbackInfo ci) {
        if (this.sitEntity) {
            view.putBoolean("SitEntity", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    private void postReadCustomDataFromNbt(ValueInput view, CallbackInfo ci) {
        this.sitEntity = view.getBooleanOr("SitEntity", false);
    }

}
