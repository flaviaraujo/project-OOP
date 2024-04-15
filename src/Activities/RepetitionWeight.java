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

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, repetition, duration, and intensity
        return 0;
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

}