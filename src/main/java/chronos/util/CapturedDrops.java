package chronos.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CapturedDrops {

    private static boolean capturing = false;
    private static List<ItemStack> drops = new ArrayList<>();

    public static void capture(ItemStack item) {
        drops.add(item);
    }

    public static List<ItemStack> getDrops() {
        return drops;
    }

    public static void clear() {
        drops.clear();
        capturing = false;
    }

    public static void startCapturing() {
        capturing = true;
    }

    public static boolean isCapturing() {
        return capturing;
    }

}
