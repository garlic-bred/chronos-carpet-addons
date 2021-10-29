package litetech.mixin.server.objective;

import carpet.CarpetServer;
import litetech.commands.GoalCommand;
import litetech.helpers.ScoreboardObjectiveHelper;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(ScoreboardPlayerScore.class)
public abstract class ScoreboardPlayerScoreMixin {

    @Shadow @Final @Nullable private ScoreboardObjective objective;

    @Shadow public abstract String getPlayerName();

    @Inject(method = "incrementScore(I)V", at = @At("HEAD"), cancellable = true)
    public void incrementScore(int amount, CallbackInfo ci) {
        if (this.objective == null) return;

        if (((ScoreboardObjectiveHelper) this.objective).isFrozen()) {
            ci.cancel();
        }

        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(this.getPlayerName());
        if (GoalCommand.GOALS.containsKey(player)) {
            HashMap<ScoreboardObjective, GoalCommand.Goal> playerGoal = GoalCommand.GOALS.get(player);
            if (playerGoal.containsKey(this.objective)) playerGoal.get(this.objective).incrementScore(amount);
        }
    }

    @Inject(method = "incrementScore()V", at = @At("HEAD"), cancellable = true)
    public void incrementScore(CallbackInfo ci) {
        if (this.objective == null) return;

        if (((ScoreboardObjectiveHelper) this.objective).isFrozen()) {
            ci.cancel();
        }

        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(this.getPlayerName());
        if (GoalCommand.GOALS.containsKey(player)) {
            HashMap<ScoreboardObjective, GoalCommand.Goal> playerGoal = GoalCommand.GOALS.get(player);
            if (playerGoal.containsKey(this.objective)) playerGoal.get(this.objective).incrementScore();
        }
    }
}
