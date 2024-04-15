public class RepetitionWeight extends Activity {
    private int repetition;
    private int weight;

    public RepetitionWeight() {
        super();
        this.repetition = 0;
        this.weight = 0;
    }

    public RepetitionWeight(int duration, int intensity, int repetition, int weight) {
        super(duration, intensity);
        this.repetition = repetition;
        this.weight = weight;
    }

    public RepetitionWeight(RepetitionWeight repetitionWeight) {
        super(repetitionWeight);
        this.repetition = repetitionWeight.getRepetition();
        this.weight = repetitionWeight.getWeight();
    }

    public int getRepetition() {
        return this.repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Acivity {\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Repetition: " + this.repetition + " times,\n");
        sb.append("  Weight: " + this.weight + " kg\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        RepetitionWeight repetitionWeight = (RepetitionWeight) o;
        return (
            this.name.equals(repetitionWeight.getName()) &&
            this.duration == repetitionWeight.getDuration() &&
            this.intensity == repetitionWeight.getIntensity() &&
            this.repetition == repetitionWeight.getRepetition() &&
            this.weight == repetitionWeight.getWeight()
        );
    }

    @Override
    public RepetitionWeight clone() {
        return new RepetitionWeight(this);
    }

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, repetition, weight, duration, and intensity
        return 0;
    }
}