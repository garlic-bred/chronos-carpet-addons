package johan.mixins;

import carpet.CarpetServer;
import chronos.ChronosSettings;
import com.google.common.collect.Lists;
import johan.commands.TotalCommand;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.ServerScoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends Scoreboard {

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

        for(int i = 0; i < 19; ++i) {
            if (this.getObjectiveForSlot(i) == objective) {
                list.add(new ScoreboardDisplayS2CPacket(i, objective));
            }
        }

        Iterator var5 = this.getAllPlayerScores(objective).iterator();

        while(var5.hasNext()) {
            ScoreboardPlayerScore scoreboardPlayerScore = (ScoreboardPlayerScore)var5.next();
            if (!ChronosSettings.scoreboardIgnoresBots || scoreboardPlayerScore.getScoreboard().getPlayerTeam(scoreboardPlayerScore.getPlayerName()) != null)
                list.add(new ScoreboardPlayerUpdateS2CPacket(ServerScoreboard.UpdateMode.CHANGE, scoreboardPlayerScore.getObjective().getName(), scoreboardPlayerScore.getPlayerName(), scoreboardPlayerScore.getScore()));
        }

        cir.setReturnValue(list);
    }

    private ScoreboardPlayerUpdateS2CPacket createPacket(ScoreboardObjective objective) {
        return new ScoreboardPlayerUpdateS2CPacket(ServerScoreboard.UpdateMode.CHANGE, objective.getName(), "Total", TotalCommand.getTotal(CarpetServer.minecraft_server.getCommandSource(), objective, !ChronosSettings.scoreboardIgnoresBots));
    }

}