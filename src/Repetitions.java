public class Repetitions extends Activity {
    private int repetitions;

    public Repetitions() {
        super();
        this.repetitions = 0;
    }

    public Repetitions(int duration, int intensity, int repetitions) {
        super(duration, intensity);
        this.repetitions = repetitions;
    }

    public Repetitions(Repetitions repetitions) {
        super(repetitions);
        this.repetitions = repetitions.getRepetitions();
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


}