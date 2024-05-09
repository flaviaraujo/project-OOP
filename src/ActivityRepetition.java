package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/* Auxiliary class to create plan based on user goals */
public class ActivityRepetition {

    private Activity activity;
    private int repetitionsLeft;

    public ActivityRepetition() {
        this.activity = null;
        this.repetitionsLeft = 0;
    }

    public ActivityRepetition(Activity activity, int repetitionsLeft) {
        this.activity = activity.clone();
        this.repetitionsLeft = repetitionsLeft;
    }

    public Activity getActivity() {
        return this.activity.clone();
    }

    public int getRepetitionsLeft() {
        return this.repetitionsLeft;
    }

    public void setRepetitionsLeft(int repetitionsLeft) {
        this.repetitionsLeft = repetitionsLeft;
    }

    public ArrayList<ActivityRepetition> clean(ArrayList<ActivityRepetition> ars) {
        // remove activities with 0 repetitions left
        Iterator<ActivityRepetition> iterator = ars.iterator();
        while (iterator.hasNext()) {
            ActivityRepetition ar = iterator.next();
            if (ar.getRepetitionsLeft() == 0) {
                iterator.remove();
            }
        }
        return ars;
    }

    public ArrayList<ActivityRepetition> sort(
        ArrayList<ActivityRepetition> ar,
        int maxActivitiesPerDay,
        User user
    ) {
        ar = clean(ar);

        Comparator<ActivityRepetition> activityRepetitionComparator = (ar1, ar2) -> {
            int calories1 = ar1.getActivity().calculateCalories(user) * Math.min(ar1.getRepetitionsLeft(), maxActivitiesPerDay);
            int calories2 = ar2.getActivity().calculateCalories(user) * Math.min(ar2.getRepetitionsLeft(), maxActivitiesPerDay);
            return Integer.compare(calories2, calories1);
        };

        Collections.sort(ar, activityRepetitionComparator);
        return ar;
    }

    public ArrayList<ActivityRepetition> create(
        ArrayList<Activity> a,
        int maxRepetitionPerWeek,
        int maxActivitiesPerDay,
        User user,
        boolean isHard
    ) {
        ArrayList<ActivityRepetition> ar = new ArrayList<ActivityRepetition>();

        for (Activity activity : a) {
            if (activity.getHard() == isHard) {
                ar.add(new ActivityRepetition(activity, maxRepetitionPerWeek));
            }
        }

        ar = sort(ar, isHard ? 1 : maxActivitiesPerDay, user);
        return ar;
    }
}
