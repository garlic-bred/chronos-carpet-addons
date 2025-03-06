package chronos.mixins;

import chronos.ChronosSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.LivingEntity.getSlotForHand;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity {

    private static final TrackedData<Boolean> DISARMED = DataTracker.registerData(TntEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);;

    public TntEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private boolean isDisarmed() {
        return this.dataTracker.get(DISARMED);
    }

    private void setDisarmed(boolean disarmed) {
        this.dataTracker.set(DISARMED, disarmed);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDisarmed(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(DISARMED, false);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        ItemStack heldItem = player.getStackInHand(hand);
        if (heldItem.isOf(Items.SHEARS) && ChronosSettings.disarmTntWithShears && !isDisarmed()) {
            this.setDisarmed(true);
            this.emitGameEvent(GameEvent.SHEAR, player);
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
            heldItem.damage(1, player, getSlotForHand(hand));
            return ActionResult.PASS;
        }

        return super.interact(player, hand);

    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/TntEntity;setFuse(I)V"))
    public void preventExplosion(TntEntity instance, int fuse) {
        if (this.isDisarmed()) {
            instance.setFuse(100 + (fuse % 20));
        } else {
            instance.setFuse(fuse);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("disarmed", this.isDisarmed());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        this.setDisarmed(nbt.getBoolean("disarmed"));
    }

}
