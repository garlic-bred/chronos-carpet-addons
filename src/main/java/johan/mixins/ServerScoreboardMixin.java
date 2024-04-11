package johan.mixins;

import carpet.CarpetServer;
import chronos.ChronosExtension;
import chronos.ChronosSettings;
import com.google.common.collect.Lists;
import johan.commands.TotalCommand;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreUpdateS2CPacket;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;
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
    public void updateScore(ScoreHolder scoreHolder, ScoreboardObjective objective, ScoreboardScore score, CallbackInfo ci) {
        if (ChronosSettings.scoreboardIgnoresBots && objective.getScoreboard().getScoreHolderTeam(scoreHolder.getNameForScoreboard()) == null)
            ci.cancel();
        else {
            if (objective != null && ChronosSettings.totalScore)
                CarpetServer.minecraft_server.getPlayerManager().sendToAll(createTotalPacket(objective));
        }
    }

    @Inject(method = "createChangePackets", at = @At("HEAD"), cancellable = true)
    public void createChangePackets(ScoreboardObjective objective, CallbackInfoReturnable<List<Packet<?>>> cir) {
        List<Packet<?>> list = Lists.newArrayList();
        list.add(new ScoreboardObjectiveUpdateS2CPacket(objective, 0));

        ScoreboardDisplaySlot[] var3 = ScoreboardDisplaySlot.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ScoreboardDisplaySlot scoreboardDisplaySlot = var3[var5];
            if (this.getObjectiveForSlot(scoreboardDisplaySlot) == objective) {
                list.add(new ScoreboardDisplayS2CPacket(scoreboardDisplaySlot, objective));
            }
        }

        if (ChronosSettings.totalScore)
            list.add(createTotalPacket(objective));

        Iterator<ScoreboardEntry> playerScoreIter = this.getScoreboardEntries(objective).iterator();

        while(playerScoreIter.hasNext()) {
            ScoreboardEntry entry = playerScoreIter.next();
            if (!ChronosSettings.scoreboardIgnoresBots || objective.getScoreboard().getScoreHolderTeam(entry.owner()) != null)
                list.add(new ScoreboardScoreUpdateS2CPacket(entry.owner(), objective.getName(), entry.value(), entry.display(), entry.numberFormatOverride()));
        }

        cir.setReturnValue(list);
    }

    private ScoreboardScoreUpdateS2CPacket createTotalPacket(ScoreboardObjective objective) {
        ChronosExtension.LOGGER.info("total packet: " + objective.getName() + objective.getDisplayName());
        return new ScoreboardScoreUpdateS2CPacket("Total", objective.getName(), TotalCommand.getTotal(CarpetServer.minecraft_server.getCommandSource(), objective, !ChronosSettings.scoreboardIgnoresBots), Text.literal("Total"), objective.getNumberFormat());
    }

}