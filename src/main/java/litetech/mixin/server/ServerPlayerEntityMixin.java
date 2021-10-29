package litetech.mixin.server;

import litetech.helpers.ServerPlayerEntityBedrockHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityBedrockHelper {
    private int timeSincePistonPlaced;
    private boolean hasPlacedPiston;

    @Override
    public int getTimeSincePistonPlaced() {
        return timeSincePistonPlaced;
    }

    @Override
    public void incrementTimeSincePistonPlaced() {
        timeSincePistonPlaced++;
    }

    @Override
    public void resetTimeSincePistonPlaced() {
        timeSincePistonPlaced = 0;
        hasPlacedPiston = false;
    }

    @Override
    public boolean hasPlacedPiston() {
        return hasPlacedPiston;
    }

    @Override
    public void toggleHasPlacedPiston(boolean value) {
        hasPlacedPiston = value;
    }
}
