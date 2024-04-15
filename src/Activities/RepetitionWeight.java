public class RepetitionsWeight extends Activity {
    private int repetitions;
    private int weight;

    public RepetitionsWeight() {
        super();
        this.repetitions = 0;
        this.weight = 0;
    }

    public RepetitionsWeight(int duration, int intensity, int repetitions, int weight) {
        super(duration, intensity);
        this.repetitions = repetitions;
        this.weight = weight;
    }

    public RepetitionsWeight(RepetitionsWeight repetitionsWeight) {
        super(repetitionsWeight);
        this.repetitions = repetitionsWeight.getRepetitions();
        this.weight = repetitionsWeight.getWeight();
    }

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, repetitions, duration, and intensity
        return 0;
    }

    public int getRepetitions() {
        return this.repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}