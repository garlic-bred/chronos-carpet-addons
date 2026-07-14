package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PrimedTnt.class)
public abstract class TntEntityMixin extends Entity {

    private static final EntityDataAccessor<Boolean> DISARMED = SynchedEntityData.defineId(TntEntityMixin.class, EntityDataSerializers.BOOLEAN);

    public TntEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    private boolean isDisarmed() {
        return this.entityData.get(DISARMED);
    }

    private void setDisarmed(boolean disarmed) {
        this.entityData.set(DISARMED, disarmed);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void initDisarmed(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DISARMED, false);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.SHEARS) && ChronosSettings.disarmTntWithShears && !isDisarmed()) {
            this.setDisarmed(true);
            this.gameEvent(GameEvent.SHEAR, player);
            this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
            heldItem.hurtAndBreak(1, player, hand);
            return InteractionResult.PASS;
        }

        return super.interact(player, hand);

    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/PrimedTnt;setFuse(I)V"))
    public void preventExplosion(PrimedTnt instance, int fuse) {
        if (this.isDisarmed()) {
            instance.setFuse(100 + (fuse % 20));
        } else {
            instance.setFuse(fuse);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void writeNbt(ValueOutput view, CallbackInfo ci) {
        view.putBoolean("disarmed", this.isDisarmed());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readNbt(ValueInput view, CallbackInfo ci) {
        this.setDisarmed(view.getBooleanOr("disarmed", false));
    }

}
