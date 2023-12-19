package chronos.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

public class CapturedDrops {

    private static PlayerEntity player;

    public static boolean capture(ItemStack item) {
        return player != null && player.getInventory().insertStack(item);
    }

    public static void clear() {
        player = null;
    }

    public static void startCapturing(PlayerEntity player) {
        CapturedDrops.player = player;
    }

    public static boolean isCapturing() {
        return player != null;
    }

    public static PlayerEntity getPlayer() {
        return player;
    }

}
