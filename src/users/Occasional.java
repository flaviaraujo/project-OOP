package src.users;

import src.User;

import java.io.Serializable;

public class Occasional extends User implements Serializable {

    private final int caloriesMultiplier = 70;

    public Occasional() {
        super();
    }

    public Occasional(int id, String name, String email, String address,
                      int heartRate, int weight, int height) {
        super(id, name, email, address, heartRate, weight, height);
    }

    public Occasional(Occasional u) {
        super(u);
    }

    @Override
    public User clone() {
        return new Occasional(this);
    }

    @Override
    public int getCaloriesMultiplier() {
        return this.caloriesMultiplier;
    }
}
