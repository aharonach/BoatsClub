package controllers;

import engine.BCEngine;

public class Entities {
    public BCEngine engine() {
        return BCEngine.instance();
    }

    public Orders getOrders() {
        return (Orders) engine().getController("orders");
    }

    public Boats getBoats() {
        return (Boats) engine().getController("boats");
    }

    public Rowers getRowers() {
        return (Rowers) engine().getController("rowers");
    }

    public Activities getActivities() {
        return (Activities) engine().getController("activities");
    }
}
