package Simulation.Core;

import java.util.Random;

public class Losowanie {
    public static int losuj(int bottom, int top){
        Random random = new Random();
        return random.nextInt(top - bottom) + bottom;
    }
}
