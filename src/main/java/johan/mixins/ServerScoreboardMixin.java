package johan.mixins;

import carpet.CarpetServer;
import chronos.ChronosSettings;
import com.google.common.collect.Lists;
import johan.commands.TotalCommand;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends Scoreboard {

    @Shadow @Final private Set<ScoreboardObjective> objectives;

    @Inject(method = "updateScore", at = @At("HEAD"), cancellable = true)
    public void updateScore(ScoreboardPlayerScore score, CallbackInfo ci) {
        if (ChronosSettings.scoreboardIgnoresBots && score.getScoreboard().getPlayerTeam(score.getPlayerName()) == null)
            ci.cancel();
        else {
            ScoreboardObjective objective = score.getObjective();
            if (objective != null && ChronosSettings.totalScore)
                CarpetServer.minecraft_server.getPlayerManager().sendToAll(createPacket(objective));
        }
    }

    @Inject(method = "createChangePackets", at = @At("HEAD"), cancellable = true)
    public void createChangePackets(ScoreboardObjective objective, CallbackInfoReturnable<List<Packet<?>>> cir) {
        List<Packet<?>> list = Lists.newArrayList();
        list.add(new ScoreboardObjectiveUpdateS2CPacket(objective, 0));
        if (ChronosSettings.totalScore)
            list.add(createPacket(objective));

        ScoreboardDisplaySlot[] var3 = ScoreboardDisplaySlot.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ScoreboardDisplaySlot scoreboardDisplaySlot = var3[var5];
            if (this.getObjectiveForSlot(scoreboardDisplaySlot) == objective) {
                list.add(new ScoreboardDisplayS2CPacket(scoreboardDisplaySlot, objective));
            }
        }

        Iterator playerScoreIter = this.getAllPlayerScores(objective).iterator();

        while(playerScoreIter.hasNext()) {
            ScoreboardPlayerScore scoreboardPlayerScore = (ScoreboardPlayerScore) playerScoreIter.next();
            if (!ChronosSettings.scoreboardIgnoresBots || scoreboardPlayerScore.getScoreboard().getPlayerTeam(scoreboardPlayerScore.getPlayerName()) != null)
                list.add(new ScoreboardPlayerUpdateS2CPacket(ServerScoreboard.UpdateMode.CHANGE, scoreboardPlayerScore.getObjective().getName(), scoreboardPlayerScore.getPlayerName(), scoreboardPlayerScore.getScore()));
        }

        cir.setReturnValue(list);
    }

    private ScoreboardPlayerUpdateS2CPacket createPacket(ScoreboardObjective objective) {
        return new ScoreboardPlayerUpdateS2CPacket(ServerScoreboard.UpdateMode.CHANGE, objective.getName(), "Total", TotalCommand.getTotal(CarpetServer.minecraft_server.getCommandSource(), objective, !ChronosSettings.scoreboardIgnoresBots));
    }

}