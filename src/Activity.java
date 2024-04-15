/**
 * Activity
 */
public class Activity {

    public enum Type {
        // TYPE         (dist, alt, reps, weight, hard)

        // distance and altimetry
        STREET_RUNNING  (1000,  50, 0, 0, false),
        TRAIL_RUNNING   (1500, 300, 0, 0, true),
        MOUNTAIN_BIKING (3000, 500, 0, 0, true),
        STREET_BIKING   (5000, 250, 0, 0, false),

        // distance
        ROWING        (5000, 0, 0, 0, false),
        TRACK_RUNNING (2000, 0, 0, 0, false),
        SKATING       (1500, 0, 0, 0, false),

        // repetitions
        ABDOMINALS      (0, 0, 50, 0, false),
        STRETCHING      (0, 0, 25, 0, false),
        PUSHUPS         (0, 0, 30, 0, false),
        DIAMOND_PUSHUPS (0, 0, 20, 0, false),
        PULLUPS         (0, 0, 10, 0, false),
        DIPS            (0, 0, 15, 0, false),
        MUSCLEUPS       (0, 0,  5, 0, true),

        // weighted repetitions
        WEIGHT_LIFTING_SMALL  (0, 0, 10,  20, false),
        WEIGHT_LIFTING_MEDIUM (0, 0, 10,  40, false),
        WEIGHT_LIFTING_LARGE  (0, 0, 10,  60, true),
        LEG_EXTENSION_SMALL   (0, 0, 10,  40, false),
        LEG_EXTENSION_MEDIUM  (0, 0, 10,  60, false),
        LEG_EXTENSION_LARGE   (0, 0, 10, 100, true);

        private int distance, altimetry, repetitions, weight;
        private boolean hard;

        Type(int distance, int altimetry, int repetitions, int weight, boolean hard) {
            this.distance = distance;
            this.altimetry = altimetry;
            this.repetitions = repetitions;
            this.weight = weight;
            this.hard = hard;
        }

        public int getDistance() {
            return this.distance;
        }

        public int getAltimetry() {
            return this.altimetry;
        }

        public int getRepetitions() {
            return this.repetitions;
        }

        public int getWeight() {
            return this.weight;
        }

        public boolean isDistanceAltimetry() {
            return this.distance > 0 && this.altimetry > 0;
        }

        public boolean isDistance() {
            return this.distance > 0 && this.altimetry == 0;
        }

        public boolean isRepetitions() {
            return this.repetitions > 0 && this.weight == 0;
        }

        public boolean isWeightedRepetitions() {
            return this.repetitions > 0 && this.weight > 0;
        }

        public boolean isHard() {
            return this.hard;
        }
    }

    private int duration; // in minutes
}
