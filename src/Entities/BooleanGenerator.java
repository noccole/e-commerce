package Entities;

import java.util.Random;

/**
 * Created by Nicole on 18/12/16.
 */
public final class BooleanGenerator {

    public static boolean generateBoolean(double probabilityTrue) {

       // Random rand = new Random();


        return Math.random() >= 1.0 - probabilityTrue;
    }
}
