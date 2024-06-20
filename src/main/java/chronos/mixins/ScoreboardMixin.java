package chronos.mixins;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import litetech.helpers.ScoreboardObjectiveHelper;
import net.minecraft.scoreboard.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {

    @Final
    @Shadow
    private final Reference2ObjectMap<ScoreboardCriterion, List<ScoreboardObjective>> objectivesByCriterion = new Reference2ObjectOpenHashMap<ScoreboardCriterion, List<ScoreboardObjective>>();

    @Shadow
    public abstract ScoreAccess getOrCreateScore(final ScoreHolder scoreHolder, final ScoreboardObjective objective, boolean forceWritable);

    @Inject(method = "forEachScore", at = @At("HEAD"), cancellable = true)
    private void ifNotFrozen(ScoreboardCriterion criterion, ScoreHolder scoreHolder, Consumer<ScoreAccess> action, CallbackInfo ci) {
        this.objectivesByCriterion.getOrDefault(criterion, Collections.emptyList()).forEach(objective -> {
            if (!((ScoreboardObjectiveHelper) objective).isFrozen())
                action.accept(this.getOrCreateScore(scoreHolder, objective, true));
        });
        ci.cancel();
    }

}
