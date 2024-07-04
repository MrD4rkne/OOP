package Simulation.Vehicles.Tram;

import Simulation.Common.Line;
import Simulation.Vehicles.Vehicle;

public class Tram extends Vehicle {
    public Tram(Line line, int sideNo, int capacity) {
        super(line,sideNo,capacity);
    }

    @Override
    public String getName() {
        return "Tram";
    }
}
