/**
 * Activity
 */
public class Activity {

    public enum Type {
        // TYPE               (dist,  alt, reps, wght,  hard, intensity)

        // distance and altimetry
        STREET_RUNNING        (1000,   50,    0,    0, false, 70),
        TRAIL_RUNNING         (1500,  300,    0,    0,  true, 80),
        MOUNTAIN_BIKING       (3000,  500,    0,    0,  true, 85),
        STREET_BIKING         (5000,  250,    0,    0, false, 75),

        // distance
        ROWING                (5000,    0,    0,    0, false, 70),
        TRACK_RUNNING         (2000,    0,    0,    0, false, 70),
        SKATING               (1500,    0,    0,    0, false, 60),

        // repetitions
        ABDOMINALS            (   0,    0,   50,    0, false, 50),
        STRETCHING            (   0,    0,   25,    0, false, 40),
        PUSHUPS               (   0,    0,   30,    0, false, 60),
        DIAMOND_PUSHUPS       (   0,    0,   20,    0, false, 65),
        PULLUPS               (   0,    0,   10,    0, false, 70),
        DIPS                  (   0,    0,   15,    0, false, 60),
        MUSCLEUPS             (   0,    0,    5,    0,  true, 80),

        // weighted repetitions
        WEIGHT_LIFTING_SMALL  (   0,    0,   10,   20, false, 60),
        WEIGHT_LIFTING_MEDIUM (   0,    0,   10,   40, false, 70),
        WEIGHT_LIFTING_LARGE  (   0,    0,   10,   60,  true, 80),
        LEG_EXTENSION_SMALL   (   0,    0,   10,   40, false, 55),
        LEG_EXTENSION_MEDIUM  (   0,    0,   10,   60, false, 65),
        LEG_EXTENSION_LARGE   (   0,    0,   10,  100,  true, 75);

        private int distance;
        private int altimetry;
        private int repetitions;
        private int weight;
        private boolean hard;
        private int intensity;

        Type(
            int distance, int altimetry, int repetitions,
            int weight, boolean hard, int intensity
        ) {
            this.distance = distance;
            this.altimetry = altimetry;
            this.repetitions = repetitions;
            this.weight = weight;
            this.hard = hard;
            this.intensity = intensity;
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

        public boolean getHard() { // equivalent to isHard()
            return this.hard;
        }

        public int getIntensity() {
            return this.intensity;
        }

        // The setter methods are not necessary since any changes can be done
        // directly in the enum declaration.

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
    }

    private int duration; // in minutes
    private Type type;

    public Activity() {
        this.duration = 0;
        this.type = null;
    }

    public Activity(int duration, Type type) {
        this.duration = duration;
        this.type = type;
    }

    public Activity(Activity activity) {
        this.duration = activity.getDuration();
        this.type = activity.getType();
    }

    public int getDuration() {
        return this.duration;
    }

    public Type getType() {
        return this.type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
