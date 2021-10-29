package litetech.helpers;

public interface ServerPlayerEntityBedrockHelper {
    int getTimeSincePistonPlaced();
    void incrementTimeSincePistonPlaced();
    void resetTimeSincePistonPlaced();
    boolean hasPlacedPiston();
    void toggleHasPlacedPiston(boolean value);
}
