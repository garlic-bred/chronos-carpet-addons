package litetech.mixin.server.op_level;

import com.mojang.authlib.GameProfile;
import litetech.helpers.PlayerManagerOPWithLevelHelper;
import net.minecraft.server.OperatorEntry;
import net.minecraft.server.OperatorList;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin implements PlayerManagerOPWithLevelHelper {
    @Final
    @Shadow
    private OperatorList ops;

    @Override
    public void addToOperators(GameProfile profile, int level, boolean bypassesPlayerLimit) {
        this.ops.add(new OperatorEntry(profile, level, bypassesPlayerLimit));

        PlayerManager manager = ((PlayerManager) (Object) this);
        ServerPlayerEntity player = manager.getPlayer(profile.getId());

        if (player != null)
            manager.sendCommandTree(player);
    }
}
