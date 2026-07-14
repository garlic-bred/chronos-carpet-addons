package chronos.mixins.accessors;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessorMixin {
    @Accessor("entityData")
    SynchedEntityData accessorGetDataTracker();

    @Accessor("level")
    Level accessorGetWorld();

    @Invoker("getX")
    double invokerGetX();

    @Invoker("getY")
    double invokerGetY();

    @Invoker("getZ")
    double invokerGetZ();

    @Invoker("isShiftKeyDown")
    boolean invokerIsSneaking();
}