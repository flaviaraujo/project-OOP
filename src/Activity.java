/**
 * Activity
 */
public class Activity {

    public enum ActivityType {
        // TYPE(distance, altimetry, repetitions, weight, hard)
        // distance and altimetry
        STREET_RUNNING(10000, 50, 0, 0, 0),
        TRAIL_RUNNING(15000, 500, 0, 0, 1),
        MOUNTAIN_BIKING(20000, 1000, 0, 0, 1),
        STREET_BIKING(20000, 100, 0, 0, 0),
        // distance
        // (ex: remo, corrida na pista de atletismo, patinagem)
        ROWING(10000, 0, 0, 0, 0),
        TRACK_RUNNING,
        SKATING,
        // repetitions
        // (ex: abdominais, alongamentos, flexões)
        ABDOMINALS,
        STRETCHING,
        PUSHUPS,
        PULLUPS,
        // weighted repetitions
        // (ex: levantamento de pesos, extensão de pernas)
        WEIGHT_LIFTING,
        LEG_EXTENSION,
    }

    private int duration; // in minutes
}
