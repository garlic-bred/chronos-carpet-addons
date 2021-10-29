package litetech.mixin.server.objective;

import litetech.helpers.ScoreboardObjectiveHelper;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScoreboardObjective.class)
public abstract class ScoreboardObjectiveMixin implements ScoreboardObjectiveHelper {
    private boolean frozen;

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
