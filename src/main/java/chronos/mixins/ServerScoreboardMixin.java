package chronos.mixins;

import carpet.CarpetServer;
import chronos.ChronosSettings;
import com.google.common.collect.Lists;
import chronos.commands.TotalCommand;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends Scoreboard {

    @Inject(method = "onScoreChanged", at = @At("HEAD"), cancellable = true)
    public void updateScore(ScoreHolder scoreHolder, Objective objective, Score score, CallbackInfo ci) {
        if (ChronosSettings.scoreboardIgnoresBots && objective.getScoreboard().getPlayersTeam(scoreHolder.getDisplayName().getString()) == null)
            ci.cancel();
        else {
            if (objective != null && ChronosSettings.totalScore)
                CarpetServer.minecraft_server.getPlayerList().broadcastAll(createTotalPacket(objective));
        }
    }

    @Inject(method = "getStartTrackingPackets", at = @At("HEAD"), cancellable = true)
    public void createChangePackets(Objective objective, CallbackInfoReturnable<List<Packet<?>>> cir) {
        List<Packet<?>> list = Lists.newArrayList();
        list.add(new ClientboundSetObjectivePacket(objective, 0));
        if (ChronosSettings.totalScore)
            list.add(createTotalPacket(objective));

        DisplaySlot[] var3 = DisplaySlot.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            DisplaySlot scoreboardDisplaySlot = var3[var5];
            if (this.getDisplayObjective(scoreboardDisplaySlot) == objective) {
                list.add(new ClientboundSetDisplayObjectivePacket(scoreboardDisplaySlot, objective));
            }
        }

        for (PlayerScoreEntry entry : this.listPlayerScores(objective)) {
            // only players on a team will be added to the scoreboard
            if (!ChronosSettings.scoreboardIgnoresBots || this.getPlayersTeam(entry.ownerName().getString()) != null)
                list.add(new ClientboundSetScorePacket(
                        entry.owner(),
                        objective.getName(),
                        entry.value(),
                        Optional.ofNullable(entry.display()),
                        Optional.ofNullable(entry.numberFormatOverride())
                ));
        }

        cir.setReturnValue(list);
    }

    private ClientboundSetScorePacket createTotalPacket(Objective objective) {
        return new ClientboundSetScorePacket(
                "Total",
                objective.getName(),
                TotalCommand.getTotal(CarpetServer.minecraft_server.createCommandSourceStack(), objective, !ChronosSettings.scoreboardIgnoresBots),
                Optional.empty(),
                Optional.empty()
        );
    }

}