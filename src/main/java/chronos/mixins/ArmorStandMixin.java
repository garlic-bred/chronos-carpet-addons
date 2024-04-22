package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.registry.tag.DamageTypeTags;
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
    protected abstract void onBreak(DamageSource source);

    @Shadow protected abstract void breakAndDropItem(DamageSource damageSource);

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void dropItem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION) && ChronosSettings.armorStandItemFromExplosion) {
            this.breakAndDropItem(source);
            this.onBreak(source);
            this.kill();
            cir.setReturnValue(false);
        }
    }

}
