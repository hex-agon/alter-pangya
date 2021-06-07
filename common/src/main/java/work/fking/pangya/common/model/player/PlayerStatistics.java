package work.fking.pangya.common.model.player;

public record PlayerStatistics(int totalStroke,
                               int totalPlaytimeSeconds,
                               int avgStrokeTimeSeconds,
                               int outOfBoundsRate,
                               int totalDistance,
                               int totalHoles,
                               int holeInOnes) {
}
