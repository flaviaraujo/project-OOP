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


}