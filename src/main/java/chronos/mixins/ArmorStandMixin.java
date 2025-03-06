package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandMixin {

    @Shadow
    public abstract void kill();

    @Shadow
    protected abstract void onBreak(ServerWorld world, DamageSource source);

    @Shadow protected abstract void breakAndDropItem(ServerWorld world, DamageSource damageSource);

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void dropItem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION) && ChronosSettings.armorStandItemFromExplosion) {
            World world = ((ArmorStandEntity) (Object) this).getWorld();
            if (world instanceof ServerWorld serverWorld) {
                this.breakAndDropItem(serverWorld, source);
                this.onBreak(serverWorld, source);
                this.kill();
                cir.setReturnValue(false);
            }
        }
    }

}
