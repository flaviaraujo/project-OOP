package src.users;

import src.User;

import java.io.Serializable;

public class Amateur extends User implements Serializable {

    private final int caloriesMultiplier = 80;

    public Amateur() {
        super();
    }

    public Amateur(int id, String name, String email, String address,
                      int heartRate, int weight, int height) {
        super(id, name, email, address, heartRate, weight, height);
    }

    public Amateur(Amateur u) {
        super(u);
    }

    @Override
    public User clone() {
        return new Amateur(this);
    }

    @Override
    public int getCaloriesMultiplier() {
        return this.caloriesMultiplier;
    }
}
