package litetech.mixins.server.objective;

import chronos.mixins.invoker.ScoresInvoker;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import litetech.helpers.ScoreboardObjectiveHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.scoreboard.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {

    @Shadow @Final private final Map<String, Scores> scores = new Object2ObjectOpenHashMap<>();

    @Shadow public abstract Collection<ScoreboardObjective> getObjectives();

    @Shadow @Final private Object2ObjectMap<String, ScoreboardObjective> objectives;

    @Shadow public abstract @Nullable ReadableScoreboardScore getScore(ScoreHolder scoreHolder, ScoreboardObjective objective);

    @Shadow protected abstract Scores getScores(String scoreHolderName);

    @Inject(method = "toNbt", at = @At("HEAD"), cancellable = true)
    public void toNbt(CallbackInfoReturnable<NbtList> cir) {
        cir.cancel();

        NbtList nbtList = new NbtList();
        this.scores.forEach((name, scores) -> ((ScoresInvoker) scores).getScoresInvoker().forEach((objective, score) -> {
            NbtCompound nbtCompound = score.toNbt();
            nbtCompound.putString("Name", name);
            nbtCompound.putString("Objective", objective.getName());
            nbtCompound.putInt("Score", score.getScore());
            nbtCompound.putBoolean("Locked", score.isLocked());
            nbtCompound.putBoolean("Frozen", ((ScoreboardObjectiveHelper) objective).isFrozen());
            nbtList.add(nbtCompound);
        }));

        cir.setReturnValue(nbtList);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    public void readNbt(NbtList NbtList, CallbackInfo ci) {
        for(int i = 0; i < NbtList.size(); ++i) {
            NbtCompound compoundTag = NbtList.getCompound(i);
            ScoreboardObjective scoreboardObjective = null;
            for (ScoreboardObjective objective : this.getObjectives()) {
                if (objective.getName().equals(compoundTag.getString("Objective"))) {
                    scoreboardObjective = objective;
                }
            }
            String string = compoundTag.getString("Name");
            if (string.length() > 40) {
                string = string.substring(0, 40);
            }

            ScoreboardScore scoreboardScore = this.getScores(string).get(scoreboardObjective);
            if (scoreboardScore == null) continue;
            scoreboardScore.setScore(compoundTag.getInt("Score"));
            if (compoundTag.contains("Locked")) {
                scoreboardScore.setLocked(compoundTag.getBoolean("Locked"));
            }

            if (scoreboardObjective == null) continue;
            if (compoundTag.contains("Frozen")) {
                ((ScoreboardObjectiveHelper) scoreboardObjective).setFrozen(compoundTag.getBoolean("Frozen"));
            }
        }
    }
}
