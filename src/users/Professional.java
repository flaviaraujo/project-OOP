package src.users;

import src.User;

import java.io.Serializable;

public class Professional extends User implements Serializable {

    private final int caloriesMultiplier = 80;

    /* Contructors */
    public Professional() {
        super();
    }

    public Professional(int id, String name, String email, String address,
                      int heartRate, int weight, int height) {
        super(id, name, email, address, heartRate, weight, height);
    }

    public Professional(Professional u) {
        super(u);
    }

    @Override
    public User clone() {
        return new Professional(this);
    }

    @Override
    public int getCaloriesMultiplier() {
        return this.caloriesMultiplier;
    }
}
