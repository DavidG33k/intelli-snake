package dev.ian.snakeboi;


public class Scorer {

    private static int score;

    public static void score() {
        score += 10;
    }

    public static int getScore() {
        return score;
    }

    public static void reset() {
        score = 0;
    }
}
