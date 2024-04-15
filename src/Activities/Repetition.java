public class Repetition extends Activity {
    private int repetition;

    public Repetition() {
        super();
        this.repetition = 0;
    }

    public Repetition(int duration, int intensity, int repetition) {
        super(duration, intensity);
        this.repetition = repetition;
    }

    public Repetition(Repetition repetition) {
        super(repetition);
        this.repetition = repetition.getRepetition();
    }

    public int getRepetition() {
        return this.repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Acivity {\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Repetition: " + this.repetition + " times\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        Repetition repetition = (Repetition) o;
        return (
            this.name.equals(repetition.getName()) &&
            this.duration == repetition.getDuration() &&
            this.intensity == repetition.getIntensity() &&
            this.repetition == repetition.getRepetition()
        );
    }

    @Override
    public Repetition clone() {
        return new Repetition(this);
    }

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, repetition, duration, and intensity
        return 0;
    }
}