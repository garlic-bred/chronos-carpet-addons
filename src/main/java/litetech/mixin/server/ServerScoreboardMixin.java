package litetech.mixin.server;

import chronos.ChronosSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends Scoreboard  {
    @Shadow
    @Final
    private MinecraftServer server;
    private ScoreboardPlayerScore score;

    @Inject(
            method = "updateScore",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/Packet;)V"
            )
    )
    public void updateScore(ScoreboardPlayerScore score, CallbackInfo ci) {
        ScoreboardObjective objective = score.getObjective();
        if (ChronosSettings.totalScore)
            this.server.getPlayerManager().sendToAll(this.createTotalPacket(objective));
    }

    @Inject(
            method = "createChangePackets",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void addTotalScore(ScoreboardObjective objective, CallbackInfoReturnable<List<Packet<?>>> cir,
                              List<Packet<?>> list) {
        if (ChronosSettings.totalScore)
            list.add(this.createTotalPacket(objective));
    }

    @Inject(
            method = "createChangePackets",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/network/packet/s2c/play/ScoreboardPlayerUpdateS2CPacket"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void getPlayerScore(ScoreboardObjective objective, CallbackInfoReturnable<List<Packet<?>>> cir,
                               List<Packet<?>> list, Iterator<?> var5, ScoreboardPlayerScore score) {
        this.score = score;
    }

    public ScoreboardPlayerUpdateS2CPacket createTotalPacket(ScoreboardObjective objective) {
        int i = 0;
        for (ScoreboardPlayerScore score : this.getAllPlayerScores(objective))
            i += score.getScore();

        return new ScoreboardPlayerUpdateS2CPacket(ServerScoreboard.UpdateMode.CHANGE, objective.getName(),
                "Total", i);
    }
}

