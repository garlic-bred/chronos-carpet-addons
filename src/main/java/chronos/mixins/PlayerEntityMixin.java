package chronos.mixins;

import chronos.ChronosSettings;
import chronos.mixins.accessors.EntityAccessorMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerEntityMixin implements EntityAccessorMixin {
    @Shadow
    @Final
    private Abilities abilities;

    @Shadow
    public abstract SoundSource getSoundSource();

//    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V", shift = At.Shift.BY, by = -2), cancellable = true)
    @Inject(method = "attack", at = @At(value = "HEAD"), cancellable = true)
    public void creativeKill(Entity target, CallbackInfo ci) {
        if (ChronosSettings.creativeOneHitKill
                && !this.accessorGetWorld().isClientSide() // this is to prevent a bug with ender dragons
                && this.abilities.instabuild
                && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            ServerLevel world = (ServerLevel) target.level();
            Consumer<Entity> instaKill = (target2) -> {
                if (target2 instanceof EnderDragonPart) {
                    Arrays.stream(((EnderDragonPart) target2).parentMob.getSubEntities()).forEach(e -> e.kill(world));
                    ((EnderDragonPart) target2).parentMob.kill(world);
                } else {
                    target2.kill(world);
                }
            };

            instaKill.accept(target);
            this.accessorGetWorld().playSound(null, this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 1.0F, 1.0F);
            if (this.invokerIsSneaking()) {
                this.accessorGetWorld().getEntitiesOfClass(Entity.class,
                                target.getBoundingBox().inflate(2.0D, 0.50D, 2.0D)).stream()
                        .filter(entity -> entity.isAttackable() && !(entity instanceof HangingEntity) && !(entity instanceof ArmorStand) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity))
                        .forEach(instaKill);
                this.accessorGetWorld().playSound(null, this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(),
                        SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1.0F, 1.0F);
            }
            ci.cancel();
        }
    }
}
