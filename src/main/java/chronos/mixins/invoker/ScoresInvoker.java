package chronos.mixins.invoker;

import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.scoreboard.Scores;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(Scores.class)
public interface ScoresInvoker {

    @Invoker("getScores")
    public Map<ScoreboardObjective, ScoreboardScore> getScoresInvoker();

}
