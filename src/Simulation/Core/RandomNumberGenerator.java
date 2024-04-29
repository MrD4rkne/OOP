package Simulation.Core;

import java.util.Random;

public class RandomNumberGenerator {
    public static int random(int bottom, int top){
        Random random = new Random();
        return random.nextInt(top - bottom) + bottom;
    }
}
